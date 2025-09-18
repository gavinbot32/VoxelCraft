package engine.graph;

import game.world.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkMesher {
    // Offsets for the 6 neighbor directions
    private static final int[][] NEIGHBORS = {
            { 0,  0, -1}, // -Z (back)
            { 0,  0,  1}, // +Z (front)
            {-1,  0,  0}, // -X (left)
            { 1,  0,  0}, // +X (right)
            { 0, -1,  0}, // -Y (down)
            { 0,  1,  0}  // +Y (up)
    };

    // Vertices of a unit cube face for each direction
// Each entry has 4 vertices (x,y,z)
    private static final float[][] FACE_VERTS = {
            // -Z
            {0,0,0,  1,0,0,  1,1,0,  0,1,0},
            // +Z
            {0,0,1,  1,0,1,  1,1,1,  0,1,1},
            // -X
            {0,0,0,  0,0,1,  0,1,1,  0,1,0},
            // +X
            {1,0,0,  1,0,1,  1,1,1,  1,1,0},
            // -Y
            {0,0,0,  1,0,0,  1,0,1,  0,0,1},
            // +Y
            {0,1,0,  1,1,0,  1,1,1,  0,1,1}
    };

    // Indices (2 triangles per face)
    private static final int[] FACE_INDICES = {0,1,2, 0,2,3};

    // UVs (always the same for a quad)
    private static final float[] FACE_UVS = {
            0,0,  1,0,  1,1,  0,1
    };
    public static ChunkMesh build(Chunk chunk, TextureArray textureArray){
        List<Float> positions = new ArrayList<>();
        List<Float> texCoords = new ArrayList<>();
        List<Float> texLayers = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for(int x = 0; x < Chunk.CHUNK_X; x++){
            for(int z = 0; z < Chunk.CHUNK_Z; z++){
                for(int y = 0; y < Chunk.CHUNK_Y; y++){
                    byte id = chunk.getBlock(x,y,z);
                    if(id == 0) continue; //Air
                    for(int face = 0; face < 6; face++){
                        int nx = x+ NEIGHBORS[face][0];
                        int ny = y + NEIGHBORS[face][1];
                        int nz = z + NEIGHBORS[face][2];

                        boolean visible = false;
                        if(nx < 0 || nx >= Chunk.CHUNK_X ||
                           ny < 0 || ny >= Chunk.CHUNK_Y ||
                           nz < 0 || nz >= Chunk.CHUNK_Z){
                            visible = true;
                        }else if(chunk.getBlock(nx,ny,nz) == 0){
                            visible = true;
                        }
                        if(visible){
                            int vertOffset = positions.size() / 3;

                            float[] faceVerts = FACE_VERTS[face];
                            for(int i = 0; i < 12; i+= 3){
                                positions.add(x + faceVerts[i]);
                                positions.add(y + faceVerts[i+1]);
                                positions.add(z+faceVerts[i+2]);
                            }
                            for(int i = 0; i < 8; i++){
                                texCoords.add(FACE_UVS[i]);
                            }
                            for(int i=0;i<4;i++){
                                texLayers.add((float)id+-1);
                            }

                            for(int i=0; i < FACE_INDICES.length; i++){
                                indices.add(vertOffset + FACE_INDICES[i]);
                            }
                        }
                    }
                }
            }
        }
        return new ChunkMesh(toArray(positions), toArray(texCoords), toArray(texLayers), toIntArray(indices));
    }

    public static float[] toArray(List<Float> list){
        float[] ret = new float[list.size()];
        for(int i = 0; i < ret.length; i++){
            ret[i] = list.get(i);
        }
        return ret;
    }
    public static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0; i < ret.length; i++){
            ret[i] = list.get(i);
        }
        return ret;
    }
}
