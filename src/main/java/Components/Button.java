package Components;

import Engine.Component;
import Engine.MouseListener;

public class Button extends Component {

    private Runnable callback;

    public Button (Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        if(MouseListener.mouseButtonDown(0)) {
            float x = MouseListener.getOrthoX();
            float y = MouseListener.getOrthoY();
            y = (float)671.37787 - y;
            if(x >= this.gameObject.transform.position.x && x <= this.gameObject.transform.position.x + this.gameObject.transform.scale.x && y >= this.gameObject.transform.position.y && y <= (this.gameObject.transform.position.y + this.gameObject.transform.scale.y)) {
                press();
            }
        }
    }

    private void press() {
        callback.run();
    }
}
