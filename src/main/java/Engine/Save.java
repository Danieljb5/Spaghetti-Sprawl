package Engine;

public class Save {
    private float seed;
    private GameObject[] gos;
    private GameScene instance;

    public GameScene getInstance() {
        return this.instance;
    }

    public Save(GameScene instance) {
        this.instance = instance;
    }
}
