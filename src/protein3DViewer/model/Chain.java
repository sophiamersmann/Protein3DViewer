package protein3DViewer.model;

import java.util.List;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Chain {

    private Character name;

    private List<Residue> residues;

    public Chain(Character name) {
        this.name = name;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    public List<Residue> getResidues() {
        return residues;
    }

    public void setResidues(List<Residue> residues) {
        this.residues = residues;
    }

    @Override
    public String toString() {
        return "Chain{" +
                "name=" + name +
                ", residues=" + residues +
                '}';
    }
}
