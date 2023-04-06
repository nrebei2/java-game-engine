#version 330 core
layout (location = 1) in vec3 aPos;

uniform mat4 u_viewProj;
uniform mat4 u_model;

void main()
{
    gl_Position = u_viewProj * u_model * vec4(aPos, 1.0);
}