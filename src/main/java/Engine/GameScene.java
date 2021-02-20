package Engine;

import Components.Tile;
import Utilities.AssetPool;
import org.joml.Vector2f;

import java.io.Serializable;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class GameScene extends Scene implements Serializable {

    private String saveGame = "Test";
    static GameScene instance;
    GameObject[] gos;
    Float[] values;
    int width = 150, height = 75;
    float res = 50f;
    float seed = 0;

    public GameScene() {
        instance = this;
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        loadResources();

        Save save = SaveSystem.load(saveGame);
        if(save != null) {
            seed = save.getInstance().seed;
        } else {
            seed = (float) Math.random() * 100f;
        }

        OpenSimplexNoise noise = new OpenSimplexNoise();
        gos = new GameObject[width * height];
        values = new Float[width * height];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double value = noise.eval((double)(x * (1f / res)), (double)(y * (1f / res)), seed);
                values[x + y * width] = (float)value;
                gos[x + y * width] = new GameObject("Tile");
                GameObject go = gos[x + y * width];
                go.transform.position = new Vector2f(x * 10, y * 10);
                go.transform.scale = new Vector2f(10, 10);
                go.addComponent(new Tile());
                go.getComponent(Tile.class).setValue((float)value);
                this.addGameObjectToScene(go);
            }
        }
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {
        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            exit();
        }

        this.renderer.render();
    }

    public void exit() {
        for(int i = 0; i < gos.length; i++) {
            this.gameObjects.remove(gos[i]);
        }
        SaveSystem.saveAndExit(saveGame, this);
        Window.changeScene(0);
    }
}
