package protein3DViewer.view;

import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import protein3DViewer.model.Atom;
import protein3DViewer.model.Chain;
import protein3DViewer.model.Model;
import protein3DViewer.model.Residue;
import protein3DViewer.view.modelVisualization.AbstractModelVisualization;
import protein3DViewer.view.modelVisualization.ModelVisualizationFactory;
import protein3DViewer.view.modelVisualization.SticksVisualization;

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
    private BottomGroup bottomGroup = new BottomGroup();
    private SubScene subScene;
    private Camera camera;

    private Map<VisualizationMode, AbstractModelVisualization> modelVisualizations = new HashMap<>();

    private final static VisualizationMode INITIAL_VISUALIZATION_MODE = VisualizationMode.STICKS;

    public ModelView(Model model) {
        this.model = model;
        init3DView();
        adjustProteinPosition();
        topPane.setPickOnBounds(false);
        addVisualization(INITIAL_VISUALIZATION_MODE);
        stackPane.getChildren().addAll(bottomPane, topPane);
        getChildren().addAll(stackPane);
    }

    /**
     * move protein to the center of the camera
     */
    private void adjustProteinPosition() {
        // calculate offset to camera position
        double offsetX = calculateOffset("X");
        double offsetY = calculateOffset("Y");
        // move each atom by offset
        for (Chain chain : model.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                for (Atom atom : residue.getAtoms().values()) {
                    atom.setX(atom.getX() + offsetX);
                    atom.setY(atom.getY() + offsetY);
                }
            }
        }
    }

    /**
     * calculate offset to the camera
     *
     * @param mode either X or Y
     * @return offset in X or Y direction
     */
    private double calculateOffset(String mode) {
        double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
        for (Chain chain : model.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                for (Atom atom : residue.getAtoms().values()) {
                    double value = mode.equalsIgnoreCase("X") ? atom.getX() : atom.getY();
                    if (value < min) {
                        min = value;
                    }
                    if (value > max) {
                        max = value;
                    }
                }
            }
        }
        double center = 0.5 * (min + max);
        return mode.equalsIgnoreCase("X") ? (camera.getTranslateX() - center) : (camera.getTranslateY() - center);
    }

    /**
     * set up sub scene and camera
     */
    private void init3DView() {
        subScene = new SubScene(bottomGroup, 1000, 1000, true, SceneAntialiasing.BALANCED);  // TODO: hard coded right now
        subScene.widthProperty().bind(stackPane.widthProperty());
        subScene.heightProperty().bind(stackPane.heightProperty());
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(Double.MAX_VALUE);
        camera.setTranslateZ(-100);
        subScene.setCamera(camera);
        bottomPane.getChildren().add(subScene);
    }

    /**
     * add visualization to the current view
     *
     * @param visualizationMode visualization mode
     */
    public void addVisualization(VisualizationMode visualizationMode) {
        modelVisualizations.put(visualizationMode, ModelVisualizationFactory.createModelVisualization(model, this, visualizationMode));
        bottomGroup.getChildren().add(modelVisualizations.get(visualizationMode).getBottomGroup());
    }

    /**
     * remove visualization from the current view
     *
     * @param visualizationMode visualization mode
     */
    public void removeVisualization(VisualizationMode visualizationMode) {
        modelVisualizations.get(visualizationMode).getBottomGroup().getChildren().clear();
        if (visualizationMode == VisualizationMode.STICKS) {
            topPane.getChildren().clear();
            ((SticksVisualization) modelVisualizations.get(VisualizationMode.STICKS)).clearSelection();
        }
    }

    /**
     * remove all visualization modes
     */
    public void clear() {
        for (VisualizationMode visualizationMode : modelVisualizations.keySet()) {
            removeVisualization(visualizationMode);
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
