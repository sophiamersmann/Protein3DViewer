package protein3DViewer.model;

import protein3DViewer.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Residue {

    private Integer id;
    private Character name;
    private String name3;
    private Map<Integer, Atom> atoms = new HashMap<>();

    public Residue(Integer id, String name3) {
        this.id = id;
        this.name = Tools.aminoAcid3to1(name3);
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

    public Map<Integer, Atom> getAtoms() {
        return atoms;
    }

    public Atom getAtom(Integer key) {
        return atoms.get(key);
    }

    public Atom getAtom(String name) {
        for (Atom atom: atoms.values()) {
            if (atom.getName() == name) {
                return atom;
            }
        }
        return null;
    }

    public void setAtoms(Map<Integer, Atom> atoms) {
        this.atoms = atoms;
    }

    public void setAtom(Integer key, Atom atom) {
        atoms.put(key, atom);
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
