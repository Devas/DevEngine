package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Base class for all shaders. Contains many methods that allow to operate on shaders.
 * Basically you inherit this class and implement bindAllAttributesToShaderVariables() and getAllUniformLocations().
 */
public abstract class ShaderProgram {

    protected static final String SHADERS_PATH = "src/shaders/";

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16); // Buffer for 4x4 float matrix

    /**
     * Creates generic shader program.
     * <p>
     * Loads vertex shader and fragment shader from files.
     *
     * @param vertexShaderPath path to vertex shader file
     * @param fragmentShaderPath path to fragment shader file
     */
    protected ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
        vertexShaderID = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAllAttributesToShaderVariables();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
    }

    /**
     * In this method you should bind all VAO attributes to shader variables (in vertex and fragment shaders).
     * Use bindAttributeToShaderVariable() to implement this method in child class.
     */
    protected abstract void bindAllAttributesToShaderVariables();

    /**
     * In this method you should retrieve from shaders all locations of uniform variables.
     * Use getUniformLocation() to implement this method in child class.
     */
    protected abstract void getAllUniformLocations();

    /**
     * Binds specified vertex attribute array into specified shader variable.
     * Use it when implementing bindAllAttributesToShaderVariables() in child class.
     *
     * @param attributeIndex index of vertex attribute array in VAO that we want to bind into shader
     * @param shaderVariableName name of variable in shader into which specified vertex attribute array will be bound
     */
    protected void bindAttributeToShaderVariable(int attributeIndex, String shaderVariableName) {
        GL20.glBindAttribLocation(programID, attributeIndex, shaderVariableName);
    }

    /**
     * Returns the location of the uniform variable declared in shader.
     * Use it when implementing getAllUniformLocations() in child class.
     *
     * @param uniformVariableName name of uniform variable declared in shader
     * @return location of uniform variable declared in shader
     */
    protected int getUniformLocation(String uniformVariableName) {
        return GL20.glGetUniformLocation(programID, uniformVariableName);
    }

    /**
     * Enables shader program.
     */
    public void start() {
        GL20.glUseProgram(programID);
    }

    /**
     * Disables shader program.
     */
    public void stop() {
        GL20.glUseProgram(0);
    }

    /**
     * Disables shader program and then deletes it.
     */
    public void cleanUp() {
        stop();
        GL20.glDeleteProgram(programID);
    }

    /**
     * Wrapper method that loads int value to shader variable.
     *
     * @param location location of shader variable
     * @param value value to be loaded to shader variable
     */
    protected void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    /**
     * Wrapper method that loads float value to shader variable.
     *
     * @param location location of shader variable
     * @param value value to be loaded to shader variable
     */
    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    /**
     * Wrapper method that loads boolean value to shader variable.
     *
     * @param location location of shader variable
     * @param value value to be loaded to shader variable
     */
    protected void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

    /**
     * Wrapper method that loads 2-float vector to shader variable.
     *
     * @param location location of shader variable
     * @param vector 2-float vector to be loaded to shader variable
     */
    protected void loadVector(int location, Vector2f vector) {
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    /**
     * Wrapper method that loads 3-float vector to shader variable.
     *
     * @param location location of shader variable
     * @param vector 3-float vector to be loaded to shader variable
     */
    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    /**
     * Wrapper method that loads 4x4 float matrix to shader variable.
     *
     * @param location location of shader variable
     * @param matrix 4x4 float matrix to be loaded to shader variable
     */
    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrix4fBuffer);
        matrix4fBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrix4fBuffer);
    }

    /**
     * Loads shader of specified type (GL_VERTEX_SHADER or GL_FRAGMENT_SHADER) from file.
     *
     * @param file path to shader file
     * @param type type of shader (GL_VERTEX_SHADER or GL_FRAGMENT_SHADER)
     * @return id of loaded shader
     */
    private int loadShader(String file, int type) { // TODO extract to ShaderLoader?
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }

    public void printShaderStats() {
        System.out.println("GL_SHADING_LANGUAGE_VERSION:" + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
        GL20.glGetShaderInfoLog(fragmentShaderID, 1000);
        GL20.glGetShaderInfoLog(vertexShaderID, 1000);
        GL20.glGetProgramInfoLog(programID, 1000);
    }
}