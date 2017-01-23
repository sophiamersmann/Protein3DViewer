package protein3DViewer.view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import protein3DViewer.model.*;

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

    private Map<Integer, AtomView> atomViews = new HashMap<>();
    private List<BondView> bondViews = new ArrayList<>();

    private Group atomViewGroup = new Group();
    private Group bondViewGroup = new Group();

    private final Property<Transform> modelTransform = new SimpleObjectProperty<>(new Rotate());

    public ModelView(Model model) {
        this.model = model;
        modelTransform.addListener((observable, oldValue, newValue) -> {
            this.getTransforms().setAll(newValue);
        }); // TODO: not nice here
        init3DView();
        topPane.setPickOnBounds(false);
        stackPane.getChildren().addAll(bottomPane, topPane);
        getChildren().addAll(stackPane);
    }

    private void init3DView() {
        SubScene subScene = new SubScene(bottomPane, 1000, 1000);  // TODO: hard coded right now
        Camera camera = new PerspectiveCamera();  // TODO: is that working properly?
//        camera.setTranslateZ(-100);
//        camera.setTranslateX(0);
//        camera.setTranslateY(0);
//        camera.setNearClip(0.1);
//        camera.setFarClip(10000);
//        camera.setFieldOfView(35);
        subScene.setCamera(camera);

        initAtomViews();
        initBondViews();
        showAtomViews();
        showBondViews();
        bottomPane.getChildren().addAll(bondViewGroup, atomViewGroup);
    }

    private void initAtomViews() {
        final AtomViewFactory atomViewFactory = new AtomViewFactory();
        for (Chain chain : model.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                for (Atom atom : residue.getAtoms().values()) {
                    atomViews.put(atom.getId(), atomViewFactory.createAtomView(atom));
                }
            }
        }
    }

    private void initBondViews() {
        for (Chain chain : model.getChains().values()) {
            for (Bond bond : chain.getBonds()) {
                bondViews.add(new BondView(bond));
            }
        }
    }

    public void showAtomViews() {
        atomViewGroup.getChildren().addAll(atomViews.values());
    }

    public void hideAtomViews() {
        atomViewGroup.getChildren().clear();
    }

    public void showBondViews() {
        bondViewGroup.getChildren().addAll(bondViews);
    }

    public void hideBondViews() {
        bondViewGroup.getChildren().clear();
    }

    public void changeAtomSize(Double factor) {
        for (AtomView atomView : atomViews.values()) {
            atomView.changeRadius(factor);
        }
    }

    public void changeBondSize(Double factor) {
        for (BondView bondView : bondViews) {
            bondView.changeRadius(factor);
        }
    }

    public void changeAtomColor(String mode) {
        for (AtomView atomView : atomViews.values()) {
            Color color = atomView.getColor(mode);  // TODO getColor static?
            atomView.setMaterial(color);
        }
    }

    public Transform getModelTransform() {
        return modelTransform.getValue();
    }

    public Property<Transform> modelTransformProperty() {
        return modelTransform;
    }

    public Pane getBottomPane() {
        return bottomPane;
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
