package display;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * This class is responsible for:
 * <ul>
 * <li>creating the display (by creating OpenGL context)</li>
 * <li>updating the display</li>
 * <li>closing the display</li>
 * </ul>
 * <p>
 * All methods are static because this class uses static methods from org.lwjgl.opengl.Display class.
 * <p>
 * This class has also getCurrentFrameDurationSeconds() method which is used to synchronize the movement of objects
 * with the time duration of the frame. The speed of objects' movement will not be affected at all by FPS.
 */
public class DisplayManager {

    private static final String WINDOW_TITLE = "DevEngine";
    private static final Resolution resolution = Resolution.WQHD;
    public static final int WIDTH = resolution.width;
    public static final int HEIGHT = resolution.height;
    public static final float ASPECT_RATIO = (float) WIDTH / (float) HEIGHT;
    private static final int FPS_CAP = 120;

    private static long lastFrameEndTime;
    private static float currentFrameDurationSeconds;
    private static boolean isFullscreen = false;
    private static boolean isVSync = false;

    public static void createDisplay() {
        ContextAttribs contextAttribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

        try {
            setDisplayMode(WIDTH, HEIGHT, isFullscreen);
            Display.create(new PixelFormat(), contextAttribs);
            Display.setTitle(WINDOW_TITLE);
//            Mouse.setGrabbed(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);

        lastFrameEndTime = getCurrentTimeMilliSeconds();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();

        long currentFrameEndTime = getCurrentTimeMilliSeconds();
        currentFrameDurationSeconds = (currentFrameEndTime - lastFrameEndTime) / 1000f;
        lastFrameEndTime = currentFrameEndTime;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    public static void toggleFullscreen() {
        System.out.println("Fullscreen: " + isFullscreen);
        isFullscreen = !isFullscreen;
        System.out.println("Fullscreen: " + isFullscreen);
        setDisplayMode(WIDTH, HEIGHT, isFullscreen);
    }

    public static void toggleVSync() {
        System.out.println("VSync: " + isVSync);
        isVSync = !isVSync;
        System.out.println("VSync: " + isVSync);
        Display.setVSyncEnabled(isVSync);
    }

    public static float getCurrentFrameDurationSeconds() {
        return currentFrameDurationSeconds;
    }

    private static long getCurrentTimeMilliSeconds() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    private static void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (DisplayMode current : modes) {
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequency against the original display mode
                        // then it's probably best to go for this one since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }

            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }
}
