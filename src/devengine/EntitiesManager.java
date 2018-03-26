package devengine;

import entities.Entity;
import entities.Player;
import loaders.TextureLoader;
import loaders.obj.OBJLoader;
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

    private final TextureLoader textureLoader;
    private final OBJLoader objLoader;
    private final TerrainsManager terrainsManager; // TODO can we remove this dependency just by passing list of Terrain to scatterOnAllTerrains()
    private List<Entity> entities = new ArrayList<>(); // TODO HashMap <name, entity> ?
    private Player playerEntity;
    private Random random = new Random();

    public EntitiesManager(TextureLoader textureLoader, OBJLoader objLoader, TerrainsManager terrainsManager) {
        this.textureLoader = textureLoader;
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
//        TexturedModel dragonStaticModel = new TexturedModel(objLoader.loadModel("dragon"), new ModelTexture(textureLoader.load("stone_512")));
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
//        TexturedModel stallStaticModel = new TexturedModel(objLoader.loadModel("stall"), new ModelTexture(textureLoader.load("stall")));
//        objLoader.printLastLoadInfo();
//        for (int i = 0; i < 20; i++) {
//            float x = random.nextFloat() * 110 - 60;
//            float y = random.nextFloat() * 110 - 60;
//            float z = random.nextFloat() * -320;
//            entities.add(new Entity("stall", stallStaticModel, new Vector3f(x, y, z), new Vector3f(random.nextFloat() * 180f, random.nextFloat() * 180f, 0f), 1f));
//        }
//
//        TexturedModel boxStaticModel = new TexturedModel(objLoader.loadModel("box"), new ModelTexture(textureLoader.load("stone_512")));
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
//        TexturedModel tree1TexturedModel = new TexturedModel(tree1, new ModelTexture(textureLoader.load("pack/tree")));
//        Entity tree1Entity = new Entity("tree1", tree1TexturedModel, new Vector3f(0, 0, 0), new Vector3f(0f, 0f, 0f), 1f);
//    }

    private void createEntities() {
        float size1 = random.nextFloat() * 0.1f + 0.6f;
        float size2 = random.nextFloat() + 3.6f;

        TexturedModel tree = new TexturedModel(objLoader.loadModel("pack/tree"), new ModelTexture(textureLoader.load("pack/tree"))); // TODO add ModelTextureLoader(Loader loader) f(){return new ModelTexture(loader.load)}
        scatterOnAllTerrains(tree, size2, 2000);

        TexturedModel grass = new TexturedModel(objLoader.loadModel("pack/grassModel"), new ModelTexture(textureLoader.load("pack/grassTexture")));
        grass.getRawModel().setFaceCulled(false);
        grass.getTexture().setUseFakeLighting(true);
        scatterOnAllTerrains(grass, 1, 2000);

        TexturedModel flower = new TexturedModel(objLoader.loadModel("pack/grassModel"), new ModelTexture(textureLoader.load("pack/flower")));
        flower.getRawModel().setFaceCulled(false);
        flower.getTexture().setUseFakeLighting(true);
        scatterOnAllTerrains(flower, 1, 2000);

//        TexturedModel fern = new TexturedModel(objLoader.loadModel("pack/fern"), new ModelTexture(textureLoader.load("pack/fern")));
        TexturedModel fern = new TexturedModel(objLoader.loadModel("pack/fern"), new ModelTexture(textureLoader.load("pack/fern_atlas")));
        fern.getRawModel().setFaceCulled(false);
        fern.getTexture().setTextureAtlasSize(2); // add this line if using atlas texture
        scatterOnAllTerrains(fern, 1, 2000);
    }

    private void scatterOnAllTerrains(TexturedModel texturedModel, float size, int numberOfEntities) {
        for (Terrain terrain : terrainsManager.getTerrains()) {
            for (int i = 0; i < numberOfEntities; i++) {
                float x = terrain.getX() + (random.nextFloat() * terrain.getSize());
                float z = terrain.getZ() + (random.nextFloat() * terrain.getSize());
                float y = terrain.getHeightOfTerrain(x, z);
                float angle = random.nextFloat() * 360;
                entities.add(new Entity("", texturedModel, new Vector3f(x, y, z), new Vector3f(0, angle, 0), size, random.nextInt(4)));
            }
        }
    }

    private void createPlayerEntity() {
        TexturedModel player = new TexturedModel(objLoader.loadModel("pack/player"), new ModelTexture(textureLoader.load("pack/player")));
        playerEntity = new Player("player", player, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
    }
}
