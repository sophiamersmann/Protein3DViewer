package protein3DViewer.model;


import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Atom {

    private Integer id;
    private String name;
    private String element;

    private Double x;
    private Double y;
    private Double z;

    private List<Bond> inBonds;
    private List<Bond> outBonds;

    public Atom(Integer id, String name, String element) {
        this.id = id;
        this.name = name;
        this.element = element;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public List<Bond> getInBonds() {
        return inBonds;
    }

    public void setInBonds(List<Bond> inBonds) {
        this.inBonds = inBonds;
    }

    public List<Bond> getOutBonds() {
        return outBonds;
    }

    public void setOutBonds(List<Bond> outBonds) {
        this.outBonds = outBonds;
    }

    @Override
    public String toString() {
        return "Atom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", element='" + element + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
