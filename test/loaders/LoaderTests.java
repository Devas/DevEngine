package loaders;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class LoaderTests {

    private static final float[] FLOAT_DATA = {1.0f, 2.0f, 3.0f};
    private static final int[] INT_DATA = {1, 2, 3, 4, 5};

    @BeforeClass
    public void init() throws LWJGLException {
        Display.create(new PixelFormat());
    }

    @AfterClass
    public void close() throws LWJGLException {
        Display.releaseContext();
    }

    public void shouldCreateFloatBufferWithTheSameSize() {
        assertEquals(Loader.loader.createFloatBufferFromData(FLOAT_DATA).remaining(), FLOAT_DATA.length);
    }

    public void shouldCreateIntBufferWithTheSameSize() {
        assertEquals(Loader.loader.createIntBufferFromData(INT_DATA).remaining(), INT_DATA.length);
    }
}
