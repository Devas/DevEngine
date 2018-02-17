package audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

public class Source {

    private int sourceId;

    public Source() {
        sourceId = AL10.alGenSources();
//        AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
//        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
//        AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);

//        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 1);
//        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
//        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 15);
    }

    /**
     * Stops playing a prior sound and starts playing a new sound.
     *
     * @param buffer id of the sound to be played
     */
    public void play(int buffer) {
        stopPlaying();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer); // binds a the sound to the source
        continuePlaying();
    }

    public void pausePlaying() {
        AL10.alSourcePause(sourceId);
    }

    public void continuePlaying() {
        AL10.alSourcePlay(sourceId);
    }

    public void stopPlaying() {
        AL10.alSourceStop(sourceId);
    }

    public int getSourceId() {
        return sourceId;
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
    }

    public void setPosition(Vector3f position) {
        AL10.alSource3f(sourceId, AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public void setVelocity(Vector3f velocity) {
        AL10.alSource3f(sourceId, AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

    public void setLooping(boolean shouldLoop) {
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, shouldLoop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public void delete() {
        stopPlaying();
        AL10.alDeleteSources(sourceId);
    }
}
