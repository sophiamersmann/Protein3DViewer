package protein3DViewer.model;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Helix extends SecondaryStructure {

    private Character chainName;
    private String type;

    private Residue initResidue;
    private Residue endResidue;

    public Helix(String id) {
        super(id);
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

    @Override
    public String toString() {
        return "Helix{" +
                "chainName=" + chainName +
                ", type='" + type + '\'' +
                ", initResidue=" + initResidue +
                ", endResidue=" + endResidue +
                '}';
    }
}
