#version 330 core
layout (location = 1) in vec3 aPos;
layout (location = 2) in vec2 aTexCoord;

uniform mat4 u_viewProj;
uniform mat4 u_model;

out vec2 TexCoord;

void main()
{
    gl_Position = u_viewProj * u_model * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
}