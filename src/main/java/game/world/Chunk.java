package game.world;

import engine.graph.ChunkMesh;
import engine.graph.ChunkMesher;
import engine.graph.TextureArray;
import engine.scene.Entity;
import engine.scene.Transform;
import org.joml.Random;
import org.joml.Vector3f;


public class Chunk {
    public static final int CHUNK_X = 16, CHUNK_Z = 16, CHUNK_Y = 256;
    private final Vector3f chunkOffset = new Vector3f(0,0,0);

    private final int cx, cz;
    private World world;
    byte[][][] blocks;
    private ChunkMesh mesh;
    private Transform transform;
    private String chunkId;

    private TextureArray blockTextures;
    public Chunk(int cx, int cz){
        this.cx = cx;
        this.cz = cz;
        chunkId = "chunk-"+cx+"_"+cz;
        transform = new Transform(chunkId);
        Vector3f pos = new Vector3f(cx*CHUNK_X,0,cz*CHUNK_Z).add(chunkOffset);
        transform.setPosition(pos);
        transform.updateModelMatrix();
        System.out.println(chunkId+": "+transform.getPosition().x+","+transform.getPosition().y+","+transform.getPosition().z+"-"+transform.getScale());
        generate();
    }

    public void init(){

    }

    public Transform getTransform() {
        return transform;
    }

    public void cleanup(){
        if(mesh != null){
            mesh.cleanup();
        }
    }

    public void buildMesh(TextureArray blockTextures){
        cleanup();
        this.blockTextures = blockTextures;
        this.mesh = ChunkMesher.build(this, blockTextures);
    }

    public void buildMesh(){
        cleanup();
        this.mesh = ChunkMesher.build(this, blockTextures);
    }


    public void generate(){
        Random rnd = new Random();

        int[] CHUNK_SIZE = {CHUNK_X,CHUNK_Y,CHUNK_Z};
        int GROUND_LEVEL = 128;
        blocks = new byte[CHUNK_SIZE[0]][CHUNK_SIZE[1]][CHUNK_SIZE[2]];

        int[][] heightMap = generateHeightmap(cx,cz, 1.23, 0.45);

        for(int x = 0; x < CHUNK_SIZE[0]; x++){
            for(int z = 0; z < CHUNK_SIZE[2]; z++){

                int yMax = heightMap[x][z];
                if(cx == 0 || cz == 0){
                    yMax += rnd.nextInt(3)-1;
                }
                if(cx == 0 && cz == 0){
                    yMax = 128;
                }
                for(int y = 0; y < yMax; y++){
                    byte blockId = 0; //Air
                    if(y == yMax-1){
                        blockId = 1; //Grass
                    }
                    if(y < yMax-1){
                        blockId = 2;//Dirt
                    }
                    if(y < yMax - 5){
                        blockId = 3; //Stone
                    }

                    blocks[x][y][z] = blockId;
                }
            }
        }

    }

    public void setBlock(int x, int y, int z, byte blockId){
        if(x < 0 || x > CHUNK_X-1){
            return;
        }
        if(y < 0 || y > CHUNK_Y-1){
            return;
        }
        if(z < 0 || z > CHUNK_Z-1){
            return;
        }
        blocks[x][y][z] = blockId;
        buildMesh();
    }
    public ChunkMesh getMesh() {
        return mesh;
    }

    public byte[][][] getBlocks() {
        return blocks;
    }
    public byte getBlock(int x, int y, int z){
        return blocks[x][y][z];
    }


    /**
     * Generates a heightmap for a chunk using sine waves.
     *
     * @param chunkX     chunk coordinate (x-axis)
     * @param chunkZ     chunk coordinate (z-axis)
     * @param amplitude  vertical size of terrain variation
     * @param frequency  horizontal stretch of the wave (smaller = wider hills)
     * @return 2D heightmap [x][z] with values between 0 and CHUNK_HEIGHT-1
     */
    public static int[][] generateHeightmap(int chunkX, int chunkZ,
                                            double amplitude, double frequency) {
        int[][] heightmap = new int[CHUNK_X][CHUNK_Z];

        // centerline around half of world height
        double baseHeight = (double) CHUNK_Y / 2;

        for (int x = 0; x < CHUNK_X; x++) {
            for (int z = 0; z < CHUNK_Z; z++) {

                // World coordinates so chunks tile seamlessly
                int worldX = chunkX * CHUNK_X + x;
                int worldZ = chunkZ * CHUNK_Z + z;

                // Simple 2D sine terrain
                double wave = Math.sin(worldX * frequency) +
                        Math.sin(worldZ * frequency);

                // Scale wave, shift to base height
                int height = (int) (baseHeight + wave * amplitude);

                // Clamp
                if (height < 0) height = 0;
                if (height >= CHUNK_Y) height = CHUNK_Y - 1;

                heightmap[x][z] = height;
            }
        }

        return heightmap;
    }

    public String getId() {
        return chunkId;
    }
}
