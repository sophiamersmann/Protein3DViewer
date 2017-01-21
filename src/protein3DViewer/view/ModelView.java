package protein3DViewer.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import protein3DViewer.presenter.ModelPresenter;
import protein3DViewer.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ModelView extends Group {

    private Model model;

    private Map<Integer, AtomView> atomViews = new HashMap<>();

    private Group atomViewGroup = new Group();

    public ModelView(ModelPresenter modelPresenter, Model model) {
        this.model = model;
        initAtomViews();
        showAtomViews();
        getChildren().add(atomViewGroup);
    }

    private void initAtomViews() {
        for (Chain chain : model.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                for (Atom atom : residue.getAtoms().values()) {
                    AtomView atomView;
                    if (atom.getName().equals("N")) {
                        atomView = new AtomViewN(atom);
                    } else if (atom.getName().equals("O")) {
                        atomView = new AtomViewO(atom);
                    } else {
                        atomView = new AtomViewC(atom);
                    }
                    atomViews.put(atom.getId(), atomView);
                }
            }
        }
    }

    public void showAtomViews() {
        atomViewGroup.getChildren().addAll(atomViews.values());
    }

    public void hideAtomViews() {
        atomViewGroup.getChildren().clear();
    }

    public void changeAtomSize(Double factor) {
        for (AtomView atomView : atomViews.values()) {
            atomView.changeRadius(factor);
        }
    }

    public void changeAtomColor(String mode) {
        for (AtomView atomView : atomViews.values()) {
            Color color = atomView.getColor(mode);  // TODO getColor static?
            atomView.setMaterial(color);
        }
    }

}
