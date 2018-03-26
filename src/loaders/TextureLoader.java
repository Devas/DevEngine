package loaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.newdawn.slick.opengl.TextureLoader.getTexture;

/**
 * Loads textures from files using SlickUtil. Initializes OpenGL texture settings.
 */
public class TextureLoader {

    private static final float MIPMAP_FACTOR = -0.5f; // the bigger number the less resolution of rendered texture

    private List<Integer> textures = new ArrayList<>();

    /**
     * Make sure textures will be tiled properly by repeating
     */
    public TextureLoader() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    }

    /**
     * Loads texture from file using SlickUtil and returns OpenGL id of loaded texture.
     *
     * @param texturePath path to texture in res folder, uses PNG
     * @return id of loaded texture
     */
    public int load(String texturePath) {
        Texture texture = null;
        try {
            texture = getTexture("PNG", new FileInputStream("res/" + texturePath + ".png"));
            enableMipmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    private void enableMipmap() {
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, MIPMAP_FACTOR);
    }

    /**
     * Delete all textures.
     */
    public void cleanUp() {
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    private int getMaxTextureSize() {
        return GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
    }
}
