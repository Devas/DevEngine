package textures;

import display.DisplayManager;
import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {

    private int textureID;
    private Vector2f position;
    private Vector2f scale;

    /**
     * This constructor uses scale of texture in relation to the size of the screen.
     *
     * @param id       OpenGL texture id from texture loader
     * @param position The centre position of gui texture can be in range -1 to 1
     * @param scale    Scale is in relation to the size of the screen, 0 is none and 1 is full size of the screen
     */
    public GuiTexture(int id, Vector2f position, Vector2f scale) {
        this.textureID = id;
        this.position = position;
        this.scale = new Vector2f(scale.x / DisplayManager.ASPECT_RATIO, scale.y); // TODO adding projection matrix in Maths?
    }

    /**
     * This constructor uses scale of texture passed as width and height measured in pixels.
     *
     * @param id             Texture id from loader
     * @param position       The centre position of gui texture can be in range -1 to 1
     * @param widthInPixels  Width of the gui texture measured in pixels
     * @param heightInPixels Height of the gui texture measured in pixels
     */
    public GuiTexture(int id, Vector2f position, int widthInPixels, int heightInPixels) {
        this.textureID = id;
        this.position = position;
        float scaleX = ((float) widthInPixels) / DisplayManager.WIDTH;
        float scaleY = ((float) heightInPixels) / DisplayManager.HEIGHT;
        this.scale = new Vector2f(scaleX, scaleY);
    }

    public int getTextureID() {
        return textureID;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
