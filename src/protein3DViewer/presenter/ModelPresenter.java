package protein3DViewer.presenter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import protein3DViewer.MySelectionModel;
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
    private MySelectionModel<AbstractAtomView> selectionModel;

    private Double downX;
    private Double downY;

    public ModelPresenter(ModelView modelView, Model model) {
        this.modelView = modelView;
        this.model = model;
        initSelectionModel();
        setUpPane();
        setUpAtomViews();
    }

    private void initSelectionModel() {
        if (modelView.getModelVisualizations().containsKey(VisualizationMode.STICKS)) {
            SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
            atomViews = new ArrayList<>(sticksVisualization.getAtomViews().values());
            selectionModel = new MySelectionModel(atomViews.toArray());

            selectionModel.getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
                public Color originalColor;

                @Override
                public void onChanged(Change<? extends AbstractAtomView> c) {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            for (AbstractAtomView atomView: c.getAddedSubList()) {
                                originalColor = atomView.getDiffuseColor();
                                atomView.setMaterial(Color.BLACK);
                            }
                        }
                        if (c.wasRemoved()) {
                            for (AbstractAtomView atomView: c.getRemoved()) {
                                atomView.setMaterial(originalColor);
                            }
                        }
                    }

                }
            });

        }
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
                if (event.isShiftDown()) {
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

    private void setUpAtomViews() {
        for (AbstractAtomView atomView: atomViews) {
            setUpAtomView(atomView);
        }

    }


    private void setUpAtomView(AbstractAtomView atomView) {

        atomView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                atomView.setSelected(!atomView.getSelected());  // TODO change in atom vie to isSelected()
            }
        });

        atomView.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    selectionModel.select(atomViews.indexOf(atomView));
                } else {
                    selectionModel.clearSelection();  // TODO
                }

            }
        });
    }
}
