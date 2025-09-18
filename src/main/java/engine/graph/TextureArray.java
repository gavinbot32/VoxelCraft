package engine.graph;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.stb.STBImage.*;

public class TextureArray {

    private final int textureId;
    private final int width;
    private final int height;
    private final int layers;

    public TextureArray(List<String> texturePaths) {
        if (texturePaths == null || texturePaths.isEmpty()) {
            throw new IllegalArgumentException("Texture paths list is empty!");
        }

        this.layers = texturePaths.size();

        // Load the first image to determine width/height
        ByteBuffer firstBuf;
        int w, h;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuf = stack.mallocInt(1);
            IntBuffer heightBuf = stack.mallocInt(1);
            IntBuffer channelsBuf = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(false);
            firstBuf = stbi_load(texturePaths.get(0), widthBuf, heightBuf, channelsBuf, 4);
            if (firstBuf == null) {
                throw new RuntimeException("Failed to load first texture [" + texturePaths.get(0) + "]: " + stbi_failure_reason());
            }

            w = widthBuf.get(0);
            h = heightBuf.get(0);
        }

        this.width = w;
        this.height = h;

        // Create texture object
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureId);

        // Allocate immutable storage for all layers
        glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, GL_RGBA8, width, height, layers,
                0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        // Upload first layer
        glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0,
                width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, firstBuf);
        stbi_image_free(firstBuf);

        // Upload remaining layers
        for (int i = 0; i < layers; i++) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer widthBuf = stack.mallocInt(1);
                IntBuffer heightBuf = stack.mallocInt(1);
                IntBuffer channelsBuf = stack.mallocInt(1);

                ByteBuffer buf = stbi_load(texturePaths.get(i), widthBuf, heightBuf, channelsBuf, 4);
                if (buf == null) {
                    throw new RuntimeException("Failed to load texture [" + texturePaths.get(i) + "]: " + stbi_failure_reason());
                }

                int wCheck = widthBuf.get(0);
                int hCheck = heightBuf.get(0);
                if (wCheck != width || hCheck != height) {
                    stbi_image_free(buf);
                    throw new RuntimeException("Texture size mismatch in [" + texturePaths.get(i) +
                            "]. Expected " + width + "x" + height +
                            " but got " + wCheck + "x" + hCheck);
                }

                glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i,
                        width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, buf);
                stbi_image_free(buf);
            }
        }

        // Texture parameters
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glBindTexture(GL_TEXTURE_2D_ARRAY, 0);

        System.out.println("TextureArray loaded with " + layers + " layers (" + width + "x" + height + ")");
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureId);
    }

    public void cleanup(){
        glDeleteTextures(textureId);
    }

    public int getTextureId() {
        return textureId;
    }

    public int getLayerCount() {
        return layers;
    }
}
