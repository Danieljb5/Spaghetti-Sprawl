package Engine;

import Components.SpriteRenderer;
import Utilities.AssetPool;
import org.joml.Vector2f;

public class MenuScene extends Scene {

    public MenuScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject obj = new GameObject("Object", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/sprites/logo.png")));
        this.addGameObjectToScene(obj);

        loadResources();
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
