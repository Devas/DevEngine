package utils;

public class MathUtil {

    /**
     * Clamp value i.e.:
     * <ul>
     * <li>if value is smaller than min then return min
     * <li>if value is greater than max then return max
     * <li>else return value
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
}
