package devengine;

import entities.Entity;
import entities.Player;
import helpers.Light;
import helpers.camera.Camera;
import helpers.camera.EntityCamera;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.Log;
import renderers.MasterRenderer;
import terrains.Terrain;

import static loaders.Loader.loader;

public class DevEngine {

    private static final TerrainsManager terrainsManager;
    private static final EntitiesManager entitiesManager;

    static {
        DisplayManager.createDisplay();
        terrainsManager = new TerrainsManager();
        entitiesManager = new EntitiesManager(terrainsManager);
        Log.setVerbose(false);
    }

    /**
     * Method with the engine's main loop
     */
    public static void main(String[] args) {

        Light light = new Light(new Vector3f(3000, 2000, -2000), Light.Colour.WHITE.getColour()); // 20000,40000,20000
        MasterRenderer masterRenderer = new MasterRenderer();
        Player player = entitiesManager.getPlayerEntity();
        Camera camera = new EntityCamera(player);
//        Camera camera = new KeyboardCamera(new Vector3f(25, 10, 70));
//        Camera camera = new FPVCamera(new Vector3f(25, 10, 70));
//        Camera camera = new EntityCamera(player, new Vector3f(25, 10, 70));
//        Camera camera = new TestCamera(player);

        // Main loop

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
                MasterRenderer.enableFillPolygonMode();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                MasterRenderer.enableLinePolygonMode();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
                MasterRenderer.enablePointPolygonMode();
            }

            // Keyboard / FPV
//            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
//                camera.move();
//            } else {
//                player.move();
//            }

            // Movement of player and camera
            player.move(terrainsManager.getTerrain(player.getPosition().x, player.getPosition().z));
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

            DisplayManager.updateDisplay();
        }

        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}