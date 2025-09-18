package game.player;

import engine.input.InputManager;
import engine.input.MouseInput;
import engine.scene.Camera;
import engine.scene.Scene;
import engine.scene.Transform;
import game.world.Chunk;
import game.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import static engine.utils.Utils.floorMod;
import static game.world.Chunk.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;

public class Player {
    private final Camera camera;
    private final World world;
    private Chunk currentChunk;
    private final InputManager input;
    private final MouseInput mouseInput;


    private static final float MOVEMENT_SPEED = 0.005f;
    private static final float MOUSE_SENSITIVITY = 0.1f;
    private byte currentBlock = 1;
    private int playerHeight = 2;
    private boolean onGround;

    private float yVelocity = 0;

    private Vector3i curBlock = new Vector3i(0,0,0);

    public Player(Scene scene, World world, InputManager input, MouseInput mouseInput){
        this.world = world;
        this.input = input;
        this.mouseInput = mouseInput;
        this.camera = scene.getCamera();
    }

    public Player(Scene scene, World world, InputManager input, MouseInput mouseInput, Vector3f position){
        this(scene,world,input,mouseInput);
        camera.setPosition(position);
        currentChunk = getChunkFromPos(camera.getPosition());

    }

    public void update(long deltaTimeMillis){
        Vector3f position = camera.getPosition();
        currentChunk = getChunkFromPos(position);
        updateBlockPosition(position);
        physicsUpdate(deltaTimeMillis);



    }

    public void updateBlockPosition(Vector3f position){
        if(currentChunk == null) return;
        int bx,by,bz;
        bx = floorMod((int)position.x, CHUNK_X);
        by = floorMod((int)position.y, CHUNK_Y);
        bz = floorMod((int)position.z, CHUNK_Z);

        curBlock.set(bx,by,bz);

        System.out.println("Block: "+ curBlock.x+","+curBlock.y+","+curBlock.z);
        System.out.println("Chunk: "+ currentChunk.getId());
    }

    public void physicsUpdate(long deltaTimeMillis) {
        final float GRAVITY = 9.8f;
        float dt = deltaTimeMillis / 1000f; // seconds

        Vector3f pos = camera.getPosition();
        float nextY = pos.y + yVelocity * dt;

        // Get world-space block under the player’s feet
        int worldX = (int) Math.floor(pos.x);
        int worldZ = (int) Math.floor(pos.z);
        int feetY = (int) Math.floor(nextY - playerHeight);

        // Work with the current chunk (already stored each frame in update())
        if (currentChunk == null) return;

        // Convert to chunk-local coords
        int localX = Math.floorMod(worldX, CHUNK_X);
        int localY = Math.floorMod(feetY, CHUNK_Y);
        int localZ = Math.floorMod(worldZ, CHUNK_Z);

        byte blockBelow = currentChunk.getBlock(localX, localY, localZ);

        if (yVelocity <= 0 && blockBelow != 0) {
            // Top of that block in world coords
            float blockTop = feetY + 1.0f;
            if (nextY - playerHeight <= blockTop) {
                // Landed
                onGround = true;
                yVelocity = 0;
                nextY = blockTop + playerHeight;
                camera.setPosition(pos.x, nextY, pos.z);
                return;
            }
        }

        // Otherwise, keep moving and apply gravity
        yVelocity -= GRAVITY * dt;
        onGround = false;
        camera.setPosition(pos.x, nextY, pos.z);
    }

    public Chunk getChunkFromPos(Vector3f pos) {
        int cX = Math.floorDiv((int) pos.x, CHUNK_X);
        int cZ = Math.floorDiv((int) pos.z, CHUNK_Z);

        return world.getChunk(cX, cZ);
    }
    public void jump(long deltaTimeMillis){
        if(!onGround) return;
        yVelocity = 5f;
    }

    public void input(long deltaTimeMillis){
        //Camera Input
        float move = deltaTimeMillis * MOVEMENT_SPEED;
        if(input.isKeyDown(GLFW_KEY_W)) {
            camera.moveForwardFlat(move);
        } else if(input.isKeyDown(GLFW_KEY_S)){
            camera.moveBackwardFlat(move);
        }
        if(input.isKeyDown(GLFW_KEY_A)){
            camera.moveLeft(move);
        }
        else if(input.isKeyDown(GLFW_KEY_D)){
            camera.moveRight(move);
        }
        if(input.isKeyDown(GLFW_KEY_UP)){
            camera.moveUp(move);
        } else if( input.isKeyDown(GLFW_KEY_DOWN)){
            camera.moveDown(move);
        }
        if(input.isKeyDown(GLFW_KEY_SPACE)){
            jump(deltaTimeMillis);
        }

        if(mouseInput.isRightButtonPressed()){
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
        }

        // Misc Input
        if(input.isKeyDown(GLFW_KEY_C)){
            for(Chunk chunk : world.getLoadedChunks()){
                chunk.getTransform().setScale(1);
            }
        }

        if(input.isKeyJustReleased(GLFW_KEY_K)){
            currentChunk.setBlock(curBlock.x,curBlock.y-playerHeight, curBlock.z,(byte)0);
        }
        if(input.isKeyJustReleased(GLFW_KEY_J)){
           /* Chunk chunk = world.getChunk(0,0);
            int highestY = 0;
            for(int y = Chunk.CHUNK_Y-1; y > 0; y--){
                if(chunk.getBlock(9,y,9) != 0){
                    highestY = y;
                    break;
                }
            }*/
            currentChunk.setBlock(curBlock.x,curBlock.y-playerHeight, curBlock.z,currentBlock);
        }

        if(input.isKeyJustReleased(GLFW_KEY_H)){
            Chunk chunk = world.getChunk(0,0);
            int highestY = 0;
            for(int y = Chunk.CHUNK_Y-1; y > 0; y--){
                if(chunk.getBlock(9,y,9) != 0){
                    highestY = y;
                    break;
                }
            }
            chunk.setBlock(9,highestY-1,9,(byte)0);
        }

        if(input.isKeyDown(GLFW_KEY_1)){
            currentBlock = 1;
        }
        if(input.isKeyDown(GLFW_KEY_2)){
            currentBlock = 2;
        }
        if(input.isKeyDown(GLFW_KEY_3)){
            currentBlock = 3;
        }
        if(input.isKeyDown(GLFW_KEY_4)){
            currentBlock = 4;
        }
    }

    public void setPosition(Vector3f pos){
        camera.setPosition(pos);
    }

    public void setPosition(float x, float y, float z){
        camera.setPosition(x,y,z);
    }

    public Vector3f getPosition(){
        return camera.getPosition();
    }


}
