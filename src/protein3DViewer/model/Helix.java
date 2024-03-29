package protein3DViewer.model;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Helix {

    private String id;
    private Character chainName;
    private String type;

    private Residue initResidue;
    private Residue endResidue;

    /**
     * tells if a given residue lies within the helix
     *
     * @param residue
     * @return true, if the given residue lies within the helix
     */
    public boolean hasResidue(Residue residue) {
        if (residue.getId() >= initResidue.getId() && residue.getId() <= endResidue.getId()) {
            return true;
        }
        return false;
    }

    public Helix(String id) {
        this.id = id;
    }

    public Character getChainName() {
        return chainName;
    }

    public void setChainName(Character chainName) {
        this.chainName = chainName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Residue getInitResidue() {
        return initResidue;
    }

    public void setInitResidue(Residue initResidue) {
        this.initResidue = initResidue;
    }

    public Residue getEndResidue() {
        return endResidue;
    }

    public void setEndResidue(Residue endResidue) {
        this.endResidue = endResidue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Helix{" +
                "id='" + id + '\'' +
                ", chainName=" + chainName +
                ", type='" + type + '\'' +
                ", initResidue=" + initResidue +
                ", endResidue=" + endResidue +
                '}';
    }
}
