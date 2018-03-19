package utils;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class MathUtil {

    /**
     * Clamp value i.e.:
     * <ul>
     * <li>if value is smaller than min then return min</li>
     * <li>if value is greater than max then return max</li>
     * <li>else return value</li>
     * </ul>
     *
     * @param value value to be clamped
     * @param min minimum value of clamping
     * @param max maximum value of clamping
     * @return clamped value
     */
    public static float clamp(float value, float min, float max) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else {
            return value;
        }
    }

    /**
     * Interpolate between two inputs (min, max) for a parameter (value) in the closed unit interval [0, 1]
     * https://en.wikipedia.org/wiki/Linear_interpolation
     *
     * Imprecise method, which does not guarantee v = v1 when t = 1, due to floating-point arithmetic error.
     * This form may be used when the hardware has a native fused multiply-add instruction.
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static float lerpFast(float value, float min, float max) {
        return min + value * (max - min);
    }

    /**
     * Interpolate between two inputs (min, max) for a parameter (value) in the closed unit interval [0, 1]
     * https://en.wikipedia.org/wiki/Linear_interpolation
     *
     * Precise method, which guarantees v = max when value = 1.
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static float lerp(float value, float min, float max) {
        return (1 - value) * min + value * max;
    }

    /**
     * https://en.wikipedia.org/wiki/Barycentric_coordinate_system
     *
     * @param p1
     * @param p2
     * @param p3
     * @param pos
     * @return
     */
    public static float barycentricCoordsOnTriangle(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }
}
