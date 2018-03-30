package textures;

/**
 * Additional support for:
 * <p>
 * - Fake Lighting - if useFakeLighting field is set to true then it changes texture's normals to face upwards to mimic
 * better lighting. This feature is useful only for small objects placed on the ground level (like grass for example).
 * If true, it's executed in shader during rendering phase. Does not affect normals of the model.
 * <p>
 * - Texture Atlases - if textureAtlasSize is set to 1 then the texture is a normal texture with a single image.
 * Otherwise, if
 */
public class ModelTexture extends Texture {

    private boolean useFakeLighting = false;
    private int textureAtlasSize = 1; // Number of rows and columns in texture's atlas.

    public ModelTexture(int id) {
        super(id);
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    /**
     * @param useFakeLighting If true it changes texture's normals to face upwards to mimic better lighting.
     */
    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public int getTextureAtlasSize() {
        return textureAtlasSize;
    }

    /**
     * @param textureAtlasSize Number of rows and columns in texture's atlas. Cannot be less than 1.
     */
    public void setTextureAtlasSize(int textureAtlasSize) {
        this.textureAtlasSize = textureAtlasSize;
        if (this.textureAtlasSize < 1) {
            this.textureAtlasSize = 1;
        }
    }
}
