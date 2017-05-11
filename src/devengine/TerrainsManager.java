package devengine;

import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;

import static loaders.Loader.loader;

public class TerrainsManager {

    private final int TERRAINS_GRID_SIZE = 2;

    private List<Terrain> terrainsList = new ArrayList<>();
    private Terrain[][] terrainsGrid = new Terrain[2][2]; // First [] is for X axis. Second [] is for Z axis.

    public TerrainsManager() {
        createTerrains();
    }

    /**
     * Get list of all terrains which is useful in loops.
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
     * Get Terrain on which the object stands
     *
     * @param worldX World X position of the object
     * @param worldZ World Z position of the object
     * @return Terrain on which the object stands
     */
    public Terrain getTerrain(float worldX, float worldZ) {
        if (terrainsList.isEmpty()) {
            throw new RuntimeException("Terrains not created");
        }
        int gridX = (int) (worldX / Terrain.getSIZE());
        int gridZ = (int) (worldZ / Terrain.getSIZE());
        return terrainsGrid[gridX][gridZ];
    }

    private void createTerrains() {
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/grass_natural_2048"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud_dry_256"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/grass_flowers_256"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/paving_256"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrain/blend_map_1024"));

        Terrain terrain00 = new Terrain(0, 0, texturePack, blendMap, "heightmap_1_256.png"); // heightmap_2_1024.bmp
        Terrain terrain10 = new Terrain(1, 0, texturePack, blendMap, "heightmap_1_256.png");
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
