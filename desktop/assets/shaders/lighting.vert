#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoords;

out VS_OUT {
    vec3 WorldPos;
    vec3 Normal;
    vec2 TexCoords;
} vs_out;

uniform mat4 u_viewProj;
uniform mat4 u_model;

void main()
{
    vs_out.WorldPos = u_model * aPos;
    vs_out.Normal = (inverse(transpose(u_model)) * vec4(aNormal, 0)).xyz;
    vs_out.TexCoords = aTexCoords;
    gl_Position = u_viewProj * vec4(vs_out.WorldPos, 1.0);
}

