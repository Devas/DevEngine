package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;
    private String name;

    public Entity(String name, TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        this.name = name;
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        rotation.x += dx;
        rotation.y += dy;
        rotation.z += dz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TexturedModel getTexturedModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPositionX(float positionX) {
        this.position.x = positionX;
    }

    public void setPositionY(float positionY) {
        this.position.y = positionY;
    }

    public void setPositionZ(float positionZ) {
        this.position.z = positionZ;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotationX(float rotationX) {
        this.rotation.x = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.rotation.y = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        this.rotation.z = rotationZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
