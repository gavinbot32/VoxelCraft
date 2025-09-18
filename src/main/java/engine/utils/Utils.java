package engine.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
    private Utils(){

    }

    public static String readFile(String filePath){
        String str;
        try{
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch(IOException excep){
            throw new RuntimeException("Error reading file ["+filePath+"]", excep);
        }
        return str;
    }

    public static Vector3f addVec3fVec4f(Vector3f vec3, Vector4f vec4){
        return new Vector3f(vec3.x + vec4.x, vec3.y + vec4.y, vec3.z + vec4.z);
    }

    public static int floorMod(int value, int mod) {
        int result = value % mod;
        return result < 0 ? result + mod : result;
    }
}
