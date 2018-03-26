package shaders.gui;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = SHADERS_PATH + "gui/guiVertexShader.glsl";
    private static final String FRAGMENT_FILE = SHADERS_PATH + "gui/guiFragmentShader.glsl";

    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void bindAttributes() {
        super.bindVertexAttributeArrayToShaderVariable(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
}