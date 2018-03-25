package audio;

import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        AudioMaster.init();
        AudioMaster.setListenerData(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

        int buffer = AudioMaster.loadSound("audio/bounce.wav");
        Source source = new Source();
        AttenuationPresets.mode2(source.getSourceId());
        source.setLooping(true);
        source.play(buffer);

        Vector3f pos = new Vector3f(1, 0, 2);
        source.setPosition(pos);

        char c = ' ';
        while (c != 'q') {
            pos.x -= 0.02f;
            source.setPosition(pos);
            System.out.println(pos.x);
            Thread.sleep(10);


//            c = (char) System.in.read();
//            if (c == 'p') {
//                if (source.isPlaying()) {
//                    source.pausePlaying();
//                } else {
//                    source.continuePlaying();
//                }
//            }
        }

        source.delete();
        AudioMaster.cleanUp();
    }
}
