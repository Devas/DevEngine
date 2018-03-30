package textures;

/**
 * Base class for textures.
 */
// TODO maybe Texture should contain only textureId if other fields are not needed in some textures?
public class Texture {

    private int textureID; // TODO rename to ID?
    private float shineDamper = 1;
    private float reflectivity = 0;

    public Texture(int textureID) {
        this.textureID = textureID;
    }

    public int getID() { // TODO rename?
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
}
