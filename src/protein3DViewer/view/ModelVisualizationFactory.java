package protein3DViewer.view;

import protein3DViewer.model.Model;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class ModelVisualizationFactory {

    public static ModelVisualization createModelVisualization(Model model, String mode) {
        if (mode.equals("Sticks")) {
            return new SticksVisualization(model);
        } else if (mode.equals("Ribbon")) {
            return new RibbonVisualization(model);
        } else if (mode.equals("Cartoon")) {
            return new CartoonVisualization(model);
        }
        return null;
    }

}
