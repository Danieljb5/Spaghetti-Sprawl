package Components;

import Engine.Component;
import Engine.Transform;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f colour;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty = true;

    public SpriteRenderer(Vector4f colour) {
        this.colour = colour;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.colour = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(this.lastTransform == null) {
            this.lastTransform = new Transform();
        }
        if(!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColour() {
        return this.colour;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }
    public void setColour(Vector4f colour) {
        if(!this.colour.equals(colour)) {
            this.isDirty = true;
            this.colour.set(colour);
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }
}
