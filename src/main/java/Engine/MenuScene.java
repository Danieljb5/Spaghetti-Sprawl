package Engine;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.Tile;
import Utilities.AssetPool;
import org.joml.Vector2f;

public class MenuScene extends Scene {

    GameObject[] gos;
    Float[] values;
    int width = 150, height = 75;
    float res = 50f;

    public MenuScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject obj = new GameObject("Object", new Transform(new Vector2f(0, 0), new Vector2f(128, 72)), 1);
        obj.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/sprites/menu/menu.png"))));
        this.addGameObjectToScene(obj);

        loadResources();

        OpenSimplexNoise noise = new OpenSimplexNoise();
        gos = new GameObject[width * height];
        values = new Float[width * height];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double value = noise.eval((double)(x * (1f / res)), (double)(y * (1f / res)));
                System.out.println(value);
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

        this.gameObjects.get(0).transform.scale = this.camera.screenToWorld(new Vector2f(214, 105));

        this.renderer.render();
    }
}
