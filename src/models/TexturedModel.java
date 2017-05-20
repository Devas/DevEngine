package models;

import textures.ModelTexture;

/**
 * This class combines a 3D model and a texture.
 */
public class TexturedModel { // TODO untextured model

    private RawModel rawModel;
    private ModelTexture texture;

    public TexturedModel(RawModel model, ModelTexture texture) {
        this.rawModel = model;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }

}
