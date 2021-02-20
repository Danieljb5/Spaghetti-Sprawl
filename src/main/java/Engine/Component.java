package Engine;

import java.io.Serializable;

public abstract class Component implements Serializable {
    public GameObject gameObject = null;

    public void start() {

    }

    public abstract void update(float dt);
}
