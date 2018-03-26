package devengine;

import loaders.TextureLoader;
import org.lwjgl.util.vector.Vector2f;
import textures.GuiTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows to create and manage all gui textures.
 */
public class GuisManager {

    private final TextureLoader textureLoader;
    private List<GuiTexture> guiTextures = new ArrayList<>();

    public GuisManager(TextureLoader textureLoader) {
        this.textureLoader = textureLoader;
        createGuiTextures();
    }

    public List<GuiTexture> getGuiTextures() {
        return guiTextures;
    }

    private void createGuiTextures() {
        GuiTexture health = new GuiTexture(textureLoader.load("gui/health"), new Vector2f(0.75f, 0.9f), new Vector2f(0.2f, 0.2f));
        guiTextures.add(health);

        GuiTexture healthInPixels = new GuiTexture(textureLoader.load("gui/health"), new Vector2f(0.f, 0.35f), 50, 50);
        guiTextures.add(healthInPixels);

        GuiTexture coords = new GuiTexture(textureLoader.load("gui/coords"), new Vector2f(-0.7f, -0.7f), new Vector2f(0.5f, 0.5f));
        guiTextures.add(coords);
    }
}
