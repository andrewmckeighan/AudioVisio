package experimental.matt.graphicsexp;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseHandlers {
	public CursorHandler cursorHandler = new CursorHandler();
	public ScrollHandler scrollHandler = new ScrollHandler();
	public EnterExitHandler enterExitHandler = new EnterExitHandler();
	public MouseButtonHandler mouseButtonHandler = new MouseButtonHandler();
	
	public MouseHandlers(long window) {
		GLFW.glfwSetCursorPosCallback(window, cursorHandler);
		GLFW.glfwSetScrollCallback(window, scrollHandler);
		GLFW.glfwSetCursorEnterCallback(window, enterExitHandler);
		GLFW.glfwSetMouseButtonCallback(window, mouseButtonHandler);
	}
	
	public void release() {
		cursorHandler.release();
		scrollHandler.release();
		enterExitHandler.release();
		mouseButtonHandler.release();
	}
	
	public class MouseButtonHandler extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			if (action == GLFW.GLFW_RELEASE) {
				switch (button) {
				case GLFW.GLFW_MOUSE_BUTTON_1:
					System.out.println("Left Mouse");
					break;
				case GLFW.GLFW_MOUSE_BUTTON_2:
					System.out.println("Right Mouse");
					break;
				case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
					System.out.println("Middle Click");
					break;
				default:
					System.out.println("Deafult Mouse");
				}
			}
		}
	}
	
	public class CursorHandler extends GLFWCursorPosCallback {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			//System.out.printf("Pos> X: %f, Y: %f\n", xpos, ypos);
		}
	}
	
	public class ScrollHandler extends GLFWScrollCallback {
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			System.out.printf("Scroll> X: %f, Y: %f\n", xoffset, yoffset);
		}
	}
	
	public class EnterExitHandler extends GLFWCursorEnterCallback {
		@Override
		public void invoke(long window, int entered) {
			if (entered == 1)
				System.out.println("Entered");
			else
				System.out.println("Exited");
		}
	}
}
