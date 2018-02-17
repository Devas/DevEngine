package audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

/**
 * If AL_ROLLOFF_FACTOR is 0 there is no attenuation.
 */
public class AttenuationPresets {

    public static void mode1(int sourceId) {
        AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 1);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 15);
    }

    public static void mode2(int sourceId) {
        AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 2);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 50);
    }

    public static void mode3(int sourceId) {
        AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE_CLAMPED);
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 2);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 50);
    }
}
