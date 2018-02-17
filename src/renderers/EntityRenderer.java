package renderers;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import utils.MatrixUtil;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Render all entities which are already grouped in the entitiesMap.
     * <p>
     * Executed once per frame.
     *
     * @param entities All entities already grouped in Map
     */
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel texturedModel : entities.keySet()) {
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    /**
     * This method executes for every TexturedModel.
     */
    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = texturedModel.getTexture();
        shader.loadTextureAtlasSize(texture.getTextureAtlasSize());
        // Face Culling is enabled by default but we want to disable it for some objects i.e. grass, foliage
        if (!rawModel.isFaceCulled()) {
            MasterRenderer.disableFaceCulling();
        }
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
    }

    /**
     * This method also re-enables Face Culling to get maximum efficiency in case Face Culling was disabled
     * in prepareTexturedModel() method.
     */
    private void unbindTexturedModel() {
        MasterRenderer.enableFaceCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /**
     * This method executes for every instance of TexturedModel. It:
     * -loads transformation matrix
     * -loads texture atlas offsets
     */
    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix =
                MatrixUtil.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadTextureAtlasOffsets(entity.getTextureAtlasOffsetX(), entity.getTextureAtlasOffsetY()); // TODO can be changed to f taking Vector2f
    }

}
