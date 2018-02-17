package helpers.cameras;

import entities.Entity;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import utils.MathUtil;

/**
 * This cameras follows a specified Entity.
 * <p>
 * Mouse wheel - zoom
 * RMB - orbit cameras around entity
 */
public class EntityCamera extends Camera {

    private static float CAM_POSITION_SHIFT = 0.4f;
    private static float CAM_ADJUSTMENT_SHIFT = 0.1f;
    private static final float SENSITIVITY_FACTOR = 2f; // affects speed of cameras when keys +/- are pressed

    private static final float MIN_DISTANCE_FROM_ENTITY = 5f;
    private static final float MAX_DISTANCE_FROM_ENTITY = 500f;
    private static final float LINEAR_ZOOM_SPEED_FACTOR = 0.01f;
    private static final float PROGRESSIVE_ZOOM_SPEED_FACTOR = 0.001f;
    private static final float X_AXIS_SPEED_FACTOR = 0.3f;
    private static final float Y_AXIS_SPEED_FACTOR = 0.3f;

    //    public boolean allowCameraZoom; // TODO enable / disable some features like zoom via configuration
    public static boolean useProgressiveZoom = true; // If enabled you zoom in/out quicker the further you are away from the entity

    private static final float ENTITY_HEIGHT = 5f; // TODO Zoom in the centre of object, for now use dummy height

    private float distanceFromEntity = 50f;
    private float angleAroundEntity = 0f;

    private Entity entity;

    /**
     * Default and initial position is (0,0,0).
     *
     * @param entity Entity followed by the cameras.
     */
    public EntityCamera(Entity entity) {
        super();
        this.entity = entity;
    }

    /**
     * @param entity   Entity followed by the cameras.
     * @param position Default and initial position.
     */
    public EntityCamera(Entity entity, Vector3f position) {
        super(position);
        this.entity = entity;
    }

    /**
     * @return Entity followed by the cameras.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @param entity Entity to be followed by the cameras.
     */
    public void setEntity(Entity entity) { // TODO test it - allows to change followed entity dynamically
        this.entity = entity;
    }

    @Override
    public void move() {
        calculateDistanceFromEntity();
        calculatePitch();
        calculateAngleAroundEntity();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        super.yaw = 180 - (entity.getRotation().y + angleAroundEntity);

//        super.yaw = 180 - (angleAroundEntity); // TODO this won't rotate cameras, only a player, usable only if pith = 90 (top-down view)

//        super.restoreDefaultPosition();


//        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
//        Mouse.setGrabbed(true);


//        if (Mouse.isButtonDown(1)) {
//            if (!Mouse.isGrabbed())
//                Mouse.setGrabbed(true);
//            calculateAngleAroundEntity();
//            calculatePitch();
//            if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
//                entity.increaseRotation(0, angleAroundEntity, 0);
//                angleAroundEntity = 0;
//            }
//        } else if (!Mouse.isButtonDown(1)) {
//            if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
//                angleAroundEntity /= 1.2f;
//                if (angleAroundEntity >= -0.5f && angleAroundEntity <= 0.5f)
//                    angleAroundEntity = 0;
//            }
//        }
//        if (!Mouse.isButtonDown(1) && Mouse.isGrabbed())
//            Mouse.setGrabbed(false);

    }

    private void calculateDistanceFromEntity() {
        float distanceChange;
        if (useProgressiveZoom == false) {
            distanceChange = Mouse.getDWheel() * LINEAR_ZOOM_SPEED_FACTOR;
        } else {
            distanceChange = Mouse.getDWheel() * PROGRESSIVE_ZOOM_SPEED_FACTOR * distanceFromEntity;
        }
        distanceFromEntity -= distanceChange;
        limitDistanceFromEntity();
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * Y_AXIS_SPEED_FACTOR;
            super.pitch -= pitchChange;
            super.limitPith(-10f, 90f);
        }
    }

    private void calculateAngleAroundEntity() {
        if (Mouse.isButtonDown(1)) {
            float angleChange = Mouse.getDX() * X_AXIS_SPEED_FACTOR;
            angleAroundEntity -= angleChange;
        }
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromEntity * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromEntity * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float totalAngle = entity.getRotation().y + angleAroundEntity;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(totalAngle)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(totalAngle)));
        super.position.x = entity.getPosition().x - offsetX;
        super.position.z = entity.getPosition().z - offsetZ;
        super.position.y = entity.getPosition().y + verticalDistance + ENTITY_HEIGHT;
        super.blockGoingBelowTerrain();
    }

    private void limitDistanceFromEntity() {
        distanceFromEntity = MathUtil.clamp(distanceFromEntity, MIN_DISTANCE_FROM_ENTITY, MAX_DISTANCE_FROM_ENTITY);
    }

    // Bugged
//    private void limitAngleAroundEntity() {
//        angleAroundEntity = MathUtil.clamp(angleAroundEntity, 60, 60);
//    }

}
