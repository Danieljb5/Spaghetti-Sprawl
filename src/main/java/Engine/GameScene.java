package Engine;

import Utilities.AssetPool;
import org.joml.Vector2f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene implements Serializable {

    private String saveGame = "Test";
    static GameScene instance;
    GameObject[] gos = new GameObject[0];
    int chunkWidth = 32, chunkHeight = 32;
    int screenWidth = 190, screenHeight = 130;
    float res = 50f;
    float seed = 0;
    float autoSaveTimer = 0.0f;
    float moveSpeed = 200.0f;
    Vector2f playerPos = new Vector2f();
    Vector2f playerPosOld = null;
    List<Vector2f> currentOnScreenChunks = new ArrayList<>();
    float clearTimer = 0f;
    Thread terrainThread;
    TerrarinGenerator terrarinGenerator;
    List<GameObject> acceptQueue = new ArrayList<>();

    public GameScene() {
        instance = this;
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f());

        Save save = SaveSystem.load(saveGame);
        if(save != null) {
            set(save);
        } else {
            seed = (float) (Math.random() * 20000f) - 10000f;
        }
        terrarinGenerator = new TerrarinGenerator();
        terrainThread = new Thread(terrarinGenerator);
        terrainThread.start();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    public static void acceptGeneratedTerrain(GameObject[] terrain) {
        for(int i = 0; i < terrain.length; i++) {
            instance.acceptQueue.add(terrain[i]);
        }
    }

    private Vector2f[] calculateOnScreenChunks(Vector2f playerPos) {
        int minX = (int)Math.floor((playerPos.x) / chunkWidth);
        int minY = (int)Math.floor(playerPos.y / screenHeight);
        int maxX = (int)Math.ceil((playerPos.x + screenWidth) / chunkWidth);
        int maxY = (int)Math.ceil((playerPos.y + screenHeight) / chunkHeight);

        List<Vector2f> tmp = new ArrayList<>();
        for(int x = minX; x <= maxX; x++) {
            for(int y = minY; y <= maxY; y++) {
                if(!currentOnScreenChunks.contains(new Vector2f(x, y))) {
                    tmp.add(new Vector2f(x, y));
                    currentOnScreenChunks.add(new Vector2f(x, y));
                }
            }
        }
        return tmp.toArray(new Vector2f[0]);
    }

    private boolean isOnScreen(Vector2f chunkPos) {
        int minX = (int)Math.floor(chunkPos.x * chunkWidth);
        int minY = (int)Math.floor(chunkPos.y * chunkHeight);
        int maxX = (int)Math.ceil((chunkPos.x + chunkWidth) * chunkWidth);
        int maxY = (int)Math.ceil((chunkPos.y + chunkHeight) * chunkHeight);
        if(minX <= screenWidth && maxX >= 0 && minY <= screenHeight && maxY >= 0) {
            return true;
        }
        return false;
    }

    private void unloadAcceptQueue() {
        int i = 0;
        while(instance.acceptQueue.size() > 0) {
            if(instance.acceptQueue.get(0) != null) {
                if(!this.gameObjects.contains(instance.acceptQueue.get(0)));
                {
                    addGameObjectToScene(instance.acceptQueue.get(0));
                }
            }
            instance.acceptQueue.remove(0);
            if(i > 998) {
                break;
            }
            i++;
        }
    }

    @Override
    public void update(float dt) {
        unloadAcceptQueue();

        for(int i = 0; i < this.gameObjects.size(); i++) {
            this.gameObjects.get(i).update(dt);
        }

        clearTimer += dt;

        if(clearTimer >= 1.0f) {
            clearTimer = 0.0f;
            for(int i = 0; i < currentOnScreenChunks.size(); i++) {
                if(!isOnScreen(currentOnScreenChunks.get(i))) {
                    currentOnScreenChunks.remove(i);
                    i--;
                }
            }
        }

        if(!playerPos.equals(playerPosOld)) {
            Vector2f[] onScreenChunks = calculateOnScreenChunks(new Vector2f(playerPos.x - 30, playerPos.y - 30));
            for(int i = 0; i < onScreenChunks.length; i++) {
                terrarinGenerator.addToQueue(onScreenChunks[i]);
            }
        }

        playerPosOld = new Vector2f(playerPos.x, playerPos.y);

        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            exit();
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_W)) {
            playerPos.y += moveSpeed * dt;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_S)) {
            playerPos.y -= moveSpeed * dt;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_D)) {
            playerPos.x += moveSpeed * dt;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_A)) {
            playerPos.x -= moveSpeed * dt;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_R)) {
            playerPos = new Vector2f();
        }

        this.camera().position = playerPos;

        autoSaveTimer += dt;
        if(autoSaveTimer >= 600f) {
            save();
        }

        this.renderer.render();
    }

    public void exit() {
        for(int i = 0; i < gos.length; i++) {
            this.gameObjects.remove(gos[i]);
        }
        SaveSystem.save(saveGame, this);
        terrarinGenerator.kill();
        Window.changeScene(0);
    }

    public void save() {
        for(int i = 0; i < gos.length; i++) {
            this.gameObjects.remove(gos[i]);
        }
        SaveSystem.save(saveGame, this);
        for(int i = 0; i < gos.length; i++) {
            this.addGameObjectToScene(gos[i]);
        }
    }

    public void set(Save save) {
        GameScene instance = save.getInstance();
        this.instance = instance;
        this.seed = instance.seed;
        this.gameObjects = instance.gameObjects;
        this.playerPos = new Vector2f();
        this.autoSaveTimer = instance.autoSaveTimer;
    }
}
