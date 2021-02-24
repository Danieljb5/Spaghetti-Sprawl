package Engine;

import Components.Button;
import Components.Chunk;
import Components.Sprite;
import Components.SpriteRenderer;
import Content.ContentInterpreter;
import Content.ContentLoader;
import Utilities.AssetPool;
import org.joml.Vector2f;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class MenuScene extends Scene {

    static MenuScene instance;
    GameObject[] gos;
    float[] values;
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

        this.seed = (((float)Math.random() * 20000f) - 10000f);
        OpenSimplexNoise noise = new OpenSimplexNoise();
        GameObject go;
        for(int x1 = 0; x1 <= width / 16; x1++) {
            for(int y1 = 0; y1 < height / 16; y1++) {
                values = new float[1024];
                for (int x = 0; x < 32; x++) {
                    for (int y = 0; y < 32; y++) {
                        double value = noise.eval((x + (x1 * 32)) * (1f / res), (y + (y1 * 32)) * (1f / res), seed);
                        values[x + y * 32] = (float) value;
                    }
                }
                go = new GameObject("Chunk");
                go.transform.position = new Vector2f(x1 * 160, 480 - y1 * 160);
                go.transform.scale = new Vector2f(160, 160);
                go.addComponent(new Chunk());
                go.getComponent(Chunk.class).setAllValues(values);
                go.getComponent(Chunk.class).showChunk();
                this.addGameObjectToScene(go);
            }
        }

        List<String[]> content = ContentLoader.loadAllContent();
        ContentInterpreter interpreter = new ContentInterpreter();
        interpreter.InterpretContent(content);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {
        if(this.recalculateScreen) {
            screenSize = getScreenSize();
            this.recalculateScreen = false;
        }

        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.gameObjects.get(0).transform.scale = screenSize;
        this.gameObjects.get(1).transform.position = this.camera.screenToWorld(new Vector2f(2, 10));
        this.gameObjects.get(1).transform.scale = this.camera.screenToWorld(new Vector2f(29, 12));
        this.gameObjects.get(2).transform.position = this.camera.screenToWorld(new Vector2f(2, 50));
        this.gameObjects.get(2).transform.scale = this.camera.screenToWorld(new Vector2f(33, 12));

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
        Window.changeScene(1);
    }

    static void quitButtonListener() {
        Window.get().quit();
    }
    static void startButtonListener() {
        MenuScene.instance.showSaves();
    }
}
