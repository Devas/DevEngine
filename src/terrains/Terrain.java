package terrains;

import loaders.Loader;
import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    // The world is made up of a square grid where each square (Terrain) has edges of size Terrain.SIZE
    private static final float SIZE = 800f;
    private static final float MAX_HEIGHT = 40f;
    private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

    // World position of the Terrain mesh
    private float x;
    private float z;

    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    // Array that stores all heights retrieved from Image. It's used in collision detection
    private float[][] heights;

    /**
     * We want the world to be made up of a grid of Terrain meshes.
     * Each Terrain is one square in the grid, and so each terrain has its own grid coordinates.
     * These gridX and gridZ ints are the coordinates of the terrain in the grid.
     * Obviously these gridX and gridZ coordinates will always be integers(we can't have a gridX value of 2.5
     * for example, because then the terrain would start half way along a grid square).
     * Using the gridX and gridZ coordinate and knowing the SIZE of each grid square, we can calculate
     * the actual world position of the terrain.
     */
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightmap) {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.model = generateTerrain(loader, heightmap); // TODO if heightmap null don't use heightmap but generate flat terrain or use flat heightmap
    }

    public static float getSIZE() {
        return SIZE;
    }

    /**
     * @return World X position of the Terrain mesh
     */
    public float getX() {
        return x;
    }

    /**
     * @return World Z position of the Terrain mesh
     */
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

    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - x;
        float terrainZ = worldZ - z;
        float gridSquareSize = SIZE / ((float) (heights.length - 1));
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX < 0 || gridZ < 0 || gridX >= heights.length - 1 || gridZ >= heights.length - 1) { // TODO -1
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize; // range 0 .. 1
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize; // range 0 .. 1
        float result;
        if (xCoord <= (1 - zCoord)) {
            result = Maths.barycentricCoordsOnTriangle(
                    new Vector3f(0, heights[gridX][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        } else {
            result = Maths.barycentricCoordsOnTriangle(
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        }
        return result;
    }

    private RawModel generateTerrain(Loader loader, String heightmap) {

        // Origin of java Image is at top left corner and x increases to the right side, and y increased downside.
        // To change it to the conventional coordinate system, change y value: y2 = image.height - y
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/heightmaps/" + heightmap));
//            image = ImageIO.read(Terrain.class.getClassLoader().getResourceAsStream(heightmap)); // TODO simplify
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the number of vertices along each side of the terrain based on the size (x or y) of the image
        // TODO add manual setting in case the image is big but we don't want to have high-poly terrain
        int VERTEX_COUNT = image.getHeight(); // get size of the image e.g. 1024 results in 1024 vertices along one side of the Terrain
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {

                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
//                float height = 0; // TODO if heightmap null don't use heightmap but generate flat terrain
                float height = getHeightFromImage(j, i, image); // get height for pixel (j,i)
                heights[j][i] = height; // store also in the array to use in collision detection
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;

                Vector3f normal = calculateNormal(j, i, image, VERTEX_COUNT);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

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

    private float getHeightFromImage(int x, int y, BufferedImage image) {
        // Coordinates of Image starts at (0,0) so we use >=
        // If we are out of bounds return height equal to 0
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            // System.out.println("Zero");
            return 0;
        }
        float height = image.getRGB(x, y); // range -MAX_PIXEL_COLOUR .. 0
        height += MAX_PIXEL_COLOUR / 2f; // range -MAX_PIXEL_COLOUR/2 .. MAX_PIXEL_COLOUR/2
        height /= MAX_PIXEL_COLOUR / 2f; // range -1 .. 1
        height *= MAX_HEIGHT; // range -MAX_HEIGHT .. MAX_HEIGHT
//        float height = Maths.lerp(-MAX_PIXEL_COLOUR, MAX_PIXEL_COLOUR, image.getRGB(x, y));
//        height = Maths.lerp(-MAX_HEIGHT, MAX_HEIGHT, height);
        return height;
    }

    @Deprecated
    private Vector3f calculateNormal(int x, int z, BufferedImage image) { // TODO optimize to calculate every vertex only once
        // TODO If there is no heightmap return old normal
        if (image == null) {
            return new Vector3f(0, 1, 0);
        }

        // calculate heights of 4 neighboring vertices
        final int OFFSET = 1; // arbitrary small OFFSET, best is 1, OFFSET >= 2 can result in too hard normals edges
        assert OFFSET > 0 : "OFFSET must be greater than zero, but OFFSET = " + OFFSET;
        float heightL = getHeightFromImage(x - OFFSET, z, image);
        float heightR = getHeightFromImage(x + OFFSET, z, image);
        float heightU = getHeightFromImage(x, z + OFFSET, image);
        float heightD = getHeightFromImage(x, z - OFFSET, image);

        // deduce terrain normal
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    // TODO works better than previous - solves seams problem
    private Vector3f calculateNormal(int x, int z, BufferedImage image, int VERTEX_COUNT) {

        float heightL;
        float heightR;
        float heightD;
        float heightU;

        if (x == 0) {
            heightL = getHeightFromImage(x - 1 + VERTEX_COUNT, z, image);
            heightR = getHeightFromImage(x + 1, z, image);
        } else if (x == 1) {
            heightL = getHeightFromImage(x - 1 + VERTEX_COUNT - 1, z, image);
            heightR = getHeightFromImage(x + 1, z, image);
        } else if (x == VERTEX_COUNT) {
            heightR = getHeightFromImage(x + 1 - VERTEX_COUNT, z, image);
            heightL = getHeightFromImage(x - 1, z, image);
        } else if (x == VERTEX_COUNT - 1) {
            heightR = getHeightFromImage(x + 1 - VERTEX_COUNT + 1, z, image);
            heightL = getHeightFromImage(x - 1, z, image);
        } else {
            heightL = getHeightFromImage(x - 1, z, image);
            heightR = getHeightFromImage(x + 1, z, image);
        }

        if (z == 0) {
            heightD = getHeightFromImage(x, z - 1 + VERTEX_COUNT, image);
            heightU = getHeightFromImage(x, z + 1, image);
        } else if (z == 1) {
            heightD = getHeightFromImage(x, z - 1 + VERTEX_COUNT - 1, image);
            heightU = getHeightFromImage(x, z + 1, image);
        } else if (z == VERTEX_COUNT) {
            heightD = getHeightFromImage(x, z - 1, image);
            heightU = getHeightFromImage(x, z + 1 - VERTEX_COUNT, image);
        } else if (z == VERTEX_COUNT - 1) {
            heightD = getHeightFromImage(x, z - 1, image);
            heightU = getHeightFromImage(x, z + 1 - VERTEX_COUNT + 1, image);
        } else {
            heightD = getHeightFromImage(x, z - 1, image);
            heightU = getHeightFromImage(x, z + 1, image);
        }

        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    // TODO to avoid bright / dark edges
    // https://www.google.pl/search?client=opera&q=terrain+normals+on+edges&sourceid=opera&ie=UTF-8&oe=UTF-8
    // http://answers.unity3d.com/questions/882210/correcting-normals-along-chunk-edges.html
    private static void RecalculateNormalsOnEdges() {

    }

}
