package engine.graph;

import engine.constants.IOConst;
import game.Main;
import engine.IAppLogic;
import engine.Window;
import engine.scene.Scene;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Render {

    private SceneRender sceneRender;
    private ChunkRender chunkRender;
    private IAppLogic appLogic;
    private Main main;
    private TextureArray textureArray;

    public Render(){
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        sceneRender = new SceneRender();
    }

    public Render(IAppLogic appLogic){
        this.appLogic = appLogic;
        this.main = (Main) appLogic;

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        defineTextures();


        sceneRender = new SceneRender();
        chunkRender = new ChunkRender(textureArray);
    }

    public void cleanup(){
        sceneRender.cleanup();
    }

    public void render(Window window, Scene scene){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glViewport(0,0, window.getWidth(), window.getHeight());

        sceneRender.render(scene);
        chunkRender.render(main.world, scene);
    }

    private void defineTextures(){
        List<String> blockTextures = new ArrayList<>();
        String folder = IOConst.RESOURCES+"textures/blocks/";
        blockTextures.add(folder+"grass.png");
        blockTextures.add(folder+"dirt.png");
        blockTextures.add(folder+"stone.png");
        blockTextures.add(folder+"rock.png");

        textureArray = new TextureArray(blockTextures);

    }

    public TextureArray getTextureArray() {
        return textureArray;
    }
}
