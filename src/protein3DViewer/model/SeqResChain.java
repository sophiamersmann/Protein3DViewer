package protein3DViewer.model;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class SeqResChain {

    private Character name;
    private String sequence;

    public SeqResChain(Character name) {
        this.name = name;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "SeqResChain{" +
                "name=" + name +
                ", sequence='" + sequence + '\'' +
                '}';
    }
}
