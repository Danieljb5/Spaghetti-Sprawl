package Engine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveSystem {
    public static void save(String saveGame, float seed, GameObject[] gos) {
        File savesDir = new File("saves");
        if(!savesDir.exists()) {
            savesDir.mkdir();
        }
        File saveDir = new File("saves/" + saveGame);
        if(!saveDir.exists()) {
            saveDir.mkdir();
        }
        try {
            FileWriter writer = new FileWriter("saves/" + saveGame + "/seed.dat");
            writer.write("SEED:" + seed);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < gos.length; i++) {
            try (FileOutputStream fos = new FileOutputStream("saves/" + saveGame + "/" + gos[i].id() + ".dat");
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(gos[i]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Save load(String saveGame) {
        File saveDir = new File("saves");
        if(!saveDir.exists()) {
            saveDir.mkdir();
        }

        File file = new File("saves/" + saveGame);
        if(file.exists()) {
            try {
                String source = new String(Files.readAllBytes(Paths.get("saves/" + saveGame + "/seeds.dat")));
                String[] splitString = source.split("(SEED:)( )+([a-zA-Z]+)");
                int index = source.indexOf("SEED:") + 5;
                int eol = source.indexOf("\n", index);
                String seedSrc = source.substring(index, eol).trim();
                float seed = Float.parseFloat(seedSrc);

                System.out.println(seed);
                Save save = new Save(seed, new GameObject[0]);
                return save;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
