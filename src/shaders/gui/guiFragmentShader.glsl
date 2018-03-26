#version 400 core

in vec2 textureCoords;

out vec4 out_colour;

uniform sampler2D guiTexture;

void main(void){

	out_colour = texture(guiTexture, textureCoords);

}