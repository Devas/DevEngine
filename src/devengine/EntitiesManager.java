package devengine;

import entities.Entity;
import entities.Player;
import loaders.Loader;
import loaders.OBJLoader;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class allows to create and manage all entities.
 * This class uses TerrainsManager class to initialize new Entity objects with proper position on the Terrain objects.
 */
public class EntitiesManager {

    private final Loader loader;
    private final TerrainsManager terrainsManager;
    private final OBJLoader objLoader;
    private List<Entity> entities = new ArrayList<>(); // TODO HashMap <name, entity> ?
    private Player playerEntity;
    private Random random = new Random();

    public EntitiesManager(Loader loader, OBJLoader objLoader, TerrainsManager terrainsManager) {
        this.loader = loader;
        this.objLoader = objLoader;
        this.terrainsManager = terrainsManager;
        createEntities();
        createPlayerEntity();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayerEntity() {
        return playerEntity;
    }

//    private void createEntities() {
////        Entity entity = new Entity(dragonStaticModel, new Vector3f(0, 0, -20), 0, 0, 0, 1);
//
//        TexturedModel dragonStaticModel = new TexturedModel(objLoader.loadObjModel("dragon"), new ModelTexture(loader.loadTexture("stone_512")));
//        objLoader.printLastLoadInfo();
//        dragonStaticModel.getTexture().setShineDamper(1000);
//        dragonStaticModel.getTexture().setReflectivity(0.5f);
//        for (int i = 0; i < 10; i++) {
//            float x = random.nextFloat() * 100 - 50;
//            float y = random.nextFloat() * 100 - 50;
//            float z = random.nextFloat() * -300;
//            entities.add(new Entity("dragon", dragonStaticModel, new Vector3f(x, y, z), new Vector3f(random.nextFloat() * 180f, random.nextFloat() * 180f, 0f), 1f));
//        }
//
//        TexturedModel stallStaticModel = new TexturedModel(objLoader.loadObjModel("stall"), new ModelTexture(loader.loadTexture("stall")));
//        objLoader.printLastLoadInfo();
//        for (int i = 0; i < 20; i++) {
//            float x = random.nextFloat() * 110 - 60;
//            float y = random.nextFloat() * 110 - 60;
//            float z = random.nextFloat() * -320;
//            entities.add(new Entity("stall", stallStaticModel, new Vector3f(x, y, z), new Vector3f(random.nextFloat() * 180f, random.nextFloat() * 180f, 0f), 1f));
//        }
//
//        TexturedModel boxStaticModel = new TexturedModel(objLoader.loadObjModel("box"), new ModelTexture(loader.loadTexture("stone_512")));
//        objLoader.printLastLoadInfo();
//        for (int i = 0; i < 20; i++) {
//            float x = random.nextFloat() * 120 - 70;
//            float y = random.nextFloat() * 120 - 70;
//            float z = random.nextFloat() * -340;
//            entities.add(new Entity("box", boxStaticModel, new Vector3f(x, y, z), new Vector3f(random.nextFloat() * 180f, random.nextFloat() * 180f, 0f), 1f));
//        }
//
//        ModelData data = OBJFileLoader.loadOBJ("pack/Tree1");
//        RawModel tree1 = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
//        TexturedModel tree1TexturedModel = new TexturedModel(tree1, new ModelTexture(loader.loadTexture("pack/tree")));
//        Entity tree1Entity = new Entity("tree1", tree1TexturedModel, new Vector3f(0, 0, 0), new Vector3f(0f, 0f, 0f), 1f);
//    }

    private void createEntities() {
        float size1 = random.nextFloat() * 0.1f + 0.6f;
        float size2 = random.nextFloat() + 3.6f;

        TexturedModel tree = new TexturedModel(objLoader.loadObjModel("pack/tree"), new ModelTexture(loader.loadTexture("pack/tree"))); // TODO add ModelTextureLoader(Loader loader) f(){return new ModelTexture(loader.loadTexture)}
        scatterOnAllTerrains(tree, size2, 2000);

        TexturedModel grass = new TexturedModel(objLoader.loadObjModel("pack/grassModel"), new ModelTexture(loader.loadTexture("pack/grassTexture")));
        grass.getRawModel().setFaceCulled(false);
        grass.getTexture().setUseFakeLighting(true);
        scatterOnAllTerrains(grass, 1, 2000);

        TexturedModel flower = new TexturedModel(objLoader.loadObjModel("pack/grassModel"), new ModelTexture(loader.loadTexture("pack/flower")));
        flower.getRawModel().setFaceCulled(false);
        flower.getTexture().setUseFakeLighting(true);
        scatterOnAllTerrains(flower, 1, 2000);

//        TexturedModel fern = new TexturedModel(objLoader.loadObjModel("pack/fern"), new ModelTexture(loader.loadTexture("pack/fern")));
        TexturedModel fern = new TexturedModel(objLoader.loadObjModel("pack/fern"), new ModelTexture(loader.loadTexture("pack/fern_atlas")));
        fern.getRawModel().setFaceCulled(false);
        fern.getTexture().setTextureAtlasSize(2); // add this line if using atlas texture
        scatterOnAllTerrains(fern, 1, 2000);
    }

    private void scatterOnAllTerrains(TexturedModel texturedModel, float size, int numberOfEntities) {
        for (Terrain terrain : terrainsManager.getTerrains()) {
            for (int i = 0; i < numberOfEntities; i++) {
                float x = terrain.getX() + (random.nextFloat() * Terrain.getSIZE());
                float z = terrain.getZ() + (random.nextFloat() * Terrain.getSIZE());
                float y = terrain.getHeightOfTerrain(x, z);
                float angle = random.nextFloat() * 360;
                entities.add(new Entity("", texturedModel, new Vector3f(x, y, z), new Vector3f(0, angle, 0), size, random.nextInt(4)));
            }
        }
    }

    private void createPlayerEntity() {
        TexturedModel player = new TexturedModel(objLoader.loadObjModel("pack/player"), new ModelTexture(loader.loadTexture("pack/player")));
        playerEntity = new Player("player", player, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
    }

}
