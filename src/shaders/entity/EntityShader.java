package shaders.entity;

import lights.Light;
import helpers.cameras.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import utils.MatrixUtil;

public class EntityShader extends ShaderProgram {

    private static final String VERTEX_FILE = SHADERS_PATH + "entity/entityVertexShader.glsl";
    private static final String FRAGMENT_FILE = SHADERS_PATH + "entity/entityFragmentShader.glsl";

    // Vertex shader
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_useFakeLighting;
    private int location_textureAtlasSize;
    private int location_textureAtlasOffsets;
    // Fragment shader
    // TODO check textureSampler why is not loaded here
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;

    public EntityShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAllAttributesToShaderVariables() {
        super.bindAttributeToShaderVariable(0, "position");
        super.bindAttributeToShaderVariable(1, "textureCoords");
        super.bindAttributeToShaderVariable(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        // Vertex shader
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_textureAtlasSize = super.getUniformLocation("textureAtlasSize");
        location_textureAtlasOffsets = super.getUniformLocation("textureAtlasOffsets");
        // Fragment shader
        location_lightColour = super.getUniformLocation("lightColour");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColour = super.getUniformLocation("skyColour");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MatrixUtil.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadLight(Light light) {
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColour, light.getColour());
    }

    public void loadShineVariables(float shineDamper, float reflectivity) {
        super.loadFloat(location_shineDamper, shineDamper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadFakeLightingVariable(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadSkyColour(Vector3f skyColour) {
        super.loadVector(location_skyColour, skyColour);
    }

    public void loadTextureAtlasSize(int textureAtlasSize) {
        super.loadFloat(location_textureAtlasSize, textureAtlasSize);
    }

    public void loadTextureAtlasOffsets(float xOffset, float yOffset) {
        super.loadVector(location_textureAtlasOffsets, new Vector2f(xOffset, yOffset));
    }
}
