#version 330 core
layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
    vec3 v_normalDir;
    vec3 v_tangentDir;
    vec3 v_biTangentDir;
    vec2 TexCoords;
    vec3 v_fragPos;
} gs_in[];

out GS_OUT {
    vec3 v_normalDir;
    vec3 v_tangentDir;
    vec3 v_biTangentDir;
    vec2 TexCoords;
    vec3 v_fragPos;
} gs_out;

uniform mat4 u_viewProj;
uniform float u_time;

vec4 explodeVertex(vec4 vert) {
    vec3 a = gl_in[2].gl_Position.xyz - gl_in[0].gl_Position.xyz;
    vec3 b = gl_in[1].gl_Position.xyz - gl_in[0].gl_Position.xyz;
    vec3 nor = cross(a, b);
    return vert + pow(sin(u_time) + 1, 2.0) * vec4(normalize(nor), 0);
}

void main() {
    for (int i = 0; i < 3; i++) {
        gs_out.v_normalDir = gs_in[i].v_normalDir;
        gs_out.v_tangentDir = gs_in[i].v_tangentDir;
        gs_out.v_biTangentDir = gs_in[i].v_biTangentDir;
        gs_out.TexCoords = gs_in[i].TexCoords;
        vec4 pos = explodeVertex(gl_in[i].gl_Position);
        gs_out.v_fragPos = pos.xyz;
        gl_Position = u_viewProj * pos;
        EmitVertex();
    }
    EndPrimitive();
}