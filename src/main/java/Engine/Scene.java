package Engine;

import Renderer.Renderer;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected boolean recalculateScreen;
    public Vector2f screenSize;

    public Scene() {

    }

    public void init()
    {

    }

    public void start() {
        for(GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if(!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public Camera camera() {
        return this.camera;
    }

    public void setScreenSize() {
        recalculateScreen = true;
    }

    protected Vector2f getScreenSize() {
        Vector2f screenSize = camera().screenToWorld(new Vector2f((int)(Math.sqrt(Window.getWidth()) * 5.9 - (float)(Math.pow(Math.pow(Window.getWidth() / 5, 5) / 300000000000f, 2)) / 50), (int)(Math.sqrt(Window.getHeight()) * 12 - (float)(Math.pow(Math.pow(Window.getHeight() / 5, 5) / 300000000000f, 2)) / 30) / 3.4f));
        return screenSize;
    }
}
