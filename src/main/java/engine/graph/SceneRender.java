package engine.graph;

import engine.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SceneRender {

    ShaderProgram shaderProgram;
    private UniformMap uniformMap;

    public SceneRender(){
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();

        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/main/resources/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/main/resources/shaders/scene.frag", GL_FRAGMENT_SHADER));
        shaderProgram = new ShaderProgram(shaderModuleDataList);

        createUniforms();
    }

    public void cleanup(){
        shaderProgram.cleanup();
    }

    public void render(Scene scene){
        shaderProgram.bind();

        uniformMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());

        scene.getMeshMap().values().forEach(mesh -> {
                    glBindVertexArray(mesh.getVaoId());
                    glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                }
        );
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    public void createUniforms(){
        uniformMap = new UniformMap(shaderProgram.getProgramId());
        uniformMap.createUniform("projectionMatrix");
    }

}
