package protein3DViewer.view.modelVisualization;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import protein3DViewer.model.AtomName;
import protein3DViewer.model.Helix;
import protein3DViewer.model.Model;
import protein3DViewer.view.atomView.AbstractAtomView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class CartoonVisualization extends AbstractModelVisualization {

    private List<Cylinder> helices;
    private List<MeshView> strands;

    public CartoonVisualization(Model model) {
        super(model);
    }

    @Override
    void createBottomGroup() {
        createHelixViews();
        createStrandViews();
        bottomGroup.getChildren().addAll(helices);
        bottomGroup.getChildren().addAll(strands);
    }

    private void createHelixViews() {
        helices = new ArrayList<>();
        for (Helix helix: model.getProtein().getSecondaryStructure().getHelices().values()) {
            Cylinder cylinder = new Cylinder();
            Point3D initPoint = new Point3D(
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getX(),
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getY(),
                    helix.getInitResidue().getAtom(AtomName.CARBON_ALPHA).getZ()
            );
            Point3D endPoint = new Point3D(
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getX(),
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getY(),
                    helix.getEndResidue().getAtom(AtomName.CARBON_ALPHA).getZ()
            );
            Point3D direction = endPoint.subtract(initPoint);
            Point3D midPoint = endPoint.midpoint(initPoint);
            Point3D yAxis = new Point3D(0, 1, 0);
            Point3D axisOfRotation = direction.crossProduct(yAxis);
            double angle = yAxis.angle(direction);

            cylinder.setTranslateX(midPoint.getX());
            cylinder.setTranslateY(midPoint.getY());
            cylinder.setTranslateZ(midPoint.getZ());
            cylinder.setRotationAxis(axisOfRotation);
            cylinder.setRotate(-angle);
            cylinder.setHeight(direction.magnitude());
            cylinder.setRadius(1);
            cylinder.setMaterial(new PhongMaterial(AbstractAtomView.HELIX_COLOR));
            helices.add(cylinder);
        }
    }

    private void createStrandViews() {
        strands = new ArrayList<>();
    }

    @Override
    void createTopGroup() {

    }
}
