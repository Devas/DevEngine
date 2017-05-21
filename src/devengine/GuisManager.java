package devengine;

import guis.GuiTexture;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static loaders.Loader.loader;

/**
 * This class allows to create and manage all gui textures.
 */
public class GuisManager {

    private List<GuiTexture> guiTextures = new ArrayList<>();

    public GuisManager() {
        createGuiTextures();
    }

    public List<GuiTexture> getGuiTextures() {
        return guiTextures;
    }

    private void createGuiTextures() {
        GuiTexture health = new GuiTexture(loader.loadTexture("gui/health"), new Vector2f(0.75f, 0.9f), new Vector2f(0.2f, 0.2f));
        guiTextures.add(health);

        GuiTexture healthInPixels = new GuiTexture(loader.loadTexture("gui/health"), new Vector2f(0.f, 0.35f), 50, 50);
        guiTextures.add(healthInPixels);

        GuiTexture coords = new GuiTexture(loader.loadTexture("gui/coords"), new Vector2f(-0.7f, -0.7f), new Vector2f(0.5f, 0.5f));
        guiTextures.add(coords);
    }

}
