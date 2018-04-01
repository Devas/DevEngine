package textures;

/**
 * Texture that supports:
 * <p>
 * - Fake Lighting - if fakeLighting field is set to true then it changes texture's normals to face upwards to mimic
 * better lighting. This feature is useful only for small objects placed on the ground level (like grass for example).
 * If true, it's executed in shader during rendering phase. Does not affect normals of the model.
 * <p>
 * - Texture Atlases - if textureAtlasSize is set to 1 then the texture consists of single image.
 * Otherwise, if textureAtlasSize is set to N then the texture consists of NxN images (N rows and N columns).
 */
public class ModelTexture extends Texture {

    private boolean fakeLighting = false; // Disabled by default
    private int textureAtlasSize = 1; // By default texture consists of single image

    public ModelTexture(int id) {
        super(id);
    }

    /**
     * Returns if fake lighting is enabled or disabled for this texture.
     *
     * @return true if texture uses fake lighting, false otherwise
     */
    public boolean isFakeLighting() {
        return fakeLighting;
    }

    /**
     * Sets fake lighting for texture. By default it's disabled.
     * Fake lighting means that texture's normals should face upwards to mimic better lighting.
     * If true it enables fake lighting. If false it disables fake lighting.
     * This field is only indicator to shader how it should deal with lighting for this texture
     * - it's implemented in shader.
     *
     * @param fakeLighting true to enable fake lighting, false to disable it
     */
    public void setFakeLighting(boolean fakeLighting) {
        this.fakeLighting = fakeLighting;
    }

    public int getTextureAtlasSize() {
        return textureAtlasSize;
    }

    /**
     * @param textureAtlasSize number of rows and columns in texture's atlas (cannot be less than 1)
     */
    public void setTextureAtlasSize(int textureAtlasSize) {
        this.textureAtlasSize = textureAtlasSize;
        if (this.textureAtlasSize < 1) {
            this.textureAtlasSize = 1;
        }
    }
}
