package helpers.cameras;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public abstract class Camera {

    Vector3f defaultPosition;
    Vector3f position;
    float defaultPitch;
    float pitch;
    float defaultYaw;
    float yaw;
    float defaultRoll;
    float roll;

    public Camera() {
        defaultPosition = new Vector3f(0, 0, 0);
        position = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position) {
        this.defaultPosition = new Vector3f(position);
        this.position = position;
    }

    public Vector3f getDefaultPosition() {
        return defaultPosition;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getDefaultPitch() {
        return defaultPitch;
    }

    public float getPitch() {
        return pitch;
    }

    public float getDefaultYaw() {
        return defaultYaw;
    }

    public float getYaw() {
        return yaw;
    }

    public float getDefaultRoll() {
        return defaultRoll;
    }

    public float getRoll() {
        return roll;
    }

    /**
     * Implement the movements of your cameras.
     * The input should change the fields of the Camera class.
     * Use the input sources from org.lwjgl.input package.
     */
    public abstract void move();

    public void printCameraInfo() {
        System.out.println("Camera info:");
        System.out.println("Position X: " + position.getX());
        System.out.println("Position Y: " + position.getY());
        System.out.println("Position Z: " + position.getZ());
        System.out.println();
    }

    protected void restoreDefaultPosition() {
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) { // TODO here input or in child class?
            position.x = defaultPosition.x;
            position.y = defaultPosition.y;
            position.z = defaultPosition.z;
        }
    }

    protected void restoreDefaultPitchYawRoll() {
        pitch = yaw = roll = 0; // TODO default pitch... according to snapping
    }

    // TODO make TERRAIN_HEIGHT dynamic upon implementation of terrain height maps...﻿
    // TODO i.e. add Terrain param in constructor and use in blockGoingBelowTerrain()
    protected void getHeightOfTerrain() {

    }

    protected void blockGoingBelowTerrain() {
        float TERRAIN_HEIGHT = 1.f;
        if (position.y < TERRAIN_HEIGHT) {
            position.y = TERRAIN_HEIGHT;
        }
    }

    protected void limitPith(float minPitch, float maxPitch) {
        pitch = Maths.clamp(pitch, minPitch, maxPitch);
    }

    protected void limitYaw(float minYaw, float maxYaw) {
        yaw = Maths.clamp(yaw, minYaw, maxYaw);
    }

    protected void limitRoll(float minRoll, float maxRoll) {
        roll = Maths.clamp(roll, minRoll, maxRoll);
    }

}
