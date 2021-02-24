package Components;

import Engine.Component;
import Engine.MouseListener;
import Engine.Window;

public class Button extends Component {

    private Runnable callback;
    public enum Mode { Down, Up, Any}
    Mode mode;
    boolean pressed, lastFrame;

    public Button (Runnable callback, Mode mode) {
        this.callback = callback;
        this.mode = mode;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        if (MouseListener.mouseButtonDown(0)) {
            float x = MouseListener.getOrthoX();
            float y = MouseListener.getOrthoY();
            y = (float) Window.getScene().screenSize.y - y;
            if (x >= this.gameObject.transform.position.x && x <= this.gameObject.transform.position.x + this.gameObject.transform.scale.x && y >= this.gameObject.transform.position.y && y <= (this.gameObject.transform.position.y + this.gameObject.transform.scale.y)) {
                pressed = true;
            }
        } else {
            pressed = false;
        }

        if((mode == Mode.Any && pressed == true) || (mode == Mode.Down && lastFrame == false && pressed == true) || (mode == Mode.Up && lastFrame == true && pressed == false)) {
            press();
        }

        lastFrame = pressed;
    }

    private void press() {
        callback.run();
    }
}
