package experimental.matt.graphicsexp;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;

public class KeyHandler extends GLFWKeyCallback {
	
	public KeyHandler() {
		System.out.println("Key Handler Created");
		System.out.printf("Release: %d\n", GLFW.GLFW_RELEASE);
		System.out.printf("Pressed: %d\n", GLFW.GLFW_PRESS);
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
			GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
		
		if ( action == GLFW.GLFW_RELEASE) {
			switch (key) {
			case GLFW.GLFW_KEY_DOWN:
			case GLFW.GLFW_KEY_A:
				System.out.println("Down");
				break;
			case GLFW.GLFW_KEY_UP:
			case GLFW.GLFW_KEY_W:
				System.out.println("Up");
				break;
			case GLFW.GLFW_KEY_RIGHT:
			case GLFW.GLFW_KEY_D:
				System.out.println("Right");
				break;
			case GLFW.GLFW_KEY_LEFT:
			case GLFW.GLFW_KEY_L:
				System.out.println("Left");
				break;
			}
		}
	}

}
