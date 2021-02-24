package Components;

import Engine.Component;
import Renderer.Texture;
import Utilities.AssetPool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chunk extends Component implements Serializable {

    private float[] values;
    private int[] vars;
    private float[] levels = {
            -1f, -0.1f, 0.1f
    };
    private int[] variants = {
            2, 2, 2
    };
    private int[] tileTypes;
    private List<String> textures = new ArrayList<>();

    @Override
    public void update(float dt) {

    }

    public void setAllValues(float[] allValues) {
        this.values = allValues;
        this.vars = new int[allValues.length];
        this.tileTypes = new int[allValues.length];
        for(int i = 0; i < allValues.length; i++) {
            setValue(allValues[i], i);
        }
    }

    public void setValue(float value, int index) {
        this.values[index] = value;
        this.tileTypes[index] = -1;
        for(int i = 0; i < levels.length - 1; i++) {
            if(value > levels[i] && value < levels[i + 1]) {
                this.tileTypes[index] = i;
                break;
            }
        }
        if(this.tileTypes[index] < 0 || this.tileTypes[index] >= levels.length) {
            this.tileTypes[index] = levels.length - 1;
        }
        Random random = new Random((long)Math.abs(value * 100));
        random.nextFloat();
        int variant = (int)(random.nextFloat() * variants[this.tileTypes[index]]);
        if(variant > variants[this.tileTypes[index]] - 1){
            variant = variants[this.tileTypes[index]] - 1;
        }
        vars[index] = variant;
        if(!textures.contains("assets/sprites/blocks/environment/" + this.tileTypes[index] + "/" + variant + ".png")) {
            textures.add("assets/sprites/blocks/environment/" + this.tileTypes[index] + "/" + variant + ".png");
        }
    }

    public void showChunk() {
        List<Texture> loadedTextures = new ArrayList<>();
        List<BufferedImage> loadedTextureBuffers = new ArrayList<>();
        for(int i = 0; i < textures.size(); i++) {
            loadedTextures.add(AssetPool.getTexture(textures.get(i)));
            loadedTextureBuffers.add(loadedTextures.get(i).getBufferedImage());
        }
        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < 32; x++) {
            for(int y = 0; y < 32; y++) {
                Texture tex = AssetPool.getTexture("assets/sprites/blocks/environment/" + this.tileTypes[x + y * 32] + "/" + vars[x + y * 32] + ".png");
                BufferedImage tileBuffer = tex.getBufferedImage();
                addImage(image, tileBuffer, x * 32, y * 32);
            }
        }

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
            }
        }
        byte[] imageInByte = baos.toByteArray();
        ByteBuffer byteBuffer = makeByteBuffer(imageInByte);

        Texture finalTex = new Texture(byteBuffer, image.getWidth(), image.getHeight());
        Sprite spr = new Sprite(finalTex);

        if(spr != null) {
            if (this.gameObject.getComponent(SpriteRenderer.class) == null) {
                this.gameObject.addComponent(new SpriteRenderer(spr));
            } else {
                this.gameObject.getComponent(SpriteRenderer.class).setSprite(spr);
            }
        } else {
            System.out.println("Sprite " + this.gameObject.id() + " is empty");
        }
    }

    private ByteBuffer makeByteBuffer(byte[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length);
        bb.order(ByteOrder.nativeOrder());
        bb.put(arr);
        bb.position(0);
        return bb;
    }

    private void addImage(BufferedImage buff1, BufferedImage buff2, int x, int y) {
        Graphics2D g2d = buff1.createGraphics();
        g2d.drawImage(buff2, x, y, null);
        g2d.dispose();
    }

    public int getTileType(int index) {
        return this.tileTypes[index];
    }

    public int[] getTileTypes() {
        return this.tileTypes;
    }

    public int[] getVariants() {
        return this.variants;
    }
}
