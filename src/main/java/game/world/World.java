package game.world;

import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class World {
    final private int[] CHUNK_SIZE = {16,256,16};
    private final Map<String, Chunk> chunks;

    public World(){
        chunks = new HashMap<>();
    }

    public void addChunk(int cx, int cz, Chunk chunk){
        chunks.put(key(cx,cz), chunk);
    }

    public Chunk getChunk(int cx,int cz){
        return chunks.get(key(cx,cz));
    }

    public Iterable<Chunk> getLoadedChunks(){
        return chunks.values();
    }

    public void init(){

    }

    public void cleanup(){
        for(Chunk chunk : chunks.values()){
            chunk.cleanup();
        }
    }

    public int[] getCHUNK_SIZE() {
        return CHUNK_SIZE;
    }

    private String key(int cx, int cz){
        return cx + "," + cz;
    }
}
