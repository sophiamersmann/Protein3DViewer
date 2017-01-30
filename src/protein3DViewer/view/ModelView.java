package protein3DViewer.view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import protein3DViewer.model.Model;
import protein3DViewer.view.modelVisualization.AbstractModelVisualization;
import protein3DViewer.view.modelVisualization.ModelVisualizationFactory;

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
    private Group bottomGroup = new Group();
    private SubScene subScene;
    private Camera camera;

    private final Property<Transform> modelTransform = new SimpleObjectProperty<>(new Rotate());

    private Map<VisualizationMode, AbstractModelVisualization> modelVisualizations = new HashMap<>();

    private final static VisualizationMode INITIAL_VISUALIZATION_MODE = VisualizationMode.STICKS;

    public ModelView(Model model) {
        this.model = model;
        initModelTransformListener();
        init3DView();
        topPane.setPickOnBounds(false);
        addVisualization(INITIAL_VISUALIZATION_MODE);
        stackPane.getChildren().addAll(bottomPane, topPane);
        getChildren().addAll(stackPane);
    }

    private void initModelTransformListener() {
        modelTransform.addListener((observable, oldValue, newValue) -> {
            this.getTransforms().setAll(newValue);
        });
    }

    private void init3DView() {
        subScene = new SubScene(bottomGroup, 500, 500);  // TODO: hard coded right now
        camera = new PerspectiveCamera();
//        camera.translateXProperty().bindBidirectional(bottomPane.prefWidthProperty());
//        camera.translateYProperty().bindBidirectional(bottomPane.prefHeightProperty());
        camera.setNearClip(0.1);
        camera.setFarClip(Double.MAX_VALUE);
        camera.setTranslateZ(-100);
        subScene.setCamera(camera);
        bottomPane.getChildren().add(subScene);
    }

    public void addVisualization(VisualizationMode visualizationMode) {
        modelVisualizations.put(visualizationMode, ModelVisualizationFactory.createModelVisualization(model, visualizationMode));
//        bottomPane.getChildren().add(modelVisualizations.get(visualizationMode).getBottomGroup());
        bottomGroup.getChildren().add(modelVisualizations.get(visualizationMode).getBottomGroup());
        topPane.getChildren().add(modelVisualizations.get(visualizationMode).getTopGroup());
    }

    public void removeVisualization(VisualizationMode visualizationMode) {
        modelVisualizations.get(visualizationMode).getBottomGroup().getChildren().clear();
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

    public Map<VisualizationMode, AbstractModelVisualization> getModelVisualizations() {
        return modelVisualizations;
    }

    public AbstractModelVisualization getModelVisualization(VisualizationMode mode) {
        return modelVisualizations.get(mode);
    }

    public Camera getCamera() {
        return camera;
    }
}
