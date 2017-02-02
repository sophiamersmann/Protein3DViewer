package protein3DViewer.presenter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
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

        /**
         * reset all selections made by left-clicking
         */
        modelView.getStackPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                if (event.getButton() == MouseButton.SECONDARY) {
                    if (modelView.getModelVisualizations().containsKey(VisualizationMode.STICKS)) {
                        SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
                        sticksVisualization.clearSelection();
                    }
                    modelView.getTopPane().getChildren().clear();
                }

            }
        });

        /**
         * zoom on scrolling
         */
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

        /**
         * rotate displayed model content by dragging
         */
        modelView.getStackPane().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Double deltaX = event.getSceneX() - downX;
                Double deltaY = event.getSceneY() - downY;
                Point2D delta = new Point2D(deltaX, deltaY);
                Point3D rotationAxis = new Point3D(-deltaY, deltaX, 0);
                Rotate rotate = new Rotate(delta.magnitude(), rotationAxis);
                modelView.getBottomGroup().worldTransformProperty().setValue(rotate.createConcatenation(modelView.getBottomGroup().getWorldTransform()));
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

    }

    /**
     * given a specific atom view, shift-select all atom views belonging
     * to the same residue
     *
     * @param atomView given atom view
     */
    public void shiftSelectAllAtomViewsOfResidue(AbstractAtomView atomView) {
        SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
        for (int atomID: atomView.getAtom().getResidue().getAtoms().keySet()) {
            AbstractAtomView view = sticksVisualization.getAtomViews().get(atomID);
            view.setShiftSelected(!view.isShiftSelected());
        }
    }


    private void setUpAtomView(AbstractAtomView atomView) {

        /**
         * make atom clickable
         */
        atomView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                if (event.isShiftDown()) {
                    atomView.setShiftSelected(!atomView.isShiftSelected());
                } else if (event.isAltDown()) {
                    atomView.setAltSelected(!atomView.isAltSelected());
                } else {
                    atomView.setSelected(!atomView.isSelected());
                    shiftSelectAllAtomViewsOfResidue(atomView);
                }
            }
        });

        /**
         * update selection model if atom gets selected or unselected
         */
        atomView.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
                if (newValue) {
                    sticksVisualization.getResidueSelectionModel().clearAndSelect(atomViews.indexOf(atomView));
                } else {
                    sticksVisualization.getResidueSelectionModel().clearSelection();
                }

            }
        });

        /**
         * update selection model if atom gets shift-selected or -unselected
         */
        atomView.shiftSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
                if (newValue) {
                    sticksVisualization.getResidueSelectionModel().select(atomViews.indexOf(atomView));
                } else {
                    sticksVisualization.getResidueSelectionModel().clearSelection(atomViews.indexOf(atomView));
                }
            }
        });

        /**
         * update selection model if atom gets alt-selected or -unselected
         */
        atomView.altSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
                if (newValue) {
                    sticksVisualization.getAtomSelectionModel().select(atomViews.indexOf(atomView));
                } else {
                    sticksVisualization.getAtomSelectionModel().clearSelection(atomViews.indexOf(atomView));
                }
            }
        });
    }

    public ModelView getModelView() {
        return modelView;
    }
}
