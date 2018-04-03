package renderers;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shaders.entity.EntityShader;
import textures.ModelTexture;
import utils.MatrixUtil;

import java.util.List;
import java.util.Map;

/**
 * Renderer for entities.
 * <p>
 * Uses batch rendering i.e. if entities use the same TexturedModel then some data (e.g textures) are loaded only once.
 */
public class EntityRenderer {

    private EntityShader shader;

    public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Render all entities which are already grouped in the entitiesMap.
     * <p>
     * Executed once per frame. Uses index buffer in glDrawElements.
     *
     * @param entities All entities already grouped in Map to be rendered
     */
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel texturedModel : entities.keySet()) {
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVerticesToRenderCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    /**
     * This method executes for every TexturedModel. It:
     * -binds VAO
     * -enables vertex attributes arrays in VAO
     * -loads texture related parameters to shader
     * -activates and binds texture
     */
    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = texturedModel.getTexture();
        shader.loadTextureAtlasSize(texture.getTextureAtlasSize());
        if (!rawModel.isFaceCulled()) { // Face Culling is enabled by default. Disable it for some objects i.e. grass, foliage
            MasterRenderer.disableFaceCulling();
        }
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        shader.loadFakeLightingVariable(texture.isFakeLighting());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
    }

    /**
     * This method executes for every Entity (instance of TexturedModel). It:
     * -creates transformation matrix and loads it to vertex shader
     * -loads texture atlas offsets
     */
    private void prepareInstance(Entity e) {
        Matrix4f transformationMatrix = MatrixUtil.createTransformationMatrix(e.getPosition(), e.getRotation(), e.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadTextureAtlasOffsets(e.getTextureAtlasOffsets());
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
}
