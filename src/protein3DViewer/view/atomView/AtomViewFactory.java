package protein3DViewer.view.atomView;

import protein3DViewer.model.Atom;
import protein3DViewer.model.AtomName;

/**
 * Created by sophiamersmann on 22/01/2017.
 */
public class AtomViewFactory {

    public static AbstractAtomView createAtomView(Atom atom) {
        if (atom.getName() == AtomName.NITROGEN) {
            return new NitrogenView(atom);
        } else if (atom.getName() == AtomName.OXYGEN) {
            return new OxygenView(atom);
        } else {
            return new CarbonView(atom);
        }
    }

}
