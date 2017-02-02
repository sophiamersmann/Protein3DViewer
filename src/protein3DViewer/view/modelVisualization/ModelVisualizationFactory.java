package protein3DViewer.view.modelVisualization;

import protein3DViewer.model.Model;
import protein3DViewer.view.ModelView;
import protein3DViewer.view.VisualizationMode;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class ModelVisualizationFactory {

    /**
     * create model visualization (sticks, ribbon or cartoon)
     *
     * @param model model
     * @param modelView model view
     * @param mode model visualization mode
     * @return model visualization
     */
    public static AbstractModelVisualization createModelVisualization(Model model, ModelView modelView, VisualizationMode mode) {
        if (mode == VisualizationMode.STICKS) {
            return new SticksVisualization(model, modelView);
        } else if (mode == VisualizationMode.RIBBON) {
            return new RibbonVisualization(model, modelView);
        } else if (mode == VisualizationMode.CARTOON) {
            return new CartoonVisualization(model, modelView);
        }
        return null;
    }

}
