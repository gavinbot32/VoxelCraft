package engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;

public class InputFrame {
    private final boolean[] lastKeys = new boolean[GLFW_KEY_LAST];
    private final InputManager input;
    public InputFrame(InputManager input){this.input = input;}

    public void update(){
        for(int i = 0; i < lastKeys.length; i++){
            lastKeys[i] = input.isKeyDown(i);
        }
    }

    public boolean isKeyJustPressed(int key){
        return input.isKeyDown(key) && !lastKeys[key];
    }

    public boolean isKeyJustReleased(int key){
        return !input.isKeyDown(key) && lastKeys[key];
    }
}
