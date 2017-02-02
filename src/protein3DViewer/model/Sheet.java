package protein3DViewer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Sheet {

    private String id;
    private Map<Integer, Strand> strands = new HashMap<>();

    /**
     * returns true, if a given residue lies within the helix
     *
     * @param residue
     * @return true, if the given residue is part of the sheet
     */
    public Boolean hasResidue(Residue residue) {
        for (Strand strand : strands.values()) {
            if (strand.hasResidue(residue)) {
                return true;
            }
        }
        return false;
    }

    public Sheet(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Integer, Strand> getStrands() {
        return strands;
    }

    public Strand getStrand(Integer key) {
        return strands.get(key);
    }

    public void setStrands(Map<Integer, Strand> strands) {
        this.strands = strands;
    }

    public void setStrand(Integer key, Strand strand) {
        strands.put(key, strand);
    }

    @Override
    public String toString() {
        return "Sheet{" +
                "id='" + id + '\'' +
                ", strands=" + strands +
                '}';
    }
}
