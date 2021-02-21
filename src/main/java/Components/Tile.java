package Components;

import Engine.Component;
import Utilities.AssetPool;

import java.util.Random;

public class Tile extends Component {

    private float value;
    private float[] levels = {
            -1f, -0.1f, 0.1f
    };
    private int[] variants = {
            2, 2, 2
    };
    private int tileType;

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

    public void setValue(float value) {
        this.value = value;
        this.tileType = -1;
        for(int i = 0; i < levels.length - 1; i++) {
            if(value > levels[i] && value < levels[i + 1]) {
                this.tileType = i;
                break;
            }
        }
        if(this.tileType < 0 || this.tileType >= levels.length) {
            this.tileType = levels.length - 1;
        }
        Random random = new Random((long)Math.abs(value * 100));
        random.nextFloat();
        int variant = (int)(random.nextFloat() * variants[this.tileType]);
        if(variant > variants[this.tileType] - 1){
            variant = variants[this.tileType] - 1;
        }
        Sprite sprite = new Sprite(AssetPool.getTexture("assets/sprites/blocks/environment/" + this.tileType + "/" + variant + ".png"));

        if(this.gameObject.getComponent(SpriteRenderer.class) == null) {
            this.gameObject.addComponent(new SpriteRenderer(sprite));
        } else {
            this.gameObject.getComponent(SpriteRenderer.class).setSprite(sprite);
        }
    }

    public int getTileType() {
        return this.tileType;
    }

    public int[] getVariants() {
        return this.variants;
    }
}
