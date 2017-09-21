package devengine;

import entities.Entity;
import entities.Player;
import guis.GuiRenderer;
import helpers.Light;
import helpers.cameras.Camera;
import helpers.cameras.EntityCamera;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.Log;
import renderers.MasterRenderer;
import terrains.Terrain;

import static loaders.Loader.loader;

class DevEngine {

    private final TerrainsManager terrainsManager;
    private final EntitiesManager entitiesManager;
    private final GuisManager guisManager;
    private final MasterRenderer masterRenderer;
    private final GuiRenderer guiRenderer;

    DevEngine() {
        DisplayManager.createDisplay();
        terrainsManager = new TerrainsManager();
        entitiesManager = new EntitiesManager(terrainsManager);
        guisManager = new GuisManager();
        masterRenderer = new MasterRenderer();
        guiRenderer = new GuiRenderer(loader);
        Log.setVerbose(false);
    }

    /**
     * Here is defined main loop.
     */
    void run() {

        Light light = new Light(new Vector3f(3000, 2000, -2000), Light.Colour.WHITE.getColour()); // 20000,40000,20000
        Player player = entitiesManager.getPlayerEntity();
        Camera camera = new EntityCamera(player);
//        Camera cameras = new KeyboardCamera(new Vector3f(25, 10, 70));
//        Camera cameras = new FPVCamera(new Vector3f(25, 10, 70));
//        Camera cameras = new EntityCamera(player, new Vector3f(25, 10, 70));
//        Camera cameras = new TestCamera(player);

        boolean paused = false;
        while (!Display.isCloseRequested()) {

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                break;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
                paused = !paused;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                camera.printCameraInfo();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
                masterRenderer.enableFillPolygonMode();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                masterRenderer.enableLinePolygonMode();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
                masterRenderer.enablePointPolygonMode();
            }

            // Keyboard / FPV
//            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
//                cameras.move();
//            } else {
//                player.move();
//            }

            // Movement of the player and the cameras
            player.move(terrainsManager.getTerrain(player));
            camera.move();

            // Assigning all entities to batches
            for (Entity entity : entitiesManager.getEntities()) {
                masterRenderer.processEntity(entity);
//                if (!paused) {
//                    entity.increaseRotation(0, 1, 0);
//                } else {
//                    entity.increaseRotation(0, 0, 0);
//                }
            }

            for (Terrain terrain : terrainsManager.getTerrains()) {
                masterRenderer.processTerrain(terrain);
            }

            masterRenderer.processEntity(player);

            masterRenderer.render(light, camera);

            guiRenderer.render(guisManager.getGuiTextures());

            DisplayManager.updateDisplay();
        }

        clenUpAll();
        DisplayManager.closeDisplay();
    }

    private void clenUpAll() {
        masterRenderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();
    }

}