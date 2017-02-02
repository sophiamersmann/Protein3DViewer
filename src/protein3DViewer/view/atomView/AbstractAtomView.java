package protein3DViewer.view.atomView;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import protein3DViewer.model.Atom;
import protein3DViewer.view.ColorMode;
import protein3DViewer.view.ColorValue;
import protein3DViewer.view.modelVisualization.AtomLabel;
import protein3DViewer.view.modelVisualization.BoundingBox;
import protein3DViewer.view.modelVisualization.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public abstract class AbstractAtomView extends Group {

    Atom atom;
    Sphere shape;

    DoubleProperty x = new SimpleDoubleProperty();
    DoubleProperty y = new SimpleDoubleProperty();
    DoubleProperty z = new SimpleDoubleProperty();

    BooleanProperty selected = new SimpleBooleanProperty();
    BooleanProperty shiftSelected = new SimpleBooleanProperty();
    BooleanProperty altSelected = new SimpleBooleanProperty();

    BoundingBox boundingBox;
    AtomLabel label;

    List<Connection> connections = new ArrayList<>();

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

    /**
     * set default radius of atom based on atomic radius of the specific atom
     *
     * @param scaleFactor atomic radius
     */
    public void setDefaultRadius(Integer scaleFactor) {
        shape.setRadius(1 / (scaleFactor * 0.1));
    }

    /**
     * increase/decrease size of the atom
     *
     * @param factor factor by which atom size is changed
     */
    public void changeSize(Double factor) {
        setScaleX(getScaleX() + factor);
        setScaleY(getScaleY() + factor);
        setScaleZ(getScaleZ() + factor);
    }

    /**
     * given a specific color mode, get the corresponding color of the atom
     *
     * @param mode color mode
     * @return color of the atom
     */
    public Color getColor(ColorMode mode) {
        switch (mode) {
            case UNICOLOR:
                return ColorValue.DEFAULT.getColor();
            case BY_ATOM_TYPE:
                return getAtomTypeColor();
            case BY_SECONDARY_STRUCTURE:
                return getSecondaryStructureColor();
            case BY_PROPERTIES:
                return getPropertyColor();
            default:
                return ColorValue.DEFAULT.getColor();
        }
    }

    /**
     * get color of atom, if color mode is by atom type
     *
     * @return color of the atom
     */
    private Color getAtomTypeColor() {
        if (this instanceof CarbonView) {
            return CarbonView.COLOR;
        } else if (this instanceof NitrogenView) {
            return NitrogenView.COLOR;
        } else if (this instanceof OxygenView) {
            return OxygenView.COLOR;
        } else {
            return ColorValue.DEFAULT.getColor();
        }
    }

    /**
     * get color of atom, if color mode is by secondary structure
     *
     * @return color of the atom
     */
    private Color getSecondaryStructureColor() {
        if (atom.getResidue().isInHelix()) {
            return ColorValue.HELIX.getColor();
        } else if (atom.getResidue().isInSheet()) {
            return ColorValue.SHEET.getColor();
        } else {
            return ColorValue.LOOPS.getColor();
        }
    }

    /**
     * get color of atom, if color mode is by properties
     *
     * @return color ot the atom
     */
    private Color getPropertyColor() {
        switch (AminoAcidGroup.aminoAcidGroupOf(atom.getResidue().getName())) {
            case SMALL_HYDROPHOBIC:
                return ColorValue.SMALL_HYDROPHOBIC.getColor();
            case LARGE_HYDROPHOBIC:
                return ColorValue.LARGE_HYDROPHOBIC.getColor();
            case POLAR:
                return ColorValue.POLAR.getColor();
            case POS_CHARGED:
                return ColorValue.POSITIVE_CHARGED.getColor();
            case NEG_CHARGED:
                return ColorValue.NEGATIVE_CHARGED.getColor();
            default:
                return ColorValue.DEFAULT.getColor();
        }
    }

    public void setColor(Color color) {
        shape.setMaterial(new PhongMaterial(color));
    }

    public double getRadius() {
        return shape.getRadius();
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Atom getAtom() {
        return atom;
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

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean isShiftSelected() {
        return shiftSelected.get();
    }

    public BooleanProperty shiftSelectedProperty() {
        return shiftSelected;
    }

    public void setShiftSelected(boolean shiftSelected) {
        this.shiftSelected.set(shiftSelected);
    }

    public AtomLabel getLabel() {
        return label;
    }

    public void setLabel(AtomLabel label) {
        this.label = label;
    }

    public boolean isAltSelected() {
        return altSelected.get();
    }

    public BooleanProperty altSelectedProperty() {
        return altSelected;
    }

    public void setAltSelected(boolean altSelected) {
        this.altSelected.set(altSelected);
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
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
