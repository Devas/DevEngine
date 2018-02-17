package guis;

import loaders.Loader;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import utils.MatrixUtil;

import java.util.List;

public class GuiRenderer {

    private final RawModel guiRawModel;
    private GuiShader shader;

    public GuiRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        guiRawModel = loader.loadToVAO(positions);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guiTextures) {
        shader.start();
        GL30.glBindVertexArray(guiRawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for (GuiTexture guiTexture : guiTextures) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, guiTexture.getTextureID());
            Matrix4f matrix = MatrixUtil.createTransformationMatrix(guiTexture.getPosition(), guiTexture.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, guiRawModel.getVertexCount());
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }

}
