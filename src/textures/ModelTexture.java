package textures;

public class ModelTexture {

    private int textureID;

    private float shineDamper = 1;
    private float reflectivity = 0;
    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public int getID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    /**
     * Change normals to face upwards to mimic better lighting.
     * Useful only for small objects placed on the ground level (like grass for example).
     * If true, it's executed in shader during rendering phase. Does not affect normals of the model.
     *
     * @param useFakeLighting
     */
    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

}
