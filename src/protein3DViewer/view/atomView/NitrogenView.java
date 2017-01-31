package protein3DViewer.view.atomView;

import javafx.scene.paint.Color;
import protein3DViewer.model.Atom;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class NitrogenView extends AbstractAtomView {

    final static Integer RADIUS = 65;
    final static Color COLOR = Color.BLUE;

    public NitrogenView(Atom atom) {
        super(atom);
        setDefaultRadius(RADIUS);
        setColor(COLOR);
    }


}
