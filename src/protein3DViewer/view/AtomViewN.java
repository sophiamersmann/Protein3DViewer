package protein3DViewer.view;

import javafx.scene.paint.Color;
import protein3DViewer.model.Atom;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class AtomViewN extends AtomView {

    final static Integer RADIUS = 65;
    final static Color COLOR = Color.BLUE;

    public AtomViewN(Atom atom) {
        super(atom);
        setDefaultRadius(RADIUS);
        setMaterial(COLOR);
    }


}