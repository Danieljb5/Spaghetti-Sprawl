package Engine;

import Components.Sprite;
import Components.SpriteRenderer;
import Utilities.AssetPool;
import org.joml.Vector2f;

public class MenuScene extends Scene {

    public MenuScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject obj = new GameObject("Object", new Transform(new Vector2f(0, 0), new Vector2f(128, 72)));
        obj.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/sprites/menu/menu.png"))));
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

        this.gameObjects.get(0).transform.scale = this.camera.screenToWorld(new Vector2f(214, 105));

        this.renderer.render();
    }
}
