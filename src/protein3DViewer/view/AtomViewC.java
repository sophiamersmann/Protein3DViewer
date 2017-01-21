package protein3DViewer.view;

import javafx.scene.paint.Color;
import protein3DViewer.model.Atom;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class AtomViewC extends AtomView {

    final static Integer RADIUS = 70;
    final static Color COLOR = Color.GREY;

    public AtomViewC(Atom atom) {
        super(atom);
        setDefaultRadius(RADIUS);
        setMaterial(COLOR);
    }

}
