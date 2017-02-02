package protein3DViewer.view.modelVisualization;

import javafx.scene.Group;
import protein3DViewer.model.Model;
import protein3DViewer.view.ModelView;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public abstract class AbstractModelVisualization {

    Model model;
    ModelView modelView;

    Group bottomGroup = new Group();

    public AbstractModelVisualization(Model model, ModelView modelView) {
        this.model = model;
        this.modelView = modelView;
        createBottomGroup();
    }

    /**
     * create group of 3D elements to be displayed
     */
    abstract void createBottomGroup();

    public Group getBottomGroup() {
        return bottomGroup;
    }

    public void setBottomGroup(Group bottomGroup) {
        this.bottomGroup = bottomGroup;
    }

}
