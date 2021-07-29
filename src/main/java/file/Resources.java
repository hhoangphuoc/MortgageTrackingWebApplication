package file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.*;

import env.ResourceEnv;

public class Resources {
    private static Resources instance = new Resources();
    public static Resources getInstance(){
        return instance;
    }

    Path resourcePath;
    Map<Path, String> texts = new ConcurrentHashMap<>();
    public Resources(){
        this.resourcePath = ResourceEnv.getInstance().getResourcePath();
    }
    public Resources(Path resourcePath){
        this.resourcePath = resourcePath;
    }
    public String readText(String path) throws IOException{
        return this.readTextAbs(this.getFullPath(path));
    }
    public String readText(Path path) throws IOException{
        return this.readTextAbs(this.getFullPath(path));
    }
    public String readTextFile(Path path) throws IOException {
        return readTextAbs(this.getFullPath(path));
    }
    public String readTextFile(String path) throws IOException {
        return readTextAbs(this.getFullPath(path));
    }
    private Path getFullPath(String path){
        return this.resourcePath.resolve(path);
    }
    private Path getFullPath(Path path){
        return this.resourcePath.resolve(path);
    }
    private String readTextAbs(Path fullPath) throws IOException{
        String text = this.texts.get(fullPath);
        if (text == null){
            text = this.readTextFileAbs(fullPath);
            this.texts.put(fullPath, text);
        }
        return text;
    }
    private String readTextFileAbs(Path fullPath) throws IOException{
        return new String(Files.readAllBytes(fullPath));
    }
}
