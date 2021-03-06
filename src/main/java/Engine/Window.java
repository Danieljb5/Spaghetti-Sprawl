package Engine;

import Utilities.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    int width, height;
    String title;
    private long glfwWindow;
    public float r, g, b, a;

    private static Window window = null;
    private static Scene currentScene;
    private int[] aWidth = new int[1], aHeight = new int[1];

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Spaghetti Sprawl";
        r = 0;
        g = 0;
        b = 0;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new MenuScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new GameScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "[Engine.Window] Error: Unknown scene '" + newScene +"'";
                break;
        }
    }

    public static void setScene(Scene scene) {
        currentScene = scene;
        currentScene.init();
        currentScene.start();
    }

    public static Window get()
    {
        if(window == null) {
            window = new Window();
        }

        return window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void run() {
        System.out.println("LWJGL " + Version.getVersion());

        init();
        loop();
        if(GameScene.isInitialised) {
            GameScene.instance.terrarinGenerator.kill();
        }
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        //glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0) {
                currentScene.update(dt);
            }

            glfwGetWindowSize(glfwWindow, aWidth, aHeight);
            if(aWidth[0] < 1320) {
                glfwSetWindowSize(glfwWindow, 1320, aHeight[0]);
            }
            if(aHeight[0] < 880) {
                glfwSetWindowSize(glfwWindow, aWidth[0], 880);
            }
            if(width != aWidth[0] || height != aHeight[0]) {
                width = aWidth[0];
                height = aHeight[0];
                MenuScene.instance.setScreenSize();
                if(GameScene.isInitialised) {
                    GameScene.instance.setScreenSize();
                }
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public void quit() {
        glfwSetWindowShouldClose(glfwWindow, true);
    }

    public static int getWidth() {
        return get().width;
    }
    public static int getHeight() {
        return get().height;
    }
}