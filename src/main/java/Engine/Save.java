package Engine;

public class Save {
    private float seed;
    private GameObject[] gos;

    public float getSeed() {
        return this.seed;
    }
    public GameObject[] getGOs() {
        return this.gos;
    }

    public Save(float seed, GameObject[] gos) {
        this.seed = seed;
        this.gos = gos;
    }
}
