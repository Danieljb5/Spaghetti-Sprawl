package Engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameObject implements Serializable {
    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;
    private int id;

    public GameObject(String name) {
        this.name = name;
        this.zIndex = 0;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        addID();
    }

    public GameObject(String name, int zIndex) {
        this.name = name;
        this.zIndex = zIndex;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        addID();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.zIndex = 0;
        this.components = new ArrayList<>();
        this.transform = transform;
        addID();
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.zIndex = zIndex;
        this.components = new ArrayList<>();
        this.transform = transform;
        addID();
    }

    private void addID() {
        id = Window.getScene().gameObjects.size() + 1;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch(ClassCastException e) {
                    e.printStackTrace();
                    assert false : "[Engine.GameObject] Error: Casting component.";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for(int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public int zIndex() {
        return this.zIndex;
    }
    public int id() {
        return this.id;
    }
}
