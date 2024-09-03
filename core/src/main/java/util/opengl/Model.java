package util.opengl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import util.Matrix4;
import util.Vector3;
import util.opengl.TextureManager.Texture;
import util.opengl.attributes.FloatAttribute;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.assimp.Assimp.*;

// Adapted from https://learnopengl.com/code_viewer_gh.php?code=includes/learnopengl/model.h

/**
 * Holds a collection of meshes representing a loaded model.
 */
public class Model {

    /**
     * Underlying meshes
     */
    private Mesh[] meshes;
    private final String texPath;

    /**
     * @param name    Project path of model file
     * @param texPath Project path of textures location
     */
    public Model(String name, String texPath) {
        this.texPath = texPath;
        loadModel(name);
    }

    // loads a model with supported ASSIMP extensions from file and stores the resulting meshes in the meshes vector.
    private void loadModel(String name) {
        // read file via ASSIMP
        var scene = aiImportFile(
                name, aiProcess_Triangulate
                        | aiProcess_GenSmoothNormals
                        | aiProcess_FlipUVs
                        | aiProcess_CalcTangentSpace
                        | aiProcess_FlipWindingOrder
                        | aiProcess_OptimizeMeshes
                        | aiProcess_OptimizeGraph
        );

        // check for errors
        if (scene == null || (scene.mFlags() & AI_SCENE_FLAGS_INCOMPLETE) != 0 || scene.mRootNode() == null) {
            System.err.printf("Model %s failed to import\n", name);
            return;
        }

        int numMeshes = scene.mNumMeshes();
        PointerBuffer aiMeshes = scene.mMeshes();
        meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(aiMesh, scene);
            meshes[i] = mesh;
        }
    }

    // return a mesh object created from the extracted mesh data
    private Mesh processMesh(AIMesh mesh, AIScene scene) {
        int vertCount = mesh.mNumVertices();
        // Attribute data
        float[] positions = new float[vertCount * 3];
        float[] normals = new float[vertCount * 3];
        float[] texCoords = new float[vertCount * 2];
        float[] tangents = new float[vertCount * 3];
        float[] biTangents = new float[vertCount * 3];

        // Buffers
        var vPos = mesh.mVertices();
        var vNor = mesh.mNormals();
        var vTex = mesh.mTextureCoords(0);
        var vTan = mesh.mTangents();
        var vBi = mesh.mBitangents();

        for (int i = 0; i < vertCount; i++) {
            var pos = vPos.get();
            var nor = vNor.get();
            var tex = vTex.get();
            var tan = vTan.get();
            var bi = vBi.get();

            positions[i * 3] = pos.x();
            positions[i * 3 + 1] = pos.y();
            positions[i * 3 + 2] = pos.z();

            normals[i * 3] = nor.x();
            normals[i * 3 + 1] = nor.y();
            normals[i * 3 + 2] = nor.z();

            tangents[i * 3] = tan.x();
            tangents[i * 3 + 1] = tan.y();
            tangents[i * 3 + 2] = tan.z();

            biTangents[i * 3] = bi.x();
            biTangents[i * 3 + 1] = bi.y();
            biTangents[i * 3 + 2] = bi.z();

            texCoords[i * 2] = tex.x();
            texCoords[i * 2 + 1] = tex.y();
        }

        // indices
        int numFaces = mesh.mNumFaces();
        ArrayList<Integer> ind = new ArrayList<>(numFaces);
        AIFace.Buffer faces = mesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace face = faces.get(i);
            IntBuffer buffer = face.mIndices();
            while (buffer.remaining() > 0) {
                ind.add(buffer.get());
            }
        }

        // Create mesh
        Mesh nMesh = new Mesh();

        Geometry geometry = new Geometry();
        geometry.addAttribute("aPos", new FloatAttribute(3, positions, false));
        geometry.addAttribute("aTexCoord", new FloatAttribute(2, texCoords, false));
        geometry.addAttribute("aNormal", new FloatAttribute(3, normals, false));
        geometry.addAttribute("aBiTangent", new FloatAttribute(3, biTangents, false));
        geometry.addAttribute("aTangent", new FloatAttribute(3, tangents, false));
        geometry.setIndices(ind.stream().mapToInt(i -> i).toArray());

        Material mat = new Material();
        var material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));
        // maps
        loadMaterialTextures(material, aiTextureType_DIFFUSE, "texture_diffuse", mat);
        //loadMaterialTextures(material, aiTextureType_BUMP, "texture_opacity", mat);
        loadMaterialTextures(material, aiTextureType_SPECULAR, "texture_specular", mat);
        loadMaterialTextures(material, aiTextureType_HEIGHT, "texture_normal", mat);
        loadMaterialTextures(material, aiTextureType_OPACITY, "texture_opacity", mat);

        nMesh.setGeometry(geometry);
        nMesh.setMat(mat);

        return nMesh;
    }

    /**
     * @param mat      Object holding model material information
     * @param type     aiTextureType
     * @param name     Uniform name
     * @param material Material to add textures onto
     */
    private void loadMaterialTextures(AIMaterial mat, int type, String name, Material material) {
        for (int i = 0; i < aiGetMaterialTextureCount(mat, type); i++) {
            AIString path = AIString.calloc();
            aiGetMaterialTexture(mat, type, i, path, (IntBuffer) null, null, null, null, null, null);

            Texture texture = new Texture(texPath, path.dataString());
            material.addTexture(texture, name + i);
        }
    }

    // Convenience helpers

    /**
     * Set the model shader
     */
    public void setShader(String shaderName) {
        for (Mesh mesh : meshes) {
            mesh.getMat().setShader(shaderName);
        }
    }

    /**
     * Begin rendering sequence for this model. You must call his method before setting shader uniforms!
     */
    public void begin() {
        // All models have the same shader
        meshes[0].getMat().shader.bind();
    }

    /**
     * Renders the model onto the screen.
     */
    public void render() {
        for (Mesh mesh : meshes) {
            mesh.render();
        }
    }

    /**
     * See {@link Mesh#renderInstanced(int)}
     */
    public void renderInstanced(int count) {
        for (Mesh mesh : meshes) {
            mesh.renderInstanced(count);
        }
    }

    /**
     * End the drawing sequence of this model.
     */
    public void end() {
    }

    /**
     * Sets of view-projection matrix for rendering of this model. Call begin before this method.
     */
    public void setCombinedMatrix(Matrix4 mat) {
        // All models have the same shader
        meshes[0].getMat().setMat4("u_viewProj", mat);
    }

    /**
     * Sets the model matrix for this model. Call begin before this method.
     */
    public void setModelMatrix(Matrix4 model) {
        meshes[0].getMat().setMat4("u_model", model);
    }

    /**
     * Set the value of a uniform variable for the current attached shader
     *
     * @param name Name of uniform on shader
     * @param vec  value to set as
     */
    public void setVec3f(String name, Vector3 vec) {
        meshes[0].getMat().setVec3f(name, vec);
    }

    /**
     * Set the value of a uniform variable for the current attached shader
     *
     * @param name Name of uniform on shader
     * @param val  value to set as
     */
    public void setFloat(String name, float val) {
        meshes[0].getMat().setFloat(name, val);
    }

    public Mesh[] getMeshes() {
        return meshes;
    }
}
