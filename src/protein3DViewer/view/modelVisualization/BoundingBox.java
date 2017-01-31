package protein3DViewer.view.modelVisualization;

import com.oracle.webservices.internal.api.message.PropertySet;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.Property;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import protein3DViewer.view.atomView.AbstractAtomView;

/**
 * Created by sophiamersmann on 30/01/2017.
 */
public class BoundingBox extends Rectangle {

    private ObjectBinding<Rectangle> binding;

    public BoundingBox(AbstractAtomView atomView, Pane pane, Property<Transform> worldTransform) {
        binding = createBoundingBoxBinding(pane, atomView, worldTransform);
        initBindings(pane);
        setStroke(Color.BLACK);
        setFill(Color.TRANSPARENT);
        setMouseTransparent(true);
    }

    private ObjectBinding<Rectangle> createBoundingBoxBinding(Pane pane, AbstractAtomView atomView, Property<Transform> worldTransform) {
        ObjectBinding<Rectangle> objectBinding = new ObjectBinding<Rectangle>() {
            @Override
            protected Rectangle computeValue() {
                bind(worldTransform);
                bind(atomView.xProperty(), atomView.yProperty());
                bind(atomView.scaleXProperty(), atomView.scaleYProperty());
                final Bounds boundsOnScreen = atomView.localToScreen(atomView.getBoundsInLocal());
                final Bounds paneBoundsOnScreen = pane.localToScreen(pane.getBoundsInLocal());
                final double xInScene = boundsOnScreen.getMinX() - paneBoundsOnScreen.getMinX();
                final double yInScene = boundsOnScreen.getMinY() - paneBoundsOnScreen.getMinY();
                return new Rectangle(xInScene, yInScene, boundsOnScreen.getWidth(), boundsOnScreen.getHeight());
            }
        };
        return objectBinding;
    }

    private void initBindings(Pane pane) {
        xProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getX();
            }
        });

        yProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getY();
            }
        });

        scaleXProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getScaleX();
            }
        });

        scaleYProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getScaleY();
            }
        });

        widthProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getWidth();
            }
        });

        heightProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getHeight();
            }
        });
    }

}
