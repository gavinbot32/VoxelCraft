package engine.scene;

import engine.graph.Mesh;
import engine.graph.Model;
import engine.graph.Texture;
import engine.graph.TextureCache;

import java.util.HashMap;
import java.util.Map;

public class Scene {

    private TextureCache textureCache;
    private Map<String, Model> modelMap;
    private Projection projection;
    private Camera camera;

    public Scene(int width, int height){
        modelMap = new HashMap<>();
        projection = new Projection(width,height);
        textureCache = new TextureCache();
        camera = new Camera();
    }

    public void addEntity(Entity entity){
        String modelId = entity.getModelId();
        Model model = modelMap.get(modelId);
        if(model == null){
            throw new RuntimeException("Could not find model ["+modelId+"]");
        }
        model.getEntityList().add(entity);
    }

    public void addModel(Model model){
        modelMap.put(model.getId(),model);
    }

    public Projection getProjection() {
        return projection;
    }
    public void cleanup(){
        modelMap.values().forEach(Model::cleanup);
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }

    public void resize(int width, int height){
        projection.updateProjMatrix(width,height);
    }

    public TextureCache getTextureCache() {
        return textureCache;
    }

    public Camera getCamera() {
        return camera;
    }
}