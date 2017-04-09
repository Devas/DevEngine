package entities;

import devengine.DisplayManager;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;

public class Player extends Entity {

    private static final float MOVE_SPEED = 20; // units per second
    private static final float RUN_SPEED = 100; // units per second
    private static final float TURN_SPEED = 160; // degrees per second
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private float currentMoveSpeed = 0;
//    private float currentMoveSpeed2 = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;
    private boolean isInAir = false;

    public Player(String name, TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(name, model, position, rotation, scale);
    }

    /**
     * currentTurnSpeed it's amount the player is turning per second so we need multiply it by amount of seconds that
     * have passed
     */
    public void move(Terrain terrain) {
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getCurrentFrameDurationSeconds(), 0); // TODO can be controlled by mouse super.increaseRotation(0, -Mouse.getDX(), 0);

        float distance = currentMoveSpeed * DisplayManager.getCurrentFrameDurationSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().y))); // we rotate along vertical Y axis
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().y)));
        super.increasePosition(dx, 0, dz);

//        float distanceZ = currentMoveSpeed2 * DisplayManager.getCurrentFrameDurationSeconds();
//        super.increasePosition(distanceZ, 0, 0);

        upwardsSpeed += GRAVITY * DisplayManager.getCurrentFrameDurationSeconds(); // apply negative value of gravity
        // we must increase position first and check terrain second right before frame is rendered to avoid jump-flickering
        super.increasePosition(0, upwardsSpeed * DisplayManager.getCurrentFrameDurationSeconds(), 0);

        // float terrainHeight = 0; // If you make flat terrain set it to 0
        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void jump() {
        if (!isInAir) {
            upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                currentMoveSpeed = RUN_SPEED;
            } else {
                currentMoveSpeed = MOVE_SPEED;
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                currentMoveSpeed = -RUN_SPEED;
            } else {
                currentMoveSpeed = -MOVE_SPEED;
            }
        } else {
            currentMoveSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            currentTurnSpeed = TURN_SPEED;
//            currentMoveSpeed2 = MOVE_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            currentTurnSpeed = -TURN_SPEED;
//            currentMoveSpeed2 = -MOVE_SPEED;
        } else {
            currentTurnSpeed = 0;
//            currentMoveSpeed2 = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            jump();
        }
    }

}
