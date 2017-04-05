package devengine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    private static long lastFrameEndTime;
    private static float currentFrameDurationSeconds;

    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("DevEngine");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameEndTime = getCurrentTimeMiliSeconds();
    }

    public static float getCurrentFrameDurationSeconds() {
        return currentFrameDurationSeconds;
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameEndTime = getCurrentTimeMiliSeconds();
        currentFrameDurationSeconds = (currentFrameEndTime - lastFrameEndTime) / 1000f;
        lastFrameEndTime = currentFrameEndTime;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    private static long getCurrentTimeMiliSeconds() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

}
