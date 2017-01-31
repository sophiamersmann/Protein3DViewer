package protein3DViewer.view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import protein3DViewer.MySelectionModel;
import protein3DViewer.model.*;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.atomView.CarbonView;
import protein3DViewer.view.modelVisualization.AbstractModelVisualization;
import protein3DViewer.view.modelVisualization.BoundingBox;
import protein3DViewer.view.modelVisualization.ModelVisualizationFactory;
import protein3DViewer.view.modelVisualization.SticksVisualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ModelView extends Group {

    private Model model;

    private StackPane stackPane = new StackPane();
    private Pane topPane = new Pane();
    private Pane bottomPane = new Pane();
    private BottomGroup bottomGroup = new BottomGroup();
    private SubScene subScene;
    private Camera camera;

//    private final Property<Transform> modelTransform = new SimpleObjectProperty<>(new Rotate());

    private Map<VisualizationMode, AbstractModelVisualization> modelVisualizations = new HashMap<>();

    private final static VisualizationMode INITIAL_VISUALIZATION_MODE = VisualizationMode.STICKS;

    public ModelView(Model model) {
        this.model = model;
        stackPane.setPrefSize(1500, 1500);
//        initModelTransformListener();
        init3DView();
        adjustProteinPosition();
        topPane.setPickOnBounds(false);
        addVisualization(INITIAL_VISUALIZATION_MODE);
        stackPane.getChildren().addAll(bottomPane, topPane);
        getChildren().addAll(stackPane);
    }


    private void adjustProteinPosition() {
        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
        for (Chain chain: model.getChains().values()) {
            for (Residue residue: chain.getResidues().values()) {
                for (Atom atom: residue.getAtoms().values()) {
                    if (atom.getX() < minX) {
                        minX = atom.getX();
                    }
                    if (atom.getX() > maxX) {
                        maxX = atom.getX();
                    }
                    if (atom.getY() < minY) {
                        minY = atom.getY();
                    }
                    if (atom.getY() > maxY) {
                        maxY = atom.getY();
                    }
                    if (atom.getZ() < minZ) {
                        minZ = atom.getZ();
                    }
                    if (atom.getZ() > maxZ) {
                        maxZ = atom.getZ();
                    }
                }
            }
        }
        double centerX = 0.5 * (minX + maxX);
        double centerY = 0.5 * (minY + maxY);
        double centerZ = 0.5 * (minZ + maxZ);
        double offsetX = camera.getTranslateX() - centerX;
        double offsetY = camera.getTranslateY() - centerY;
        double offsetZ = camera.getTranslateZ() - centerZ;
        for (Chain chain: model.getChains().values()) {
            for (Residue residue: chain.getResidues().values()) {
                for (Atom atom: residue.getAtoms().values()) {
                    atom.setX(atom.getX() + offsetX);
                    atom.setY(atom.getY() + offsetY);
//                    atom.setZ(atom.getZ() + offsetZ);
                }
            }
        }
    }

//    private void initModelTransformListener() {
//        modelTransform.addListener((observable, oldValue, newValue) -> {
//            this.getTransforms().setAll(newValue);
//        });
//    }

    private void init3DView() {
        subScene = new SubScene(bottomGroup, 800, 800, true, SceneAntialiasing.BALANCED);  // TODO: hard coded right now
        subScene.setFill(Color.LIGHTGREY);
        camera = new PerspectiveCamera(true);
//        subScene.widthProperty().bind(bottomPane.widthProperty());
//        subScene.heightProperty().bind(bottomPane.heightProperty());
//        camera.translateXProperty().bindBidirectional(bottomPane.prefWidthProperty());
//        camera.translateYProperty().bindBidirectional(bottomPane.prefHeightProperty());
        camera.setNearClip(0.1);
        camera.setFarClip(Double.MAX_VALUE);
        camera.setTranslateZ(-100);
        subScene.setCamera(camera);
        bottomPane.getChildren().add(subScene);
    }

    public void addVisualization(VisualizationMode visualizationMode) {
        modelVisualizations.put(visualizationMode, ModelVisualizationFactory.createModelVisualization(model, this, visualizationMode));
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

//    public Transform getWorldTransform() {
//        return modelTransform.getValue();
//    }
//
//    public Property<Transform> worldTransformProperty() {
//        return modelTransform;
//    }

    public Map<VisualizationMode, AbstractModelVisualization> getModelVisualizations() {
        return modelVisualizations;
    }

    public AbstractModelVisualization getModelVisualization(VisualizationMode mode) {
        return modelVisualizations.get(mode);
    }

    public BottomGroup getBottomGroup() {
        return bottomGroup;
    }
}
