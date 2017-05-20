package models;

public class RawModel {

    private int vaoID;
    private int vertexCount;

    private boolean faceCulled = true; // Face Culling is enabled by default for maximum efficiency // TODO can be moved also to TexturedModel or Texture

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
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
