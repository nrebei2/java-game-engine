#version 330 core
layout (location = 0) in vec3 aPos;

out vec3 TexCoords;

uniform mat4 u_viewProj;

void main()
{
    TexCoords = aPos;
    vec4 pos = u_viewProj * vec4(aPos, 1.0);
    // Force z-depth 1
    gl_Position = pos.xyww;
}