package engine.graph;

import engine.scene.Projection;
import engine.scene.Scene;
import game.world.World;

public interface RenderPipeline {
    void render(Scene scene);
    void render(World world, Scene scene);
    void cleanup();

}
