package protein3DViewer.view;

import javafx.scene.Group;
import protein3DViewer.model.Atom;
import protein3DViewer.model.Bond;

/**
 * Created by sophiamersmann on 22/01/2017.
 */
public class BondView extends Group {

    private Bond bond;
    private Line line;

    public BondView(Bond bond) {
        this.bond = bond;
        line = new Line(bond.getStartAtom().getX(), bond.getStartAtom().getY(), bond.getStartAtom().getZ(),
                bond.getEndAtom().getX(), bond.getEndAtom().getY(), bond.getEndAtom().getZ());
        this.bond.getStartAtom().xProperty().bindBidirectional(line.startXProperty());
        this.bond.getStartAtom().yProperty().bindBidirectional(line.startYProperty());
        this.bond.getStartAtom().zProperty().bindBidirectional(line.startZProperty());
        this.bond.getEndAtom().xProperty().bindBidirectional(line.endXProperty());
        this.bond.getEndAtom().yProperty().bindBidirectional(line.endYProperty());
        this.bond.getEndAtom().zProperty().bindBidirectional(line.endZProperty());
        getChildren().add(line);
    }

    public void changeRadius(Double factor) {
        line.setRadius(line.getRadius() + factor);
    }

}
