package protein3DViewer.view.modelVisualization;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.Property;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Transform;
import protein3DViewer.view.atomView.AbstractAtomView;


/**
 * Created by sophiamersmann on 01/02/2017.
 */
public class AtomLabel extends Label {

    private ObjectBinding<Label> binding;

    public AtomLabel(AbstractAtomView atomView, Pane pane, Property<Transform> worldTransform, String text) {
        binding = createAtomLabelBinding(atomView, pane, worldTransform);
        initBindings(pane);
        setText(text);
        setTextFill(Color.BLACK);
        setMouseTransparent(true);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, new Insets(0))));
    }

    private ObjectBinding<Label> createAtomLabelBinding(AbstractAtomView atomView, Pane pane, Property<Transform> worldTransform) {
        ObjectBinding<Label> objectBinding = new ObjectBinding<Label>() {
            @Override
            protected Label computeValue() {
                bind(worldTransform);
                bind(atomView.translateXProperty(), atomView.translateYProperty());
                bind(atomView.scaleXProperty(), atomView.scaleYProperty());
                final Bounds boundsOnScreen = atomView.localToScreen(atomView.getBoundsInLocal());
                final Bounds paneBoundsOnScreen = pane.localToScreen(pane.getBoundsInLocal());
                final double xInScene = boundsOnScreen.getMinX() - paneBoundsOnScreen.getMinX();
                final double yInScene = boundsOnScreen.getMinY() - paneBoundsOnScreen.getMinY();
                Label label = new Label();
                label.setTranslateX(xInScene);
                label.setTranslateY(yInScene);
                return label;
            }
        };
        return objectBinding;
    }

    private void initBindings(Pane pane) {
        translateXProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getTranslateX();
            }
        });

        translateYProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }
            @Override
            protected double computeValue() {
                return binding.get().getTranslateY();
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

    }
}
