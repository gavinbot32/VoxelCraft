package engine.graph;

import engine.constants.IOConst;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    public static final String DEFAULT_TEXTURE = IOConst.DEFAULT_TEXTURE;
    private Map<String, Texture> textureMap;

    public TextureCache(){
        textureMap = new HashMap<>();
        textureMap.put(DEFAULT_TEXTURE, new Texture(DEFAULT_TEXTURE));
    }

    public void cleanup(){
        textureMap.values().forEach(Texture::cleanup);
    }

    public Texture createTexture(String texturePath){
        return textureMap.computeIfAbsent(texturePath, Texture::new);
    }

    public Texture getTexture(String texturePath){
        Texture texture = null;
        try {
            if(texturePath != null){
                texture = textureMap.get(texturePath);
            }else{
                throw new RuntimeException("Unable to getTexture : parameter 'texturePath' cannot be null");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(texture == null){
            texture = textureMap.get(DEFAULT_TEXTURE);
        }
        return texture;
    }


}
