#version 430 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec3 aTangent;
layout (location = 3) in vec3 aBiTangent;
layout (location = 4) in vec2 aTexCoord;

out vec3 v_normalDir;
out vec3 v_tangentDir;
out vec3 v_biTangentDir;
out vec2 TexCoords;
out vec3 v_fragPos;

layout (location = 0) uniform mat4 u_viewProj;
layout (location = 1) uniform mat4 u_model;

void main()
{
    v_normalDir = (transpose(inverse(u_model)) * vec4(aNormal, 0)).xyz;
    v_tangentDir = (u_model * vec4(aTangent, 0)).xyz;
    v_biTangentDir = (u_model * vec4(aBiTangent, 0)).xyz;
    TexCoords = aTexCoord;

    vec4 worldPos = u_model * vec4(aPos, 1);
    v_fragPos = worldPos.xyz;
    gl_Position = u_viewProj * worldPos;
}