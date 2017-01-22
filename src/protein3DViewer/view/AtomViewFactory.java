package protein3DViewer.view;

import protein3DViewer.model.Atom;

/**
 * Created by sophiamersmann on 22/01/2017.
 */
public class AtomViewFactory {

    public AtomView createAtomView(Atom atom) {
        if (atom.getName().equals("N")) {
            return new NitrogenView(atom);
        } else if (atom.getName().equals("O")) {
            return new OxygenView(atom);
        } else {
            return new CarbonView(atom);
        }
    }

}
