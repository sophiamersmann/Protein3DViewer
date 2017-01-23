package protein3DViewer.presenter;

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

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ModelPresenter {

    private ModelView modelView;
    private Model model;

    private Double downX;
    private Double downY;

    public ModelPresenter(ModelView modelView, Model model) {
        this.modelView = modelView;
        this.model = model;
        setUpPane();
    }

    private void setUpPane() {

        // TODO: zooming
        modelView.getStackPane().setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();
                double scaleDelta = 1.1;
                double scaleFactor = (event.getDeltaY() > 0) ? scaleDelta : 1 / scaleDelta;

                double scaledX = modelView.getScaleX() * scaleFactor;
                double scaledY = modelView.getScaleY() * scaleFactor;
                double scaledZ = modelView.getScaleZ() * scaleFactor;

                Scale scale = new Scale(scaledX, scaledY, scaledZ);
                modelView.modelTransformProperty().setValue(scale.createConcatenation(modelView.getModelTransform()));
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
                    modelView.modelTransformProperty().setValue(translate.createConcatenation(modelView.getModelTransform()));
                } else {
                    Point2D delta = new Point2D(deltaX, deltaY);
                    Point3D rotationAxis = new Point3D(-deltaY, deltaX, 0);
                    Rotate rotate = new Rotate(delta.magnitude(), rotationAxis);
                    modelView.modelTransformProperty().setValue(rotate.createConcatenation(modelView.getModelTransform()));
                }
                downX = event.getSceneX();
                downY = event.getSceneY();
                event.consume();
            }
        });
    }

}
