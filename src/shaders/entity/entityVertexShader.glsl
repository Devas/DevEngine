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

uniform int useFakeLighting; // Receives only 0 or 1 so it acts like boolean
uniform float textureAtlasSize;
uniform vec2 textureAtlasOffsets;

// TODO make uniform
const float fogDensity = 0.007; // 0.0035
const float fogGradient = 1.5;  // 5.0

void main(void) {

    // If textureAtlasSize = 1 then this acts like single texture i.e. pass_textureCoords = textureCoords
    pass_textureCoords = (textureCoords / textureAtlasSize) + textureAtlasOffsets;

    // MVP computations
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0); // Multiply 4x4 matrix by 4x1 vector = 4x1 vector
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCamera;

    //TODO move from shader to ModelTexture functionality
    // Change some normals to face upwards to mimic better lighting.
    // Useful only for small objects placed on the ground level (like grass for example).
    // We could alter the normals to face up in Java before they are sent, but we if-check uniform which isn't slow.
    vec3 actualNormal = normal;
    if (useFakeLighting > 0) {
        actualNormal = vec3(0.0, 1.0, 0.0);
    }

    // Lighting computations
    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
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
