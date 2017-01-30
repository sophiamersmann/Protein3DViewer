package protein3DViewer.view.atomView;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import protein3DViewer.model.Atom;
import protein3DViewer.view.ColorMode;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public abstract class AbstractAtomView extends Group {

    public final static Color DEFAULT_COLOR = Color.GREY;

    public final static Color HELIX_COLOR = Color.GREEN;
    public final static Color SHEET_COLOR = Color.YELLOW;

    public final static Color SMALL_HYDROPHOBIC_COLOR = Color.SALMON;
    public final static Color LARGE_HYDROPHOBIC_COLOR = Color.LIGHTBLUE;
    public final static Color POLAR_COLOR = Color.PALEGREEN;
    public final static Color POS_CHARGED_COLOR = Color.ORANGERED;
    public final static Color NEG_CHARGED_COLOR = Color.NAVAJOWHITE;

    Atom atom;
    Sphere shape;

    DoubleProperty x = new SimpleDoubleProperty();
    DoubleProperty y = new SimpleDoubleProperty();
    DoubleProperty z = new SimpleDoubleProperty();

    public AbstractAtomView(Atom atom) {
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

    public Color getColor(ColorMode mode) {
        switch (mode) {
            case UNICOLOR: return DEFAULT_COLOR;
            case BY_ATOM_TYPE: return getAminoAcidColor();
            case BY_SECONDARY_STRUCTURE: return getSecondaryStructureColor();
            case BY_PROPERTIES: return getPropertyColor();
            default: return DEFAULT_COLOR;
        }
    }

    private Color getAminoAcidColor() {
        if (this instanceof CarbonView) {
            return CarbonView.COLOR;
        } else if (this instanceof NitrogenView) {
            return NitrogenView.COLOR;
        } else if (this instanceof OxygenView) {
            return OxygenView.COLOR;
        } else {
            return DEFAULT_COLOR;
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
        switch (AminoAcidGroup.aminoAcidGroupOf(atom.getResidue().getName())) {
            case SMALL_HYDROPHOBIC: return SMALL_HYDROPHOBIC_COLOR;
            case LARGE_HYDROPHOBIC: return LARGE_HYDROPHOBIC_COLOR;
            case POLAR: return POLAR_COLOR;
            case POS_CHARGED: return POS_CHARGED_COLOR;
            case NEG_CHARGED: return NEG_CHARGED_COLOR;
            default: return DEFAULT_COLOR;
        }
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public double getZ() {
        return z.get();
    }

    public DoubleProperty zProperty() {
        return z;
    }

    public void setZ(double z) {
        this.z.set(z);
    }

    @Override
    public String toString() {
        return "AbstractAtomView{" +
                "atom=" + atom +
                ", shape=" + shape +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
