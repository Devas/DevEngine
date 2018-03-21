package lights;

import org.lwjgl.util.vector.Vector3f;

public class Light {

    /**
     * http://planetpixelemporium.com/tut orialpages/light.html
     */
    public enum Colour {

        RED(new Vector3f(1.0f, 0.0f, 0.0f)),
        GREEN(new Vector3f(0.0f, 1.0f, 0.0f)),
        BLUE(new Vector3f(0.0f, 0.0f, 1.0f)),
        GREY(new Vector3f(0.5f, 0.5f, 0.5f)),
        WHITE(new Vector3f(1.0f, 1.0f, 1.0f)),
        CANDLE(new Vector3f(255, 147, 41).normalise(null)),
        TUNGSTEN_40W(new Vector3f(255, 197, 143).normalise(null)),
        TUNGSTEN_100W(new Vector3f(255, 214, 170).normalise(null)),
        HALOGEN(new Vector3f(255, 241, 224).normalise(null)),
        CARBON_ARC(new Vector3f(255, 250, 244).normalise(null)),
        HIGH_NOON_SUN(new Vector3f(255, 255, 251).normalise(null)),
        DIRECT_SUNLIGHT(new Vector3f(255, 255, 255).normalise(null)),
        OVERCAST_SKY(new Vector3f(201, 226, 255).normalise(null)),
        CLEAR_BLUE_SKY(new Vector3f(64, 156, 255).normalise(null)),
        WARM_FLUORESCENT(new Vector3f(255, 244, 229).normalise(null)),
        STANDARD_FLUORESCENT(new Vector3f(244, 255, 250).normalise(null)),
        COOL_WHITE_FLUORESCENT(new Vector3f(212, 235, 255).normalise(null)),
        FULL_SPECTRUM_FLUORESCENT(new Vector3f(255, 244, 242).normalise(null)),
        GROW_LIGHT_FLUORESCENT(new Vector3f(255, 239, 247).normalise(null)),
        BLACK_LIGHT_FLUORESCENT(new Vector3f(167, 0, 255).normalise(null)),
        MERCURY_VAPOR(new Vector3f(216, 247, 255).normalise(null)),
        SODIUM_VAPOR(new Vector3f(255, 209, 178).normalise(null)),
        METAL_HALIDE(new Vector3f(242, 252, 255).normalise(null)),
        HIGH_PRESSURE_SODIUM(new Vector3f(255, 183, 76).normalise(null));

        private Vector3f colour;

        Colour(Vector3f colour) {
            this.colour = colour;
        }

        public Vector3f getColour() {
            return colour;
        }
    }

    private Vector3f position;
    private Vector3f colour;

    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.colour = colour;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
}
