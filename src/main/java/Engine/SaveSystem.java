package Engine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveSystem {
    public static void saveAndExit(String saveGame, GameScene instance) {
        File savesDir = new File("saves");
        if(!savesDir.exists()) {
            savesDir.mkdir();
        }
        File saveDir = new File("saves/" + saveGame);
        if(!saveDir.exists()) {
            saveDir.mkdir();
        }
        try (FileOutputStream fos = new FileOutputStream("saves/" + saveGame + "/sceneInstance.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(instance);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Save load(String saveGame) {
        File saveDir = new File("saves");
        if(!saveDir.exists()) {
            saveDir.mkdir();
        }

        File file = new File("saves/" + saveGame + "/");
        if(!file.exists()) {
            return null;
        }
        try {
            byte[] sceneInstanceBytes = Files.readAllBytes(Paths.get("saves/" + saveGame + "/sceneInstance.dat"));
            ByteArrayInputStream in = new ByteArrayInputStream(sceneInstanceBytes);
            ObjectInputStream is = new ObjectInputStream(in);
            GameScene instance = (GameScene)is.readObject();

            File saveDirectory = new File("saves/" + saveGame);
            String[] saveData = saveDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });
            GameObject[] gos = new GameObject[saveData.length];
            for(int i = 0; i < saveData.length; i++) {
                byte[] goBytes = Files.readAllBytes(Paths.get("saves/" + saveGame + "/" + saveData[i]));
                in = new ByteArrayInputStream(goBytes);
                is = new ObjectInputStream(in);
                gos[i] = (GameObject)is.readObject();
            }

            Save save = new Save(instance);
            return save;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
