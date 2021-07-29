package env;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceEnv {
    private static ResourceEnv instance;
    public static ResourceEnv getInstance(){
        if (instance == null){
            instance = new ResourceEnv();
        }
        return instance;
    }
    private String[] possibleResourceDirs = {
        "WEB-INF/Classes",
        "src/main/resources",
        "C:\\Users\\Public\\_docs\\university\\module4\\programming\\webapp\\src\\main\\resources",
    };
    private File resourceDir = null;
    public Path getResourcePath(){
        var dir = this.getResourceDir();
        if (dir == null){return null;}
        return Paths.get(dir.getPath());
    }
    public File getResourceDir(){
        if (resourceDir != null){return resourceDir;}
        for (String dirName: possibleResourceDirs){
            var dir = new File(dirName);
            if (dir.isDirectory()){
                resourceDir = dir;
                return resourceDir;
            }
        }
        return null;
    }
}
