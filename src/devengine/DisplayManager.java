package devengine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * This class is responsible for:
 * -creating the display (by creating OpenGL context)
 * -updating the display
 * -closing the display
 * <p>
 * All methods are static because this class uses static methods from org.lwjgl.opengl.Display class.
 * <p>
 * This class has also getCurrentFrameDurationSeconds() method which is used to synchronize the movement of objects
 * with the time duration of the frame. The speed of objects' movement will not be affected at all by FPS.
 */
public class DisplayManager {

    // TODO add different resolutions support
    private static final int WIDTH = 3840;
    private static final int HEIGHT = 2160;
    private static final float ASPECTRATIO = (float) WIDTH / (float) HEIGHT;
    private static final int FPSCAP = 120;
    private static long lastFrameEndTime;
    private static float currentFrameDurationSeconds;

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static float getASPECTRATIO() {
        return ASPECTRATIO;
    }

    public static int getFPSCAP() {
        return FPSCAP;
    }

    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("DevEngine");
//            Mouse.setGrabbed(true); // TODO enable / disable
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
        Display.sync(FPSCAP);
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
