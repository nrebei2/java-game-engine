#version 410 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec3 aTangent;
layout (location = 3) in vec3 aBiTangent;
layout (location = 4) in vec2 aTexCoord;

out VS_OUT {
    vec3 v_normalDir;
    vec3 v_tangentDir;
    vec3 v_biTangentDir;
    vec2 TexCoords;
    vec3 v_fragPos;
} vs_out;


uniform mat4 u_viewProj;
uniform mat4 u_model;

void main()
{
    vs_out.v_normalDir = (transpose(inverse(u_model)) * vec4(aNormal, 0)).xyz;
    vs_out.v_tangentDir = (u_model * vec4(aTangent, 0)).xyz;
    vs_out.v_biTangentDir = (u_model * vec4(aBiTangent, 0)).xyz;
    vs_out.TexCoords = aTexCoord;

    vec4 worldPos = u_model * vec4(aPos, 1);
    vs_out.v_fragPos = worldPos.xyz;
    gl_Position = worldPos;
}