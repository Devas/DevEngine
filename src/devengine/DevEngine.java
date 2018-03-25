package devengine;

import display.DisplayManager;
import entities.Entity;
import entities.Player;
import helpers.cameras.Camera;
import helpers.cameras.EntityCamera;
import lights.Light;
import lights.LightColour;
import loaders.Loader;
import loaders.OBJLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.Log;
import renderers.GuiRenderer;
import renderers.MasterRenderer;
import terrains.Terrain;
import terrains.TerrainLoader;

class DevEngine {

    private final Loader loader;
    private final OBJLoader objLoader;
    private final TerrainLoader terrainLoader;
    private final TerrainsManager terrainsManager;
    private final EntitiesManager entitiesManager;
    private final GuisManager guisManager;
    private final MasterRenderer masterRenderer;
    private final GuiRenderer guiRenderer;

    DevEngine() {
        DisplayManager.createDisplay();
        loader = new Loader();
        objLoader = new OBJLoader(loader);
        terrainLoader = new TerrainLoader(loader);
        terrainsManager = new TerrainsManager(loader, terrainLoader);
        entitiesManager = new EntitiesManager(loader, objLoader, terrainsManager);
        guisManager = new GuisManager(loader);
        masterRenderer = new MasterRenderer();
        guiRenderer = new GuiRenderer(loader);
        Log.setVerbose(false);
    }

    /**
     * Here is defined main loop.
     */
    void run() {

        Light light = new Light(new Vector3f(3000, 2000, -2000), LightColour.WHITE.getColour()); // 20000,40000,20000 // TODO LightManager
        Player player = entitiesManager.getPlayerEntity();
        Camera camera = new EntityCamera(player);
//        Camera cameras = new KeyboardCamera(new Vector3f(25, 10, 70));
//        Camera cameras = new FPVCamera(new Vector3f(25, 10, 70));
//        Camera cameras = new EntityCamera(player, new Vector3f(25, 10, 70));
//        Camera cameras = new TestCamera(player);

        boolean paused = false;
        while (!Display.isCloseRequested()) {

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) break;

            if (Keyboard.isKeyDown(Keyboard.KEY_F11)) DisplayManager.toggleFullscreen();
            if (Keyboard.isKeyDown(Keyboard.KEY_F10)) DisplayManager.toggleVSync();

            if (Keyboard.isKeyDown(Keyboard.KEY_P)) paused = !paused;

            if (Keyboard.isKeyDown(Keyboard.KEY_I)) camera.printCameraInfo();

            if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
                masterRenderer.enableFillPolygonMode();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
                masterRenderer.enableLinePolygonMode();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
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