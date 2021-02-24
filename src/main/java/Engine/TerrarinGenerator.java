package Engine;

import Components.Chunk;
import org.joml.Vector2f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TerrarinGenerator extends Thread implements Runnable, Serializable {
    List<Vector2f> queue = new ArrayList<>();
    boolean running = false, inLoop = false;

    public void kill() {
        if(running) {
            while (running) {
                running = false;
                currentThread().interrupt();
                System.out.println("Attempting to stop terrain thread");
            }
            System.out.println("Terrain thread ready to stop");
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void run() {
        running = true;
        inLoop = true;
        GameScene instance = GameScene.instance;
        System.out.println("Terrain thread started");
        while(true) {
            if(queue.size() > 0) {
                generateTerrainChunk(instance, queue.get(0));
                queue.remove(0);
            }

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!isRunning()) {
                break;
            }
        }
        inLoop = false;
        System.out.println("Terrain thread stopped");
    }

    public void addToQueue(Vector2f chunkCoords) {
        queue.add(chunkCoords);
    }

    public void generateTerrainChunk(GameScene instance, Vector2f chunkCoords) {
        int width = instance.chunkWidth;
        int height = instance.chunkHeight;
        Vector2f offset = new Vector2f(chunkCoords.x * width, chunkCoords.y * height);
        float[] values;

        OpenSimplexNoise noise = new OpenSimplexNoise();
        GameObject go;
        values = new float[width * height];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double value = noise.eval((double)((x + (int) offset.x) * (1f / instance.res)), (double)((y + (int) offset.y) * (1f / instance.res)), instance.seed);
                values[x + y * width] = (float)value;

            }
        }
        go = new GameObject("Tile");
        go.transform.position = new Vector2f(offset.x * 10, offset.y * 10);
        go.transform.scale = new Vector2f(320, 320);
        go.addComponent(new Chunk());
        go.getComponent(Chunk.class).setAllValues(values);
        GameScene.acceptGeneratedTerrain(go);
    }
}
