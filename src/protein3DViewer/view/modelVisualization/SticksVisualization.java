package protein3DViewer.view.modelVisualization;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import protein3DViewer.model.*;
import protein3DViewer.view.ColorMode;
import protein3DViewer.view.bondView.BondView;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.atomView.AtomViewFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class SticksVisualization extends AbstractModelVisualization {

    private Map<Integer, AbstractAtomView> atomViews;
    private List<BondView> bondViews;

    private Group atomViewGroup;
    private Group bondViewGroup;

    public SticksVisualization(Model model) {
        super(model);
    }

    @Override
    void createBottomGroup() {
        atomViewGroup = new Group();
        bondViewGroup = new Group();
        initAtomViews();
        initBondViews();
        showAtomViews();
        showBondViews();
        bottomGroup.getChildren().addAll(bondViewGroup, atomViewGroup);
    }

    @Override
    void createTopGroup() {

    }

    private void initAtomViews() {
        atomViews = new HashMap<>();
        for (Chain chain : model.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                for (Atom atom : residue.getAtoms().values()) {
                    atomViews.put(atom.getId(), AtomViewFactory.createAtomView(atom));
                }
            }
        }
    }

    private void initBondViews() {
        bondViews = new ArrayList<>();
        for (Chain chain : model.getChains().values()) {
            for (Bond bond : chain.getBonds()) {
                bondViews.add(new BondView(bond));
            }
        }
    }

    public void showAtomViews() {  // TODO: re-adding throws exception
        atomViewGroup.getChildren().addAll(atomViews.values());
    }

    public void hideAtomViews() {
        atomViewGroup.getChildren().clear();
    }

    public void showBondViews() {
        bondViewGroup.getChildren().addAll(bondViews);
    }

    public void hideBondViews() {
        bondViewGroup.getChildren().clear();
    }

    public void changeAtomSize(Double factor) {
        for (AbstractAtomView atomView : atomViews.values()) {
            atomView.changeRadius(factor);
        }
    }

    public void changeBondSize(Double factor) {
        for (BondView bondView : bondViews) {
            bondView.changeRadius(factor);
        }
    }

    public void changeAtomColor(ColorMode colorMode) {
        for (AbstractAtomView atomView : atomViews.values()) {
            Color color = atomView.getColor(colorMode);  // TODO getColor static?
            atomView.setMaterial(color);
        }
    }

    public Map<Integer, AbstractAtomView> getAtomViews() {
        return atomViews;
    }
}
