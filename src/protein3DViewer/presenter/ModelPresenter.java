package protein3DViewer.presenter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import protein3DViewer.model.Model;
import protein3DViewer.view.ModelView;
import protein3DViewer.view.VisualizationMode;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.modelVisualization.SticksVisualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ModelPresenter {

    private ModelView modelView;
    private Model model;

    private List<AbstractAtomView> atomViews;

    private Double downX;
    private Double downY;

    public ModelPresenter(ModelView modelView, Model model) {
        this.modelView = modelView;
        this.model = model;
        setUpPane();
        setUpAtomViews();
    }


    private void setUpPane() {

        // TODO: zooming
        modelView.getStackPane().setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();
                double scaleDelta = 1.1;
                double scaleFactor = (event.getDeltaY() > 0) ? scaleDelta : 1 / scaleDelta;

                double scaledX = modelView.getBottomGroup().getScaleX() * scaleFactor;
                double scaledY = modelView.getBottomGroup().getScaleY() * scaleFactor;
                double scaledZ = modelView.getBottomGroup().getScaleZ() * scaleFactor;

                Scale scale = new Scale(scaledX, scaledY, scaledZ);
                modelView.getBottomGroup().worldTransformProperty().setValue(scale.createConcatenation(modelView.getBottomGroup().getWorldTransform()));

//                modelView.worldTransformProperty().setValue(scale.createConcatenation(modelView.getWorldTransform()));
            }
        });

        modelView.getStackPane().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                downX = event.getSceneX();
                downY = event.getSceneY();
            }
        });

        modelView.getStackPane().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Double deltaX = event.getSceneX() - downX;
                Double deltaY = event.getSceneY() - downY;
                if (event.isShiftDown()) { /// TODO remove that
                    Translate translate = new Translate(deltaX, deltaY);
                    modelView.getBottomGroup().worldTransformProperty().setValue(translate.createConcatenation(modelView.getBottomGroup().getWorldTransform()));
//                    modelView.worldTransformProperty().setValue(translate.createConcatenation(modelView.getWorldTransform()));
                } else {
                    Point2D delta = new Point2D(deltaX, deltaY);
                    Point3D rotationAxis = new Point3D(-deltaY, deltaX, 0);
                    Rotate rotate = new Rotate(delta.magnitude(), rotationAxis);
                    modelView.getBottomGroup().worldTransformProperty().setValue(rotate.createConcatenation(modelView.getBottomGroup().getWorldTransform()));
//                    modelView.worldTransformProperty().setValue(rotate.createConcatenation(modelView.getWorldTransform()));
                }
                downX = event.getSceneX();
                downY = event.getSceneY();
                event.consume();
            }
        });
    }

    void setUpAtomViews() {
        SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
        atomViews = new ArrayList<>(sticksVisualization.getAtomViews().values());
        for (AbstractAtomView atomView: atomViews) {
            setUpAtomView(atomView);
        }
//        SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
//        for (AbstractAtomView atomView: sticksVisualization.getAtomViews().values()) {
//            setUpAtomView(atomView);
//        }

    }


    private void setUpAtomView(AbstractAtomView atomView) {

        atomView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                if (event.isShiftDown()) {
                    atomView.setShiftSelected(!atomView.isShiftSelected());
                } else {
                    SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
                    atomView.setSelected(!atomView.isSelected());
                    for (int atomID: atomView.getAtom().getResidue().getAtoms().keySet()) {
                        AbstractAtomView view = sticksVisualization.getAtomViews().get(atomID);
                        view.setShiftSelected(!view.isShiftSelected());
                    }
                }
            }
        });

        atomView.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
                if (newValue) {
                    sticksVisualization.getSelectionModel().clearAndSelect(atomViews.indexOf(atomView));
                } else {
                    sticksVisualization.getSelectionModel().clearSelection(atomViews.indexOf(atomView)); // TODO
                }

            }
        });

        atomView.shiftSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
                if (newValue) {
                    sticksVisualization.getSelectionModel().select(atomViews.indexOf(atomView));
                } else {
                    sticksVisualization.getSelectionModel().clearSelection(atomViews.indexOf(atomView));
                }
            }
        });
    }
}
