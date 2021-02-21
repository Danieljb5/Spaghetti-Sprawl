package Content;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentLoader {
    public static String[] loadFromFile(String path) {
        File file = new File(path);
        try {
            String[] fileLines = Files.readAllLines(Paths.get(file.getAbsolutePath())).toArray(new String[0]);
            return fileLines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String[]> loadAllContent() {
        File contentDir = new File("assets/content");
        List<File> files = new ArrayList<>();
        String[] dirs = contentDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        File[] firstFiles = contentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        for(int i = 0; i < firstFiles.length; i++) {
            files.add(firstFiles[i]);
        }
        for(int i = 0; i < dirs.length; i++) {
            File[] childDirFiles = loadAllChildren(dirs[i]);
            for(int j = 0; j < childDirFiles.length; j++) {
                files.add(childDirFiles[j]);
            }
        }
        List<String[]> fileContent = new ArrayList<>();
        for(int i = 0; i < files.size(); i++) {
            String[] content = new String[0];
            try {
                content = Files.readAllLines(Paths.get(files.get(i).getAbsolutePath())).toArray(new String[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileContent.add(content);
        }
        return fileContent;
    }

    private static File[] loadAllChildren(String dir) {
        File cDir = new File(dir);
        List<File> files = Arrays.asList(cDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        }));
        String[] dirs = cDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        if(dirs.length != 0) {
            for(int i = 0; i < dirs.length; i++) {
                File[] dirFiles = loadAllChildren(dirs[i]);
                for(int j = 0; j < dirFiles.length; j++) {
                    files.add(new File(String.valueOf(dirFiles[i])));
                }
            }
        }
        return files.toArray(new File[0]);
    }
}
