package engine.graph;

import engine.scene.Entity;
import engine.scene.Projection;
import engine.scene.Scene;
import game.world.Chunk;
import game.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ChunkRender implements RenderPipeline{

    ShaderProgram shaderProgram;
    private UniformMap uniformMap;
    private TextureArray blockTextureArray;

    public ChunkRender(TextureArray blockTextureArray){
        this.blockTextureArray = blockTextureArray;

        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();

        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/main/resources/shaders/chunk.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("src/main/resources/shaders/chunk.frag", GL_FRAGMENT_SHADER));
        shaderProgram = new ShaderProgram(shaderModuleDataList);

        createUniforms();
    }

    @Override
    public void render(Scene scene) {

    }

    @Override
    public void render(World world, Scene scene) {
        shaderProgram.bind();

        uniformMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
        uniformMap.setUniform("viewMatrix", scene.getCamera().getViewMatrix());
        uniformMap.setUniform("texArray",0);

        glActiveTexture(GL_TEXTURE0);
        blockTextureArray.bind();

        for(Chunk chunk : world.getLoadedChunks()){
            ChunkMesh mesh = chunk.getMesh();
            if(mesh == null) continue;
            glBindVertexArray(mesh.getVaoId());
            uniformMap.setUniform("modelMatrix", chunk.getTransform().getModelMatrix());
            glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
        }

        glBindVertexArray(0);
        shaderProgram.unbind();

    }

    private void createUniforms() {
        uniformMap = new UniformMap(shaderProgram.getProgramId());
        uniformMap.createUniform("projectionMatrix");
        uniformMap.createUniform("modelMatrix");
        uniformMap.createUniform("texArray");
        uniformMap.createUniform("viewMatrix");
    }

    @Override
    public void cleanup() {
        shaderProgram.cleanup();
        blockTextureArray.cleanup();
    }
}
