package loaders;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads data to OpenGl.
 */
public class Loader {

    private static final float MIPMAP_FACTOR = -0.5f; // the bigger number the less resolution of rendered texture

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    /**
     * Make sure textures will be tiled properly by repeating
     */
    public Loader() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    }

    /**
     * Used for GUI.
     *
     * @param positions array of vertex positions, 2 floats per vertex
     */
    public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInVertexAttributeArray(0, 2, positions);      // Store 2-value-positions in attribute 0
        unbindVAO();
        return new RawModel(vaoID, positions.length / 2);
    }

    /**
     * Used for models.
     *
     * @param positions array of vertex positions, 3 floats per vertex
     * @param textureCoords array of texture coordinates, 2 floats per coordinate
     * @param normals array of normals positions, 3 floats per normal
     * @param indices ints for index buffer to specify order of vertices
     * @return RawModel loaded to VAO
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) { // TODO rename textureCoords to uvCoords
        int vaoID = createVAO();
        bindIndexBuffer(indices);
        storeDataInVertexAttributeArray(0, 3, positions);      // Store 3-value-vertex-positions in attribute 0 (vertex buffer)
        storeDataInVertexAttributeArray(1, 2, textureCoords);  // Store 2-values-texture-coords in attribute 1 (uv buffer)
        storeDataInVertexAttributeArray(2, 3, normals);        // Store 3-value-normals in attribute 2 (normals buffer)
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    /**
     * VAO (Vertex Array Object) consists of 16 (indexed from 0 to GL_MAX_VERTEX_ATTRIBS - 1) vertex attribute arrays
     * (vertex arrays, attribute arrays, attribute list, slots).
     * VAO does not store any data but stores references to the VBOs.
     * VAOs have the usual creation, destruction, and binding functions:
     * glGenVertexArrays, glDeleteVertexArrays, and glBindVertexArray.
     * A newly-created VAO has array access disabled for all attributes.
     * Array access is enabled by binding the VAO in question and calling: glEnableVertexAttribArray(GLuint index​).
     * There is a similar glDisableVertexAttribArray function to disable an enabled array.
     * <p/>
     * This method creates VAO, binds it (activates, enables) and returns id of the created VAO.
     *
     * @return id of the created VAO
     */
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    /**
     * Creates VBO for index buffer. Index buffer is essentially an array of pointers into the vertex buffer.
     * It allows to reorder the vertex data, and reuse existing data for multiple vertices.
     * Index buffer is stored within the VAO as additional slot. Does not has to be unbound.
     * GL_ELEMENT_ARRAY_BUFFER is used to indicate the buffer contains the indices of each element in the "other"
     * (GL_ARRAY_BUFFER) buffer.
     *
     * @param indices ints for index buffer to specify order of vertices
     */
    private void bindIndexBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = createIntBufferFromData(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * VBO (Vertex Buffer Object) is a buffer which is used to hold a vertex attribute array
     * (vertex array, attribute array, attribute list, slot in VAO).
     * <p/>
     * This method creates VBO, binds it (activates, enables), stores data into VBO,
     * puts VBO into active VAO, lastly unbinds VBO.
     * <p/>
     * More: glBindBuffer sets a global variable, then glVertexAttribPointer reads that global variable and stores it
     * in VAO. Changing that global variable after it's been read doesn't affect VAO.
     *
     * @param vertexAttributeArrayIndex index of vertex attribute array in VAO where data will be stored
     * @param coordinateSize length of vertex for example 3 for 3d vertices and 2 for 2d points for textures
     * @param data data to store
     */
    private void storeDataInVertexAttributeArray(int vertexAttributeArrayIndex, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = createFloatBufferFromData(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(vertexAttributeArrayIndex, coordinateSize, GL11.GL_FLOAT, false, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Unbinds bound VAO. glBindVertexArray(0) breaks the existing VAO binding.
     */
    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    /**
     * Creates FloatBuffer object from float data. FloatBuffer object is flipped and prepared for reading.
     *
     * @param data float data
     * @return flipped FloatBuffer object prepared for reading
     */
    FloatBuffer createFloatBufferFromData(float[] data) {
        return BufferUtils.createFloatBuffer(data.length)
                .put(data)
                .flip();
    }

    /**
     * Creates IntBuffer object from int data. IntBuffer object is flipped and prepared for reading.
     *
     * @param data int data
     * @return flipped IntBuffer object prepared for reading
     */
    IntBuffer createIntBufferFromData(int[] data) {
        return BufferUtils.createIntBuffer(data.length)
                .put(data)
                .flip();
    }

    /**
     * Loads texture from file using SlickUtil and returns OpenGL id of loaded texture.
     *
     * @param texturePath path to texture in res folder, uses PNG
     * @return id of loaded texture
     */
    // TODO extract texture and VAO loaders // TextureLoader?
    public int loadTexture(String texturePath) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + texturePath + ".png"));
            enableMipmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    private void enableMipmap() {
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, MIPMAP_FACTOR);
    }

    private int getMaxTextureSize() {
        return GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
    }

    /**
     * Delete all VAOs, VBOs, textures.
     */
    public void cleanUp() {
        for (int vao : vaos) GL30.glDeleteVertexArrays(vao);
        for (int vbo : vbos) GL15.glDeleteBuffers(vbo);
        for (int texture : textures) GL11.glDeleteTextures(texture);
    }
}
