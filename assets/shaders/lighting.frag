#version 330 core

#define MAX_LIGHTS 16
#define SPOTLIGHT 0
#define POINTLIGHT 1

in VS_OUT {
    vec3 WorldPos;
    vec3 Normal;
    vec2 TexCoords;
} fs_in;

struct Light {
    int type;
    vec3 Position;
    vec3 Direction;
    vec3 Color;
    float SpotAngle;
    float Range;
    float Intensity;
};

uniform Light u_lights[MAX_LIGHTS];
uniform vec3 u_eyePos;
uniform sampler2D texture_diffuse0;
uniform int u_numLights;

void main()
{
    vec4 base = texture(texture_diffuse0, fs_in.TexCoords);
    if (base.a < 0.5)
        discard;
    vec3 color = base.xyz;

    // ambiance
    float ambient = 0.1;

    float diffuse = 0;
    float specular = 0;

    for (int i = 0; i < u_numLights; i++) {
       Light light = u_lights[i];
        switch (light.type) {
                case SPOTLIGHT:
                {
                    
                }
        }
    }

    // diffuse
    vec3 lightDir = normalize(u_sunDir);
    float diffuse = 0.6*max(dot(norm, lightDir), 0);

    // specular
    vec3 viewDir = normalize(u_viewPos - v_fragPos);
    vec3 halfAngle = normalize(viewDir + lightDir);
    float phongFac = pow(max(0, dot(norm, halfAngle)), 32);
    float geomFac = max(dot(norm, lightDir), 0);
    float specular = specularStrength * phongFac * geomFac;

    vec3 result = (ambient + diffuse + specular) * lightColor * color;
    FragColor = vec4(result, 1);
}  