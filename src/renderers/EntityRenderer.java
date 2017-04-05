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
import toolbox.Maths;

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
                loadModelMatrix(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture modelTexture = texturedModel.getTexture();
        if (modelTexture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }
        shader.loadShineVariables(modelTexture.getShineDamper(), modelTexture.getReflectivity());
        shader.loadFakeLightingVariable(modelTexture.isUseFakeLighting());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTexture.getID());
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(),
                entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

}
