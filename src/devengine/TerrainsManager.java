package devengine;

import entities.Entity;
import loaders.TextureLoader;
import terrains.Terrain;
import terrains.TerrainLoader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows to create and manage all terrains.
 */
public class TerrainsManager {

    private final int TERRAINS_GRID_X_SIZE = 2; // Max number of possible Terrain objects along X axis
    private final int TERRAINS_GRID_Z_SIZE = 2; // Max number of possible Terrain objects along Z axis

    private final TextureLoader textureLoader;
    private final TerrainLoader terrainLoader;
    private List<Terrain> terrainsList = new ArrayList<>();
    private Terrain[][] terrainsGrid = new Terrain[TERRAINS_GRID_X_SIZE][TERRAINS_GRID_Z_SIZE];

    public TerrainsManager(TextureLoader textureLoader, TerrainLoader terrainLoader) {
        this.textureLoader = textureLoader;
        this.terrainLoader = terrainLoader;
        createTerrains();
    }

    /**
     * Get the list of all Terrain objects. It is useful in loops.
     *
     * @return List of all terrains
     */
    public List<Terrain> getTerrains() {
        if (terrainsList.isEmpty()) {
            throw new RuntimeException("Terrains not created");
        }
        return terrainsList;
    }

    /**
     * Get the Terrain on which the Entity object stands.
     * If the Entity is out of bound of any Terrain then this methods returns null.
     *
     * @param entity Entity object
     * @return Terrain on which the Entity object stands or null if the Entity's position is out of bound of any Terrain
     */
    public Terrain getTerrain(Entity entity) {
        return getTerrainCommon(entity.getPosition().x, entity.getPosition().z);
    }

    /**
     * Get the Terrain on which the object stands.
     * If the (worldX, worldZ) position is out of bound of any Terrain then this methods returns null.
     *
     * @param worldX World X position of the object
     * @param worldZ World Z position of the object
     * @return Terrain on which the object stands or null if the position is out of bound of any Terrain
     */
    public Terrain getTerrain(float worldX, float worldZ) {
        return getTerrainCommon(worldX, worldZ);
    }

    /**
     * Common functionality for getTerrain(Entity entity) and getTerrain(float worldX, float worldZ).
     */
    private Terrain getTerrainCommon(float worldX, float worldZ) {
        if (terrainsList.isEmpty()) {
            throw new RuntimeException("Terrains not created");
        }
        int gridX = (int) (worldX / TerrainLoader.SIZE); // TODO move to Terrain - belongsToTerrain()? in order to remove SIZE dependency
        int gridZ = (int) (worldZ / TerrainLoader.SIZE);
        if (gridX < 0 || gridZ < 0 || gridX >= TERRAINS_GRID_X_SIZE || gridZ >= TERRAINS_GRID_Z_SIZE) {
            return null;
        } else {
            return terrainsGrid[gridX][gridZ];
        }
    }

    private void createTerrains() {
        TerrainTexture backgroundTexture = new TerrainTexture(textureLoader.load("terrain/grass_natural_2048")); // "terrain/anime_grass_512"
        TerrainTexture rTexture = new TerrainTexture(textureLoader.load("terrain/mud_dry_256"));
        TerrainTexture gTexture = new TerrainTexture(textureLoader.load("terrain/grass_flowers_256"));
        TerrainTexture bTexture = new TerrainTexture(textureLoader.load("terrain/paving_256"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(textureLoader.load("terrain/blend_map_1024"));

        Terrain terrain00 = new Terrain(terrainLoader, 0, 0, texturePack, blendMap, "heightmap_1_256.png");
        Terrain terrain10 = new Terrain(terrainLoader, 1, 0, texturePack, blendMap, "heightmap_1_256.png");
//        Terrain terrain00 = new Terrain(0, 0, texturePack, blendMap, "heightmap_2_1024.bmp");
//        Terrain terrain10 = new Terrain(1, 0, texturePack, blendMap, "heightmap_2_1024.bmp");
        terrainsList.add(terrain00);
        terrainsList.add(terrain10);
        terrainsGrid[0][0] = terrain00;
        terrainsGrid[1][0] = terrain10;


//        Terrain terrain01 = new Terrain(0, 1, texturePack, blendMap, "heightmap_1_256.png"); // heightmap_2_1024.bmp
//        Terrain terrain11 = new Terrain(1, 1, texturePack, blendMap, "heightmap_1_256.png");
//        terrainsList.add(terrain01);
//        terrainsList.add(terrain11);
//        terrainsGrid[0][1] = terrain01;
//        terrainsGrid[1][1] = terrain11;
    }

}
