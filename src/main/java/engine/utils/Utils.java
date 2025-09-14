package engine.utils;

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
}
