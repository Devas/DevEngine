package terrains;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import utils.MathUtil;

// TODO refactor to terrain loader / terrain generator
/**
 * Stores information about terrain. During construction uses TerrainLoader to generate terrain.
 */
public class Terrain {

    private final float size;
    private float x; // World X position of the Terrain mesh
    private float y = 0; // Additional World Y position of the Terrain mesh used only in getPosition() to get nice vector
    private float z; // World Z position of the Terrain mesh
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap; // texture used to blend all textures from texturePack

    private RawModel model;
    private float[][] heights; // Array that stores all heights retrieved from Image. It's used in collision detection

    /**
     * We want the world to be made up of a grid of Terrain meshes.
     * Each Terrain is one square in the grid, and so each terrain has its own grid coordinates.
     * These gridX and gridZ ints are the coordinates of the terrain in the grid.
     * Obviously these gridX and gridZ coordinates will always be integers (we can't have a gridX value of 2.5
     * for example, because then the terrain would start half way along a grid square).
     * Using the gridX and gridZ coordinate and knowing the SIZE of each grid square, we can calculate
     * the actual world position of the terrain.
     *
     * @param terrainLoader loads Terrain into VAO
     * @param gridX X coordinate of the Terrain in the grid
     * @param gridZ Z coordinate of the Terrain in the grid
     */
    // TODO get RawModel instead of TerrainLoader
    // TODO if heightmap null don't use heightmap but generate flat terrain or use flat heightmap
    public Terrain(TerrainLoader terrainLoader, int gridX, int gridZ, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightmap) {
        this.size = TerrainLoader.SIZE;
        this.x = gridX * size;
        this.z = gridZ * size;
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        // TODO can these 2 be merged in one / new object TerrainModel?
        this.model = terrainLoader.loadTerrainFromHeightmap(heightmap);
        this.heights = terrainLoader.getHeights();
    }

    public float getSize() {
        return size;
    }

    /**
     * @return World X position of the Terrain mesh
     */
    public float getX() {
        return x;
    }

    /**
     * @return World Y position of the Terrain mesh
     */
    public float getY() {
        return y;
    }

    /**
     * @return World Z position of the Terrain mesh
     */
    public float getZ() {
        return z;
    }

    public Vector3f getPosition() {
        return new Vector3f(x, y, z);
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMapTexture() {
        return blendMap;
    }

    /**
     * Get height of the Terrain using barycentric interpolation.
     *
     * @param worldX X position in world-coordinates
     * @param worldZ Z position in world-coordinates
     * @return Height of the Terrain
     */
    public float getHeightOfTerrain(float worldX, float worldZ) {
        // Convert the world-coordinates of the object into the object's position in relation to the Terrain
        // So now for any Terrain these will be in range 0 .. SEIZE
        float terrainX = worldX - x; // range 0 .. SEIZE
        float terrainZ = worldZ - z; // range 0 .. SEIZE

        int gridSquaresCountAlongSide = heights.length - 1; // there is always 1 less than the number of vertices
        float gridSquareSize = size / ((float) gridSquaresCountAlongSide);

        // Calculate coordinates indicating on which grid-square the object is standing inside the Terrain
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX < 0 || gridZ < 0 || gridX >= heights.length - 1 || gridZ >= heights.length - 1) {
            return 0;
        }

        // Calculate normalized coordinates indicating where the object is standing inside the grid-square
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize; // range 0 .. 1
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize; // range 0 .. 1

        // Every grid-square consists of 2 triangles, so detect the triangle and interpolate to get height
        float result;
        if (xCoord <= (1 - zCoord)) {
            result = MathUtil.barycentricCoordsOnTriangle(
                    new Vector3f(0, heights[gridX][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        } else {
            result = MathUtil.barycentricCoordsOnTriangle(
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        }
        return result;
//        TODO Simplification of the choosing of triangle in the end of getHeightOfTerrain()
//        int var1 = xCoord <= (1 - zCoord) ? 0 : 1;
//        return Maths.barycentricCoordsOnTriangle(new Vector3f(var1, heights[gridX + var1][gridZ], 0),
//                new Vector3f(1, heights[gridX + 1][gridZ + var1], var1),
//                new Vector3f(0, heights[gridX][gridZ + 1], 1),
//                new Vector2f(xCoord, zCoord));﻿
    }
}
