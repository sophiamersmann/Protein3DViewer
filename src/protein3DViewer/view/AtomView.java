package protein3DViewer.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import protein3DViewer.model.Atom;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public abstract class AtomView extends Group {

    public final static String COLOR_BY_AMINO_ACID = "amino acid";
    public final static String COLOR_BY_SECONDARY_STRUCTURE = "secondary structure";
    public final static String COLOR_BY_PROPERTIES = "physicochemical properties";

    public final static Color DEFAULT_COLOR = Color.GREY;
    public final static Color HELIX_COLOR = Color.GREEN;
    public final static Color SHEET_COLOR = Color.YELLOW;

    Atom atom;
    Sphere shape;

    DoubleProperty x = new SimpleDoubleProperty();
    DoubleProperty y = new SimpleDoubleProperty();
    DoubleProperty z = new SimpleDoubleProperty();

    public AtomView(Atom atom) {
        this.atom = atom;
        shape = new Sphere();
        setTranslateX(atom.getX());
        setTranslateY(atom.getY());
        setTranslateZ(atom.getZ());
        this.atom.xProperty().bindBidirectional(x);
        this.atom.yProperty().bindBidirectional(y);
        this.atom.zProperty().bindBidirectional(z);
        x.bindBidirectional(translateXProperty());
        y.bindBidirectional(translateYProperty());
        z.bindBidirectional(translateZProperty());
        getChildren().add(shape);
    }

    public void setDefaultRadius(Integer scaleFactor) {
        shape.setRadius(1 / (scaleFactor * 0.1));
    }

    public void setMaterial(Color color) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color);
        material.setSpecularColor(Color.BLACK);
        shape.setMaterial(material);
    }

    public void changeRadius(Double factor) {
        shape.setRadius(shape.getRadius() + factor);
    }

    public Color getColor(String mode) {
        if (mode.equals(COLOR_BY_AMINO_ACID)) {
            return getAminoAcidColor();
        } else if (mode.equals(COLOR_BY_SECONDARY_STRUCTURE)) {
            return getSecondaryStructureColor();
        } else if (mode.equals(COLOR_BY_PROPERTIES)) {
            return getPropertyColor();
        } else {
            System.err.println(mode + " not implemented.");
            System.exit(1);
        }
        return null;
    }

    private Color getAminoAcidColor() {
        if (this instanceof CarbonView) {
            return CarbonView.COLOR;
        } else if (this instanceof NitrogenView) {
            return NitrogenView.COLOR;
        } else if (this instanceof OxygenView) {
            return OxygenView.COLOR;
        } else {
            System.err.println("Coloring not successful.");
            System.exit(1);
            return null;
        }
    }

    private Color getSecondaryStructureColor() {
        if (atom.getResidue().isInHelix()) {
            return HELIX_COLOR;
        } else if (atom.getResidue().isInSheet()) {
            return SHEET_COLOR;
        } else {
            return DEFAULT_COLOR;
        }
    }

    private Color getPropertyColor() {
        return null; // TODO: implement
    }


    @Override
    public String toString() {
        return "AtomView{" +
                "atom=" + atom +
                ", shape=" + shape +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
