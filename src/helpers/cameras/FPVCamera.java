package helpers.cameras;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class FPVCamera extends Camera {

    private static float CAM_POSITION_SHIFT = 0.4f;
    private static float CAM_ADJUSTMENT_SHIFT = 0.1f;
    private static final float SENSITIVITY_FACTOR = 2.f; // affects speed of cameras when keys +/- are pressed

    public FPVCamera() {
        super();
    }

    public FPVCamera(Vector3f position) {
        super(position);
    }

    @Override
    public void move() {
        float speed = 0.5f;
        yaw = -(Display.getWidth() - Mouse.getX() / 2);
        pitch = (Display.getHeight() / 2) - Mouse.getY();

        if (pitch >= 90) {
            pitch = 90;
        } else if (pitch <= -90) {
            pitch = -90;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z += -(float) Math.cos(Math.toRadians(yaw)) * speed;
            position.x += (float) Math.sin(Math.toRadians(yaw)) * speed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z -= -(float) Math.cos(Math.toRadians(yaw)) * speed;
            position.x -= (float) Math.sin(Math.toRadians(yaw)) * speed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.z += (float) Math.sin(Math.toRadians(yaw)) * speed;
            position.x += (float) Math.cos(Math.toRadians(yaw)) * speed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.z -= (float) Math.sin(Math.toRadians(yaw)) * speed;
            position.x -= (float) Math.cos(Math.toRadians(yaw)) * speed;
        }

//        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
//            position.y -= CAM_POSITION_SHIFT;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
//            position.y += CAM_POSITION_SHIFT;
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
//            pitch -= CAM_ADJUSTMENT_SHIFT;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
//            pitch += CAM_ADJUSTMENT_SHIFT;
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
//            yaw -= CAM_ADJUSTMENT_SHIFT;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
//            yaw += CAM_ADJUSTMENT_SHIFT;
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
//            roll -= CAM_ADJUSTMENT_SHIFT;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
//            roll += CAM_ADJUSTMENT_SHIFT;
//        }

        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
            super.restoreDefaultPosition();
        }

//        if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
//            CAM_POSITION_SHIFT *= SENSITIVITY_FACTOR;
//            CAM_ADJUSTMENT_SHIFT *= SENSITIVITY_FACTOR;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
//            CAM_POSITION_SHIFT /= SENSITIVITY_FACTOR;
//            CAM_ADJUSTMENT_SHIFT /= SENSITIVITY_FACTOR;
//        }

        ////Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        //Mouse.setGrabbed(true);
    }
}
