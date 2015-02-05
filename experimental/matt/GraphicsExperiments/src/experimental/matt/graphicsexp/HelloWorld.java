/**
 * Adapted from LWJGL's getting started guide:
 * http://www.lwjgl.org/guide
 */
package experimental.matt.graphicsexp;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class HelloWorld {

	private GLFWErrorCallback errorCallback;
	private KeyHandler keyCallback;
	private MouseHandlers mouseHandlers;
	
	private long window;
	
	public void run() {
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
		
		try {
			init();
			loop();
			
			GLFW.glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			GLFW.glfwTerminate();
			errorCallback.release();
		}
	}
	
	private void init() {
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		
		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
		int WIDTH = 640;
		int HEIGHT = 480;
		
		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		keyCallback = new KeyHandler();
		mouseHandlers = new MouseHandlers(window);
		
		glfwSetKeyCallback(window, keyCallback);
		
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
				window,
				(GLFWvidmode.width(vidmode) - WIDTH)/2,
				(GLFWvidmode.height(vidmode) - HEIGHT)/2
		);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		glfwShowWindow(window);
	}
	
	private void loop() {
		GLContext.createFromCurrent();
		
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		
		float ratio;
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		
		while ( glfwWindowShouldClose(window) == GL_FALSE ) {
			
			glfwGetFramebufferSize(window, width, height);
			ratio = width.get() / (float) height.get();
			
			width.rewind();
			height.rewind();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(-ratio, ratio, -1f, 1f, 1f, -1f);
			glMatrixMode(GL_MODELVIEW);
			
			glLoadIdentity();
			glRotatef((float) glfwGetTime() * 50f, 0f, 0f, 1f);
			
			glBegin(GL_TRIANGLES);
			glColor3f(1f, 0f, 0f);
			glVertex3f(-0.6f, -0.4f, 0f);
			glColor3f(0f, 1f, 0f);
			glVertex3f(0.6f, -0.4f, 0f);
			glColor3f(0f, 0f, 1f);
			glVertex3f(0f, 0.6f, 0f);
			glEnd();
			
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}
	
	public static void main(String[] args) {
		new HelloWorld().run();
	}
}
