package models;

/**
 * Stores information needed for OpenGL to render an object.
 * <p>
 * verticesToRenderCount is not exactly the same as number of vertices in model.
 * Simple quad consisting of 2 triangles has 4 vertices in model but to render it we need 6 vertices (3 per triangle).
 * This number is needed for glDrawArrays() or glDrawElements()
 */
public class RawModel {

    private int vaoID;
    private int verticesToRenderCount;
    private boolean faceCulled = true; // Face Culling is enabled by default for maximum efficiency // TODO can be moved also to TexturedModel

    /**
     * @param vaoID id of VAO
     * @param verticesToRenderCount number of vertices to render (not exactly the same as number of vertices in model)
     */
    public RawModel(int vaoID, int verticesToRenderCount) {
        this.vaoID = vaoID;
        this.verticesToRenderCount = verticesToRenderCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVerticesToRenderCount() {
        return verticesToRenderCount;
    }

    /**
     * @return true if an object uses Face Culling, false otherwise
     */
    public boolean isFaceCulled() {
        return faceCulled;
    }

    /**
     * Face culling removes non-visible triangles of closed surfaces to get maximum efficiency.
     * <p>
     * Argument should be set to true for closed objects when it's not possible to see an interior of the object,
     * for example primitives like box or 3d models like stanford-dragon.
     * <p>
     * Argument should be set to false for not-closed objects when a texture should be visible from both sides,
     * for example grass, foliage, leaves.
     *
     * @param faceCulling true if an object should use Face Culling, false otherwise
     */
    public void setFaceCulled(boolean faceCulling) {
        this.faceCulled = faceCulling;
    }
}
