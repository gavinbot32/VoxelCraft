package engine.graph;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int numVertices;
    private int vaoId;
    private List<Integer> vboIdList;

    public Mesh(float[] positions, float[] colors, int[] indices){
        numVertices = indices.length;
        vboIdList = new ArrayList<>();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Positions VBO
        int vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer positionsBuffer = MemoryUtil.memCallocFloat(positions.length);
        positionsBuffer.put(0, positions);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER,positionsBuffer,GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3, GL_FLOAT, false, 0 ,0);

        //Color VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer colorsBuffer = MemoryUtil.memCallocFloat(colors.length);
        colorsBuffer.put(0, colors);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // Index VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = MemoryUtil.memCallocInt(indices.length);
        indicesBuffer.put(0, indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        MemoryUtil.memFree(positionsBuffer);
        MemoryUtil.memFree(colorsBuffer);
        MemoryUtil.memFree(indicesBuffer);


        //Unbind VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

    public void cleanup(){
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVaoId() {
        return vaoId;
    }
}
