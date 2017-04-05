package helpers.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public abstract class Camera {

    protected Vector3f position;
    protected Vector3f defaultPosition;
    protected float pitch;
    protected float defaultPitch;
    protected float yaw;
    protected float defaultYaw;
    protected float roll;
    protected float defaultRoll;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        defaultPosition = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position) {
        this.position = position;
        this.defaultPosition = new Vector3f(position);
    }

    /**
     * Implement the movements of your camera. The input should change the fields of the Camera class.
     * Use the input sources from org.lwjgl.input package.
     */
    public abstract void move();

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

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
        if(position.y < TERRAIN_HEIGHT){
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
