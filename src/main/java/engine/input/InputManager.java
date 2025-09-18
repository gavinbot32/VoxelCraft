package engine.input;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {
    private final boolean[] keys = new boolean[GLFW_KEY_LAST];
    private final boolean[] mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private double mouseX, mouseY;
    private double scrollX, scrollY;
    private InputFrame frame;

    public InputManager(){
        frame = new InputFrame(this);
    }

    public void keyCallBack(long window, int key, int scancode, int action, int mods){
        if(key >= 0 && key < keys.length){
            keys[key] = action != GLFW_RELEASE;
        }
    }

    public void mouseButtonCallback(long window, int button, int action, int mods){
        if(button >= 0 && button < mouseButtons.length){
            mouseButtons[button] = action != GLFW_RELEASE;
        }
    }

    public void cursorPosCallback(long window, double xPos, double yPos){
        mouseX = xPos;
        mouseY = yPos;
    }

    public void scrollCallback(long window, double xOffset, double yOffset){
        scrollX += xOffset;
        scrollY += yOffset;
    }

    public void update(){
        frame.update();
    }

    // --- Accessors ---
    public boolean isKeyDown(int key){return keys[key];}
    public boolean isMouseDown(int button){return mouseButtons[button];}
    public boolean isKeyJustPressed(int key){
        return frame.isKeyJustPressed(key);
    }

    public boolean isKeyJustReleased(int key){
        return frame.isKeyJustReleased(key);
    }
    public double getMouseX() {return mouseX;}
    public double getMouseY() {return mouseY;}
    public double consumeScrollY(){
        double y = scrollY;
        scrollY = 0;
        return y;
    }
}
