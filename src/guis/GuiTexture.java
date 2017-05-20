package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {

    private int textureID;

    private Vector2f position;
    private Vector2f scale;

    /**
     * Top-left corner of the screen is (0, 0) and bottom-right is (1, 1) so position must be in this range.
     * Position is centre of guiTexture.
     * Scale is in range (0, 1) where 1 is full width / height of the screen.
     *
     * @param id
     * @param position
     * @param scale
     */
    public GuiTexture(int id, Vector2f position, Vector2f scale) {
        this.textureID = id;
        this.position = position;
        this.scale = scale;
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
