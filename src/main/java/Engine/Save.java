package Engine;

public class Save {
    private GameScene instance;

    public GameScene getInstance() {
        return this.instance;
    }

    public Save(GameScene instance) {
        this.instance = instance;
    }
}
