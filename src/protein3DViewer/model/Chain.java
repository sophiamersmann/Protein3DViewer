package protein3DViewer.model;

import java.util.*;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Chain {

    private Character name;
    private Map<Integer, Residue> residues = new HashMap<>();
    private List<Bond> bonds = new ArrayList<>();

    public Chain(Character name) {
        this.name = name;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    public Map<Integer, Residue> getResidues() {
        return residues;
    }

    public Residue getResidue(Integer key) {
        return residues.get(key);
    }

    public void setResidues(Map<Integer, Residue> residues) {
        this.residues = residues;
    }

    public void setResidue(Integer key, Residue residue) {
        residues.put(key, residue);
    }

    public List<Bond> getBonds() {
        return bonds;
    }

    public void setBonds(List<Bond> bonds) {
        this.bonds = bonds;
    }

    public void createBonds() {
        // bonds within a residue
        for (Residue res : residues.values()) {
            createBond(res, "N", res, "CA");
            createBond(res, "CA", res, "CB");
            createBond(res, "CA", res, "C");
            createBond(res, "C", res, "O");
        }
        // bonds between neighbouring residues
        List<Integer> residueIDs = new ArrayList<>(residues.keySet());
        Collections.sort(residueIDs);
        for (int i = 0; i < residueIDs.size() - 1; i++) {
            Residue startResidue = residues.get(residueIDs.get(i));
            Residue endResidue = residues.get(residueIDs.get(i + 1));
            createBond(startResidue, "C", endResidue, "N");
        }
    }

    private void createBond(Residue startResidue, String startAtomName, Residue endResidue, String endAtomName) {
        Atom startAtom = startResidue.getAtom(startAtomName);
        Atom endAtom = endResidue.getAtom(endAtomName);
        if (startAtom != null && endAtom != null) {
            bonds.add(new Bond(startAtom, endAtom));
        }
    }

    @Override
    public String toString() {
        return "Chain{" +
                "name=" + name +
                ", residues=" + residues +
                ", bonds=" + bonds +
                '}';
    }
}
