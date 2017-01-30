package protein3DViewer.view.modelVisualization;

import protein3DViewer.model.Model;
import protein3DViewer.view.VisualizationMode;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class ModelVisualizationFactory {

    public static AbstractModelVisualization createModelVisualization(Model model, VisualizationMode mode) {
        if (mode == VisualizationMode.STICKS) {
            return new SticksVisualization(model);
        } else if (mode == VisualizationMode.RIBBON) {
            return new RibbonVisualization(model);
        } else if (mode == VisualizationMode.CARTOON) {
            return new CartoonVisualization(model);
        }
        return null;
    }

}
