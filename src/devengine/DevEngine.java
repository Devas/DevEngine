package devengine;

import entities.Entity;
import entities.Player;
import helpers.Light;
import helpers.camera.Camera;
import helpers.camera.EntityCamera;
import loaders.Loader;
import loaders.OBJLoader;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.Log;
import renderers.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DevEngine {

    private static Random random = new Random();

    static {
        Log.setVerbose(false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Main method with main engine loop
    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

//        // New loader
//        ModelData data = OBJFileLoader.loadOBJ("pack/Tree1");
//        RawModel tree1 = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
//        TexturedModel tree1TexturedModel = new TexturedModel(tree1, new ModelTexture(loader.loadTexture("pack/tree")));
//        Entity tree1Entity = new Entity("tree1", tree1TexturedModel, new Vector3f(0, 0, 0), 0f, 0f, 0f, 1f);

//        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);

        Light light = new Light(new Vector3f(3000, 2000, -2000), Light.Colour.WHITE.getColour()); // 20000,40000,20000
        MasterRenderer masterRenderer = new MasterRenderer();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/grass_natural_2048"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud_dry_256"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/grass_flowers_256"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/paving_256"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrain/blend_map_1024"));

        List<Terrain> terrains = new ArrayList<>();
        Terrain terrain1 = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap_1_256.png"); // heightmap_2_1024.bmp
        Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap, "heightmap_1_256.png");
        terrains.add(terrain1);
        terrains.add(terrain2);

//        Entity entity = new Entity(dragonStaticModel, new Vector3f(0, 0, -20), 0, 0, 0, 1);

        List<Entity> entitiesList = new ArrayList<>();

//        TexturedModel dragonStaticModel = new TexturedModel(OBJLoader.loadObjModel("dragon", loader), new ModelTexture(loader.loadTexture("stone_512")));
//        OBJLoader.printLastLoadInfo();
//        dragonStaticModel.getTexture().setShineDamper(1000);
//        dragonStaticModel.getTexture().setReflectivity(0.5f);
//        for (int i = 0; i < 10; i++) {
//            float x = random.nextFloat() * 100 - 50;
//            float y = random.nextFloat() * 100 - 50;
//            float z = random.nextFloat() * -300;
//            entitiesList.add(new Entity("dragon", dragonStaticModel, new Vector3f(x, y, z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
//        }
//
//        TexturedModel stallStaticModel = new TexturedModel(OBJLoader.loadObjModel("stall", loader), new ModelTexture(loader.loadTexture("stall")));
//        OBJLoader.printLastLoadInfo();
//        for (int i = 0; i < 20; i++) {
//            float x = random.nextFloat() * 110 - 60;
//            float y = random.nextFloat() * 110 - 60;
//            float z = random.nextFloat() * -320;
//            entitiesList.add(new Entity("stall", stallStaticModel, new Vector3f(x, y, z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
//        }
//
//        TexturedModel boxStaticModel = new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("stone_512")));
//        OBJLoader.printLastLoadInfo();
//        for (int i = 0; i < 20; i++) {
//            float x = random.nextFloat() * 120 - 70;
//            float y = random.nextFloat() * 120 - 70;
//            float z = random.nextFloat() * -340;
//            entitiesList.add(new Entity("box", boxStaticModel, new Vector3f(x, y, z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
//        }

        TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("pack/tree", loader), new ModelTexture(loader.loadTexture("pack/tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("pack/grassModel", loader), new ModelTexture(loader.loadTexture("pack/grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("pack/grassModel", loader), new ModelTexture(loader.loadTexture("pack/flower")));
        flower.getTexture().setHasTransparency(true);
        flower.getTexture().setUseFakeLighting(true);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("pack/fern", loader), new ModelTexture(loader.loadTexture("pack/fern")));
        fern.getTexture().setHasTransparency(true);

        for (Terrain terrain : terrains) {
            int numberOfEntities = 2000;
            for (int i = 0; i < numberOfEntities; i++) {
                float x = terrain.getX() + (random.nextFloat() * terrain.getSIZE());
                float z = terrain.getZ() + (random.nextFloat() * terrain.getSIZE());
                float y = terrain.getHeightOfTerrain(x, z);
                float angle = random.nextFloat() * 360;
                float size1 = random.nextFloat() * 0.1f + 0.6f;
                float size2 = random.nextFloat() + 3.6f;
                entitiesList.add(new Entity("tree", tree, new Vector3f(x, y, z), new Vector3f(0, angle, 0), size2));
                entitiesList.add(new Entity("grass", grass, new Vector3f(x, y, z), new Vector3f(0, angle, 0), 1));
                entitiesList.add(new Entity("flower", flower, new Vector3f(x, y, z), new Vector3f(0, angle, 0), 1));
                entitiesList.add(new Entity("fern", fern, new Vector3f(x, y, z), new Vector3f(0, angle, 0), 0.6f));
            }
        }

//        entitiesList.add(tree1Entity);

        TexturedModel player = new TexturedModel(OBJLoader.loadObjModel("pack/player", loader), new ModelTexture(loader.loadTexture("pack/player")));
        Player playerEntity = new Player("player", player, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);

//        Camera camera = new KeyboardCamera(new Vector3f(25, 10, 70));
//        Camera camera = new FPVCamera(new Vector3f(25, 10, 70));
//        Camera camera = new EntityCamera(playerEntity, new Vector3f(25, 10, 70));
        Camera camera = new EntityCamera(playerEntity);
//        Camera camera = new TestCamera(playerEntity);

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
//                playerEntity.move();
//            }

            // ThirdPerson
            playerEntity.move(terrain1); // TODO detect on which terrain player is standing
            camera.move();

            // Assigns all entities to batches
            for (Entity entity : entitiesList) {
                masterRenderer.processEntity(entity);
//                if (!paused) {
//                    entity.increaseRotation(0, 1, 0);
//                } else {
//                    entity.increaseRotation(0, 0, 0);
//                }
            }

            for (Terrain terrain : terrains) {
                masterRenderer.processTerrain(terrain);
            }

            masterRenderer.processEntity(playerEntity);

            masterRenderer.render(light, camera);

            DisplayManager.updateDisplay();
        }

        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}