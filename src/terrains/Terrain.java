package terrains;

import models.RawModel;
import loaders.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class Terrain {

    // The world is made up of a square grid where each square (Terrain) has edges of size Terrain.SIZE
    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128; // along each side of the terrain

    // World position of the Terrain mesh
    private float x;
    private float z;

    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    /**
     * We want the world to be made up of a grid of Terrain meshes.
     * Each Terrain is one square in the grid, and so each terrain has its own grid coordinates.
     * These gridX and gridZ ints are the coordinates of the terrain in the grid.
     * Obviously these gridX and gridZ coordinates will always be integers(we can't have a gridX value of 2.5
     * for example, because then the terrain would start half way along a grid square).
     * Using the gridX and gridZ coordinate and knowing the SIZE of each grid square, we can calculate
     * the actual world position of the terrain.
     */
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.model = generateTerrain(loader);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
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

    private RawModel generateTerrain(Loader loader) {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;
                textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

}
