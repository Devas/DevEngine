package renderers;

import helpers.cameras.Camera;
import entities.Entity;
import helpers.Light;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import toolbox.Maths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    // TODO SkyColour class
//    private static Vector3f skyColour = new Vector3f(0.5f, 0.5f, 0.5f); // heavy foggy
    private static Vector3f skyColour = new Vector3f(0.6f, 0.78f, 0.76f); // atmospheric fog 1
//    private static Vector3f skyColour = new Vector3f(0.5f, 0.6f, 0.7f); // atmospheric fog 2
//    private static Vector3f skyColour = (new Vector3f(255, 197, 143)).normalise(null); // dust-desert fog

    private StaticShader staticShader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainShader terrainShader = new TerrainShader();
    private TerrainRenderer terrainRenderer;

    private Map<TexturedModel, List<Entity>> entitiesMap = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    /**
     * Face Culling is enabled here to get maximum efficiency.
     */
    public MasterRenderer() {
        enableFaceCulling();
        Matrix4f projectionMatrix = Maths.createProjectionMatrix();
        entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    /**
     * Face culling removes non-visible triangles of closed surfaces to get maximum efficiency. This method enables it.
     */
    public static void enableFaceCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Face culling removes non-visible triangles of closed surfaces to get maximum efficiency. This method disables it.
     */
    public static void disableFaceCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * Executed once per frame to render objects and terrain.
     * <p>
     * 1: Prepares shader, loads Light, loads ViewMatrix.
     * 2: Calls render() method of EntityRenderer to render all entities which are already grouped in the entitiesMap.
     * 3: Removes all of the mappings from entitiesMap.
     *
     * @param light
     * @param camera
     */
    public void render(Light light, Camera camera) {
        prepare();

        staticShader.start();
        staticShader.loadSkyColour(skyColour);
        staticShader.loadLight(light);
        staticShader.loadViewMatrix(camera);
        entityRenderer.render(entitiesMap);
        staticShader.stop();
        entitiesMap.clear();

        terrainShader.start();
        terrainShader.loadSkyColour(skyColour);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
    }

    /**
     * Assigns a single Entity to proper batch.
     * This method ensures that all Entities are grouped in entitiesMap based on TexturedModel which is a key.
     * <p>
     * Executed for every Entity once per frame.
     *
     * @param entity Entity to be processed
     */
    public void processEntity(Entity entity) {
        TexturedModel texturedModel = entity.getTexturedModel();
        List<Entity> batch = entitiesMap.get(texturedModel);
        // If batch for that particular TexturedModel already exists, add this entity to existing batch. If batch != null then batch exists.
        if (batch != null) {
            batch.add(entity);
        }
        // If there are no batches for that particular TexturedModel, we need to create new batch for that TexturedModel. Next we put this in HshMap.
        else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entitiesMap.put(texturedModel, newBatch);
        }
    }

    /**
     * Adds a single Terrain to the list of all terrains.
     * <p>
     * Executed for every Terrain once per frame.
     *
     * @param terrain Terrain to processed
     */
    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void cleanUp() {
        staticShader.cleanUp();
        terrainShader.cleanUp();
    }

    public void printProcessingInfo() {
        System.out.println("Last processing info:");
        for (Map.Entry<TexturedModel, List<Entity>> entry : entitiesMap.entrySet()) {
            System.out.println("Entity name: " + entry.getValue().get(0).getName() + " Number of entities: " + entry.getValue().size());
        }
        System.out.println();
    }

    private void prepare() {
        // TODO background color
        GL11.glClearColor(skyColour.x, skyColour.y, skyColour.z, 1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void enableFillPolygonMode() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public void enableLinePolygonMode() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
//        GL11.glLineWidth(2.0f); // change seize of line
    }

    public void enablePointPolygonMode() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
//        GL11.glPointSize(1.4f); // change seize of point
    }

}
