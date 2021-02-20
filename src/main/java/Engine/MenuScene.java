package Engine;

import Components.Button;
import Components.Sprite;
import Components.SpriteRenderer;
import Components.Tile;
import Utilities.AssetPool;
import org.joml.Vector2f;

import java.io.File;
import java.io.FilenameFilter;

public class MenuScene extends Scene {

    static MenuScene instance;
    GameObject[] gos;
    Float[] values;
    int width = 150, height = 75;
    float res = 50f;
    float seed;

    public MenuScene() {
        instance = this;
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject obj = new GameObject("Object", new Transform(new Vector2f(0, 0), new Vector2f(128, 72)), 10);
        obj.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/sprites/menu/menu.png"))));
        this.addGameObjectToScene(obj);
        GameObject obj2 = new GameObject("Object", 10);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/sprites/menu/quitButton.png"))));
        obj2.addComponent(new Button(MenuScene::quitButtonListener, Button.Mode.Down));
        this.addGameObjectToScene(obj2);
        GameObject obj3 = new GameObject("Object", 10);
        obj3.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/sprites/menu/startButton.png"))));
        obj3.addComponent(new Button(MenuScene::startButtonListener, Button.Mode.Down));
        this.addGameObjectToScene(obj3);

        loadResources();

        OpenSimplexNoise noise = new OpenSimplexNoise();
        seed = (float)(Math.random() * 2 - 1) * 10000f;
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

        this.gameObjects.get(0).transform.scale = this.camera.screenToWorld(new Vector2f(214, 105));
        this.gameObjects.get(1).transform.position = this.camera.screenToWorld(new Vector2f(5, 10));
        this.gameObjects.get(1).transform.scale = this.camera.screenToWorld(new Vector2f(25, 12));
        this.gameObjects.get(2).transform.position = this.camera.screenToWorld(new Vector2f(5, 50));
        this.gameObjects.get(2).transform.scale = this.camera.screenToWorld(new Vector2f(25, 12));

        this.renderer.render();
    }

    private void showSaves() {
        File saveDir = new File("saves");
        String[] saves = saveDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
    }

    static void quitButtonListener() {
        Window.get().quit();
    }
    static void startButtonListener() {
        MenuScene.instance.showSaves();
    }
}
