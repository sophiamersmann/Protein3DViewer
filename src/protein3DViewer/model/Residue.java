package protein3DViewer.model;

import java.util.List;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Residue {

    private Integer id;
    private Character name;
    private String name3;

    private List<Atom> atoms;

    public Residue(Integer id, Character name, String name3) {
        this.id = id;
        this.name = name;
        this.name3 = name3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public List<Atom> getAtoms() {
        return atoms;
    }

    public void setAtoms(List<Atom> atoms) {
        this.atoms = atoms;
    }

    @Override
    public String toString() {
        return "Residue{" +
                "id=" + id +
                ", name=" + name +
                ", name3='" + name3 + '\'' +
                ", atoms=" + atoms +
                '}';
    }
}
