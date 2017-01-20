package protein3DViewer.model;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Bond {

    private Atom startAtom;
    private Atom endAtom;

    public Bond(Atom startAtom, Atom endAtom) {
        this.startAtom = startAtom;
        this.endAtom = endAtom;
    }

    public Atom getStartAtom() {
        return startAtom;
    }

    public void setStartAtom(Atom startAtom) {
        this.startAtom = startAtom;
    }

    public Atom getEndAtom() {
        return endAtom;
    }

    public void setEndAtom(Atom endAtom) {
        this.endAtom = endAtom;
    }

    @Override
    public String toString() {
        return "Bond{" +
                "startAtom=" + startAtom +
                ", endAtom=" + endAtom +
                '}';
    }
}
