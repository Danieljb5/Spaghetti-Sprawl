package Engine;

import Components.Tile;
import Utilities.AssetPool;
import org.joml.Vector2f;

public class GameScene extends Scene {

    GameObject[] gos;
    Float[] values;
    int width = 150, height = 75;
    float res = 50f;
    float seed;

    public GameScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        loadResources();

        OpenSimplexNoise noise = new OpenSimplexNoise();
        seed = (float)Math.random() * 100f;
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

        this.renderer.render();
    }
}
