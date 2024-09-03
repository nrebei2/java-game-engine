#version 410 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

uniform mat4 u_viewProj;
uniform mat4 u_model;

out vec2 uv;

void main()
{
    uv = aTexCoord;
    vec4 worldPos = u_model * vec4(aPos, 1);
    gl_Position = u_viewProj * worldPos;
}