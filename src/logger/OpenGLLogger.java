package logger;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class OpenGLLogger {

    public void printStats() {
        System.out.println("MAX_VERTEX_ATTRIBS : " + GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS) +
                " [max number of attribute lists per VAO, numbered 0 .. GL_MAX_VERTEX_ATTRIBS - 1]");
    }
}
