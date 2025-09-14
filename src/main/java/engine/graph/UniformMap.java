package engine.graph;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class UniformMap {
    private int programId;
    private Map<String, Integer> uniforms;

    public UniformMap(int programId){
        this.programId = programId;
        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName){
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if(uniformLocation < 0){
            throw new RuntimeException("Could not find uniform [" + uniformName + "] in shader program [" +
                    programId + "]");
        }
        uniforms.put(uniformName,uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()){
            Integer location = uniforms.get(uniformName);
            if (location == null) {
                throw new RuntimeException("Could not find uniform [" + uniformName + "]");
            }
            glUniformMatrix4fv(location, false, value.get(stack.callocFloat(16)));
        }
    }

}
