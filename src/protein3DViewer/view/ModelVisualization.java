package protein3DViewer.view;

import javafx.scene.Node;
import protein3DViewer.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public abstract class ModelVisualization {

    Model model;

    List<Node> bottomChildren = new ArrayList<>();
    List<Node> topChildren = new ArrayList<>();

    public ModelVisualization(Model model) {
        this.model = model;
        createBottomChildren();
        createTopChildren();
    }

    abstract void createBottomChildren();

    abstract void createTopChildren();

}
