#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float pixelVisibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void) {

    vec4 colour = texture(textureSampler, pass_textureCoords);

    // If the alpha value of the colour is smaller than 0.5 then don't render that pixel.
    // Transparent areas of textures won't be rendered.
    if(colour.a < 0.5) {
        discard;
    }

    // Diffuse lighting and specular lighting computations
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLightVector = normalize(toLightVector);

    float brightness = dot(unitNormal, unitToLightVector);
    brightness = max(brightness, 0.1);
    vec3 diffuse = brightness * lightColour;

    vec3 unitToCameraVector = normalize(toCameraVector);
    vec3 lightDirection = -unitToLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
    specularFactor = max(specularFactor, 0.0);
    float dampedSpecularFactor = pow(specularFactor, shineDamper);
    vec3 specular = dampedSpecularFactor * reflectivity * lightColour;

    // Add lighting
    out_Color = vec4(diffuse, 1.0) * colour + vec4(specular, 1.0);

    // Add fog
    // pixelVisibility is in range [0,1].
    // 0.0 means object is rendered using completely skyColour (object is totally covered with fog so cannot be seen).
    // 1.0 means object is rendered using completely outColour (object is uneffected by fog so it's totally visible).
    out_Color = mix(vec4(skyColour, 1.0), out_Color, pixelVisibility);
}
