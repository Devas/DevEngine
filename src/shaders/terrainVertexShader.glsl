#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float pixelVisibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

// TODO make uniform
const float fogDensity = 0.007;
const float fogGradient = 1.5;

void main(void) {

    pass_textureCoords = textureCoords;

    // MVP computations
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCamera;

    // Lighting computations
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
    vec3 cameraPosition = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
    toCameraVector = cameraPosition - worldPosition.xyz;

    // Pixel visibility computations used by fog.
    // fogDensity determines the thickness of the fog (increasing this will decrease general visibility of the scene).
    // fogGradient determines how quickly the visibility decreses with distance (increasing this makes the transition
    // from full visibility to 0 visibility much more smaller).
    float distanceToCamera = length(positionRelativeToCamera.xyz);
    pixelVisibility = exp(-pow(distanceToCamera * fogDensity, fogGradient));
    pixelVisibility = clamp(pixelVisibility, 0.0, 1.0);

}
