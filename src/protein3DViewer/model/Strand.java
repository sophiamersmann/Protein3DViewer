package protein3DViewer.model;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Strand {

    private Integer id;
    private Character chainName;
    private Integer sense;

    private Residue initResidue;
    private Residue endResidue;

    public Strand(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getChainName() {
        return chainName;
    }

    public void setChainName(Character chainName) {
        this.chainName = chainName;
    }

    public Integer getSense() {
        return sense;
    }

    public void setSense(Integer sense) {
        this.sense = sense;
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
        return "Strand{" +
                "id=" + id +
                ", chainName=" + chainName +
                ", sense=" + sense +
                ", initResidue=" + initResidue +
                ", endResidue=" + endResidue +
                '}';
    }
}
