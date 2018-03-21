package renderers;

import org.lwjgl.util.vector.Vector3f;

/**
 * Colour of the sky affects atmospheric fog. Sky colour is blended into output image.
 */
public enum SkyColour {

    ATMOSPHERIC_1(0.6f, 0.78f, 0.76f),
    ATMOSPHERIC_2(0.5f, 0.6f, 0.7f),
    HEAVY(0.5f, 0.5f, 0.5f),
    DESERT_DUST(new Vector3f(255, 197, 143).normalise(null));

    private Vector3f skyColour;

    Vector3f getSkyColour() {
        return skyColour;
    }

    SkyColour(float r, float g, float b) {
        this.skyColour = new Vector3f(r, g, b);
    }

    SkyColour(Vector3f skyColour) {
        this.skyColour = skyColour;
    }
}
