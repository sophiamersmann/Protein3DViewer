package protein3DViewer.view.modelVisualization;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.Property;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Transform;
import protein3DViewer.view.bondView.Line;

/**
 * Created by sophiamersmann on 02/02/2017.
 */
public class DistanceLabel extends Label {

//    private ObjectBinding<Label> binding;
//
//    public DistanceLabel(Line line, Pane pane, Property<Transform> worldTransformProperty) {
//        binding = createAtomLabelBinding(line, pane, worldTransformProperty);
//        initBindings(pane);
//        Point3D start = new Point3D(line.getStartX(), line.getStartY(), line.getStartZ());
//        Point3D end = new Point3D(line.getEndX(), line.getEndY(), line.getEndZ());
//        setText(end.subtract(start).magnitude() + "A");
//        setTextFill(Color.BLACK);
//        setMouseTransparent(true);
//        setStyle("-fx-font-weight: bold");
//        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, new Insets(0))));
//    }
//
//    private ObjectBinding<Label> createAtomLabelBinding(Line line, Pane pane, Property<Transform> worldTransformProperty) {
//        ObjectBinding<Label> objectBinding = new ObjectBinding<Label>() {
//            @Override
//            protected Label computeValue() {
//                bind(worldTransformProperty);
//                Point3D start = new Point3D(line.getStartX(), line.getStartY(), line.getStartZ());
//                Point3D end = new Point3D(line.getEndX(), line.getEndY(), line.getEndZ());
//                Point3D midPoint = end.midpoint(start);
//                Sphere pseudo = new Sphere();
//                pseudo.setTranslateX(midPoint.getX());
//                pseudo.setTranslateY();
//                bind(midPoint, atomView.translateYProperty());
//                bind(atomView.scaleXProperty(), atomView.scaleYProperty());
////                bind(pane.widthProperty(), pane.heightProperty());
//                final Bounds boundsOnScreen = atomView.localToScreen(atomView.getBoundsInLocal());
//                final Bounds paneBoundsOnScreen = pane.localToScreen(pane.getBoundsInLocal());
//                final double xInScene = boundsOnScreen.getMinX() - paneBoundsOnScreen.getMinX();
//                final double yInScene = boundsOnScreen.getMinY() - paneBoundsOnScreen.getMinY();
//                Label label = new Label();
//                label.setTranslateX(xInScene);
//                label.setTranslateY(yInScene);
////                label.setPrefSize(boundsOnScreen.getWidth(), boundsOnScreen.getHeight());
//                return label;
//            }
//        };
//        return objectBinding;
//
//    }
}
