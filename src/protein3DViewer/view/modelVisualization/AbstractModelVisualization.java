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
    Group topGroup = new Group();

    public AbstractModelVisualization(Model model, ModelView modelView) {
        this.model = model;
        this.modelView = modelView;
        createBottomGroup();
        createTopGroup();
    }

    abstract void createBottomGroup();

    abstract void createTopGroup();

    public Group getBottomGroup() {
        return bottomGroup;
    }

    public void setBottomGroup(Group bottomGroup) {
        this.bottomGroup = bottomGroup;
    }

    public Group getTopGroup() {
        return topGroup;
    }

    public void setTopGroup(Group topGroup) {
        this.topGroup = topGroup;
    }
}
