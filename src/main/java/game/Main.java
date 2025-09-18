package game;

import engine.Engine;
import engine.IAppLogic;
import engine.Window;
import engine.constants.IOConst;
import engine.graph.*;
import engine.input.InputManager;
import engine.input.MouseInput;
import engine.scene.Camera;
import engine.scene.Entity;
import engine.scene.Scene;
import engine.scene.Transform;
import engine.utils.Utils;
import game.player.Player;
import game.world.Chunk;
import game.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static game.world.Chunk.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic {
    public World world;
    private float moveCooldown = 0.25f;
    private InputManager input;
    private MouseInput mouseInput;


    //--Entities
    private Player player;
    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("chapter-04", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        input = window.getInput();
        mouseInput = window.getMouseInput();

        world = new World();

        int distance = 8;
        int chunkCount = 0;

        for(int x = -distance; x < distance+1; x++){
            for(int z = -distance; z < distance+1; z++){
                Chunk chunk = new Chunk(x,z);
                chunk.buildMesh(render.getTextureArray());
                world.addChunk(x,z,chunk);
                chunkCount++;
            }
        }

        System.out.println(chunkCount);

        player = new Player(scene, world, input, mouseInput, new Vector3f(8, (CHUNK_Y/2)+5,8));

       /* Random rnd = new Random();
        chunkList = new ArrayList<>();
        float[] positions = new float[]{
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};
        Mesh cubeMesh = new Mesh(positions, textCoords, indices);

        //Get Texture
        Texture defaultTexture = scene.getTextureCache().createTexture(IOConst.DEFAULT_TEXTURE);

        //Create Material with Texture
        Material defaultMaterial = new Material();
        defaultMaterial.setTexturePath(defaultTexture.getTexturePath());

        //Add to material list.
        List<Material> defaultMaterialList = new ArrayList<>();
        defaultMaterialList.add(defaultMaterial);

        //Create the mesh and add it to the grass mesh list
        defaultMaterial.getMeshList().add(cubeMesh);

        //Create the model with the material list and add it to the scene.
        Model defaultModel = new Model("default-model", defaultMaterialList);
        scene.addModel(defaultModel);



        //Get Texture
        Texture grassTexture = scene.getTextureCache().createTexture(IOConst.RESOURCES+"/models/cube/grass_texture.png");

        //Create Material with Texture
        Material grassMaterial = new Material();
        grassMaterial.setTexturePath(grassTexture.getTexturePath());

        //Add to material list.
        List<Material> grassMaterialList = new ArrayList<>();
        grassMaterialList.add(grassMaterial);

        //Create the mesh and add it to the grass mesh list
        grassMaterial.getMeshList().add(cubeMesh);

        //Create the model with the material list and add it to the scene.
        Model grassModel = new Model("grass-model", grassMaterialList);
        scene.addModel(grassModel);


        //Dirt

        //Get Texture
        Texture dirtTexture = scene.getTextureCache().createTexture(IOConst.RESOURCES+"/models/cube/dirt_texture.png");

        //Create Material with Texture
        Material dirtMaterial = new Material();
        dirtMaterial.setTexturePath(dirtTexture.getTexturePath());

        //Add to material list.
        List<Material> dirtMaterialList = new ArrayList<>();
        dirtMaterialList.add(dirtMaterial);

        //Create the mesh and add it to the grass mesh list
        dirtMaterial.getMeshList().add(cubeMesh);

        //Create the model with the material list and add it to the scene.
        Model dirtModel = new Model("dirt-model", dirtMaterialList);
        scene.addModel(dirtModel);

        Vector3f chunkOffset = new Vector3f(0,-256,-20);

        int[][] heightMap = generateHeightmap(0,0, 1.23, 0.45);

        int blockId = 0;
        for(int x = 0; x < 16; x++){
            for(int z = 0; z < 16; z++){
                // int yMax = 251 + rnd.nextInt(5)+5;
                int yMax = heightMap[x][z];
                for(int y = 0; y < yMax; y++){
                    Model model = dirtModel;
                    if(y == yMax-1){
                        model = grassModel;
                    }
                    if(y == 256){
                        model = defaultModel;
                    }
                    Entity block = new Entity("block-"+blockId, model.getId());
                    block.setPosition(x+chunkOffset.x,y+chunkOffset.y,z+chunkOffset.z);
                    chunkList.add(block);
                    scene.addEntity(block);
                    block.updateModelMatrix();
                }
            }
        }
*/
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        player.input(diffTimeMillis);
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
       player.update(diffTimeMillis);


    }


}
