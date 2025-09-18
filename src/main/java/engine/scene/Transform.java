package engine.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {
    private final String id;
    private Matrix4f modelMatrix;
    private Vector3f position;
    private Quaternionf rotation;
    private  float scale;

    public Transform(String id){
        this.id = id;
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
    }

    public String getId() {
        return id;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x,y,z,angle);
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public void updateModelMatrix(){
        modelMatrix.translationRotateScale(position,rotation,scale);
    }
}
