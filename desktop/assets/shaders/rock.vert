#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 2) in vec2 aTexCoord;
layout (location = 3) in mat4 aInstanceMatrix;

out vec2 TexCoords;

uniform mat4 u_viewProj;

void main()
{
    TexCoords = aTexCoord;
    gl_Position = u_viewProj * aInstanceMatrix * vec4(aPos, 1.0f);
}