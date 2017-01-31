package protein3DViewer.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Atom {

    private Integer id;
    private AtomName name;
    private String element;

    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private DoubleProperty z = new SimpleDoubleProperty();

    private Residue residue;

    public Atom(Integer id, String name, String element) {
        this.id = id;
        this.name = AtomName.enumOf(name);
        this.element = element;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AtomName getName() {
        return name;
    }

    public void setName(AtomName name) {
        this.name = name;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
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

    public Residue getResidue() {
        return residue;
    }

    public void setResidue(Residue residue) {
        this.residue = residue;
    }

//    @Override
//    public String toString() {
//        return "Atom{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", element='" + element + '\'' +
//                ", x=" + x +
//                ", y=" + y +
//                ", z=" + z +
//                ", residue=" + residue +
//                '}';
//    }
}
