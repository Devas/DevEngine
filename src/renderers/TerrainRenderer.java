package renderers;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.terrain.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexturePack;
import utils.MatrixUtil;

import java.util.List;

/**
 * Renderer for terrains.
 * <p>
 * Does not use batch rendering. Terrains usually differ from each other so batch rendering is not used.
 */
public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadTextureUnits();
        shader.stop();
    }

    /**
     * Render all terrains. Terrains usually differ from each other so batch rendering is not used.
     * <p>
     * Executed once per frame. Uses index buffer in glDrawElements.
     *
     * @param terrains All terrains to be rendered
     */
    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVerticesToRenderCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTerrainModel();
        }
    }

    /**
     * This method executes for every Terrain.
     */
    private void prepareTerrain(Terrain terrain) {
        RawModel rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTextures(terrain);
        //TODO for now fixed values for speculars
//        shader.loadShineVariables(terrain.getBlendMapTexture().getShineDamper(), terrain.getBlendMapTexture().getReflectivity());
        shader.loadShineVariables(1, 0);
    }

    //TODO place blendMap in unit 0 and rest of textures in following units
    private void bindTextures(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.getTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMapTexture().getID());
    }

    /**
     * This method executes for every instance of Terrain. It:
     * -loads transformation matrix
     * -loads texture atlas offsets
     */
    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = MatrixUtil.createTransformationMatrix(terrain.getPosition(), new Vector3f(0, 0, 0), 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void unbindTerrainModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
