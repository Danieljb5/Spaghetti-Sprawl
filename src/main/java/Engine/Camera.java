package Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f,0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp);
        this.viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return this.inverseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }

    public Vector2f worldToScreen(Vector2f worldPos) {
        float currentX = worldPos.x;
        currentX = (currentX / (float)Window.get().width) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        tmp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
        currentX = tmp.x;

        float currentY = worldPos.y;
        currentY = (currentY / (float)Window.get().height) * 2.0f - 1.0f;
        tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
        currentY = tmp.y;

        return new Vector2f(currentX, currentY);
    }

    public Vector2f screenToWorld(Vector2f screenPos) {
        float currentX = screenPos.x;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        tmp.mul(Window.getScene().camera().getProjectionMatrix()).mul(Window.getScene().camera().getViewMatrix());
        currentX = tmp.x;
        currentX = ((currentX + 1) * 2) * Window.get().width;

        float currentY = screenPos.y;
        tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(Window.getScene().camera().getProjectionMatrix()).mul(Window.getScene().camera().getViewMatrix());
        currentY = tmp.y;
        currentY = ((currentY + 1) * 2) * Window.get().height;

        return new Vector2f(currentX, currentY);
    }
}
