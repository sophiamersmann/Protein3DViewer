package protein3DViewer.view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import protein3DViewer.model.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ModelView extends Group {

    private Model model;

    private StackPane stackPane = new StackPane();
    private Pane topPane = new Pane();
    private Pane bottomPane = new Pane();

    private final Property<Transform> modelTransform = new SimpleObjectProperty<>(new Rotate());

    private Map<String, ModelVisualization> modelVisualizations = new HashMap<>();

    private final static String INITIAL_VISUALIZATION_MODE = "Sticks";

    public ModelView(Model model) {
        this.model = model;
        initModelTransformListener();
        topPane.setPickOnBounds(false);
        modelVisualizations.put(INITIAL_VISUALIZATION_MODE, ModelVisualizationFactory.createModelVisualization(model, INITIAL_VISUALIZATION_MODE));
        bottomPane.getChildren().addAll(modelVisualizations.get(INITIAL_VISUALIZATION_MODE).bottomChildren);
        topPane.getChildren().addAll(modelVisualizations.get(INITIAL_VISUALIZATION_MODE).topChildren);
        stackPane.getChildren().addAll(bottomPane, topPane);
        getChildren().addAll(stackPane);
    }

    private void initModelTransformListener() {
        modelTransform.addListener((observable, oldValue, newValue) -> {
            this.getTransforms().setAll(newValue);
        });
    }

    public void addVisualization(String visualizationMode) {
        modelVisualizations.put(visualizationMode, ModelVisualizationFactory.createModelVisualization(model, visualizationMode));
        bottomPane.getChildren().addAll(modelVisualizations.get(visualizationMode).bottomChildren);
        topPane.getChildren().addAll(modelVisualizations.get(visualizationMode).topChildren);
    }

    public void removeVisualization(String visualizationMode) {
        modelVisualizations.remove(visualizationMode);
        bottomPane.getChildren().clear();
        topPane.getChildren().clear();
        for (String key : modelVisualizations.keySet()) {
            addVisualization(key);
        }
    }

    public Model getModel() {
        return model;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public Pane getTopPane() {
        return topPane;
    }

    public Pane getBottomPane() {
        return bottomPane;
    }

    public Transform getModelTransform() {
        return modelTransform.getValue();
    }

    public Property<Transform> modelTransformProperty() {
        return modelTransform;
    }

    public Map<String, ModelVisualization> getModelVisualizations() {
        return modelVisualizations;
    }

    public ModelVisualization getModelVisualization(String key) {
        return modelVisualizations.get(key);
    }
}
