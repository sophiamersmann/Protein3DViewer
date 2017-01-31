package protein3DViewer.view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * Created by sophiamersmann on 30/01/2017.
 */
public class BottomGroup extends Group {

    private final Property<Transform> worldTransform = new SimpleObjectProperty<>(new Rotate());

    public BottomGroup() {
        initModelTransformListener();
    }

    private void initModelTransformListener() {
        worldTransform.addListener((observable, oldValue, newValue) -> {
            this.getTransforms().setAll(newValue);
        });
    }

    public Transform getWorldTransform() {
        return worldTransform.getValue();
    }

    public Property<Transform> worldTransformProperty() {
        return worldTransform;
    }
}
