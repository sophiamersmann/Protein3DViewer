package protein3DViewer.model;

import java.util.*;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Chain {

    private Character name;
    private Map<Integer, Residue> residues = new HashMap<>();
    private List<Bond> bonds = new ArrayList<>();

    /**
     * generate amino acid sequence of this chain
     *
     * @return amino acid sequence in one letter code
     */
    public String generateAminoAcidSequence() {
        StringBuilder sb = new StringBuilder();
        for (Residue residue : residues.values()) {
            sb.append(residue.getName());
        }
        return sb.toString();
    }

    /**
     * generates string of annotations for each residue in chain
     * according to its participation in a secondary structure element
     *
     * @param helixAnnotation annotation for helix residues
     * @param sheetAnnotation annotation for sheet residues
     * @param otherAnnotation annotation for the rest of residues
     * @return annotation string
     */
    public String generateSecondaryStructureAnnotations(String helixAnnotation, String sheetAnnotation, String otherAnnotation) {
        StringBuilder sb = new StringBuilder();
        for (Residue residue : residues.values()) {
            if (residue.isInHelix()) {
                sb.append(helixAnnotation);
            } else if (residue.isInSheet()) {
                sb.append(sheetAnnotation);
            } else {
                sb.append(otherAnnotation);
            }
        }
        return sb.toString();
    }

    /**
     * create all bonds of the protein backbone
     */
    public void createBonds() {
        // bonds within a residue
        for (Residue res : residues.values()) {
            createBond(res, AtomName.NITROGEN, res, AtomName.CARBON_ALPHA);
            createBond(res, AtomName.CARBON_ALPHA, res, AtomName.CARBON_BETA);
            createBond(res, AtomName.CARBON_ALPHA, res, AtomName.CARBON);
            createBond(res, AtomName.CARBON, res, AtomName.OXYGEN);
        }
        // bonds between neighbouring residues
        List<Integer> residueIDs = new ArrayList<>(residues.keySet());
        Collections.sort(residueIDs);
        for (int i = 0; i < residueIDs.size() - 1; i++) {
            Residue startResidue = residues.get(residueIDs.get(i));
            Residue endResidue = residues.get(residueIDs.get(i + 1));
            createBond(startResidue, AtomName.CARBON, endResidue, AtomName.NITROGEN);
        }
    }

    /**
     * create bond between two atoms
     *
     * @param startResidue  residue of the first atom
     * @param startAtomName name of the first atom
     * @param endResidue    residue of the second atom
     * @param endAtomName   name of the second atom
     */
    private void createBond(Residue startResidue, AtomName start, Residue endResidue, AtomName end) {
        Atom startAtom = startResidue.getAtom(start);
        Atom endAtom = endResidue.getAtom(end);
        if (startAtom != null && endAtom != null) {
            bonds.add(new Bond(startAtom, endAtom));
        }
    }

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

    public void setResidues(Map<Integer, Residue> residues) {
        this.residues = residues;
    }

    public Residue getResidue(Integer key) {
        return residues.get(key);
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

    @Override
    public String toString() {
        return "Chain{" +
                "name=" + name +
                ", residues=" + residues +
                ", bonds=" + bonds +
                '}';
    }
}
