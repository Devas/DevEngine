#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float pixelVisibility;

out vec4 out_Color;

// Samplers for textures
uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void) {

    // TODO make tiling variable uniform
    // TODO add 2nd blendmap
    // -Just use the second blend map as if there were 4 extra channels in the original blend map.
    // So you'd have r, g, b, a, r2, g2, b2, a2, and (1 - (all the rest added together))﻿
    // TODO modularize shaders

    vec4 blendMapColour = texture(blendMap, pass_textureCoords);

    float backgroundTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
    vec2 tiledCoordinates = pass_textureCoords * 40;

    vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoordinates) * backgroundTextureAmount;
    vec4 rTextureColour = texture(rTexture, tiledCoordinates) * blendMapColour.r;
    vec4 gTextureColour = texture(gTexture, tiledCoordinates) * blendMapColour.g;
    vec4 bTextureColour = texture(bTexture, tiledCoordinates) * blendMapColour.b;

    vec4 colour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

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
