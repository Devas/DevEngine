package helpers.cameras;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * This cameras allows to view the world from flight perspective;
 * <p>
 * WSAD - movement
 * QE - height
 * Numpads - adjustment
 * +/- - speed
 */
public class KeyboardCamera extends Camera {

    private static float CAM_POSITION_SHIFT = 0.4f;
    private static float CAM_ADJUSTMENT_SHIFT = 0.1f;
    private static final float SENSITIVITY_FACTOR = 2.f; // affects speed of cameras when keys +/- are pressed

    public KeyboardCamera() {
        super();
    }

    public KeyboardCamera(Vector3f position) {
        super(position);
    }

    @Override
    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) position.z -= CAM_POSITION_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) position.z += CAM_POSITION_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) position.x -= CAM_POSITION_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) position.x += CAM_POSITION_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) position.y -= CAM_POSITION_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) position.y += CAM_POSITION_SHIFT;

        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) pitch -= CAM_ADJUSTMENT_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) pitch += CAM_ADJUSTMENT_SHIFT;

        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) yaw -= CAM_ADJUSTMENT_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) yaw += CAM_ADJUSTMENT_SHIFT;

        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) roll -= CAM_ADJUSTMENT_SHIFT;
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) roll += CAM_ADJUSTMENT_SHIFT;

        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) super.restoreDefaultPosition();

        if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
            CAM_POSITION_SHIFT *= SENSITIVITY_FACTOR;
            CAM_ADJUSTMENT_SHIFT *= SENSITIVITY_FACTOR;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
            CAM_POSITION_SHIFT /= SENSITIVITY_FACTOR;
            CAM_ADJUSTMENT_SHIFT /= SENSITIVITY_FACTOR;
        }
    }
}
