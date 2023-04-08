#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D color;

void main()
{
    vec4 color = texture(color, TexCoord);
    FragColor = color;
}