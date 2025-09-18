package engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ChunkMesh extends Mesh{

    private int texLayerVboId;

    public ChunkMesh(float[] positions, float[] textCoords, float[] texLayers,int[] indices){
        super(positions,textCoords,indices);

        glBindVertexArray(getVaoId()); // rebind VAO from Mesh

        texLayerVboId = glGenBuffers();
        FloatBuffer texLayerBuffer = MemoryUtil.memCallocFloat(texLayers.length);
        texLayerBuffer.put(0, texLayers);

        glBindBuffer(GL_ARRAY_BUFFER, texLayerVboId);
        glBufferData(GL_ARRAY_BUFFER, texLayerBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(2); // new attribute
        glVertexAttribPointer(2, 1, GL_FLOAT, false, 0, 0);

        MemoryUtil.memFree(texLayerBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0); // unbind again when done
    }

    @Override
    public void cleanup() {
        super.cleanup();
        glDeleteBuffers(texLayerVboId);
    }
}
