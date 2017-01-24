package protein3DViewer.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import protein3DViewer.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 23/01/2017.
 */
public class SticksVisualization extends ModelVisualization {

    private Map<Integer, AtomView> atomViews;
    private List<BondView> bondViews;

    private Group atomViewGroup;
    private Group bondViewGroup;

    public SticksVisualization(Model model) {
        super(model);
    }

    @Override
    void createBottomChildren() {
        atomViewGroup = new Group();
        bondViewGroup = new Group();
        initAtomViews();
        initBondViews();
        showAtomViews();
        showBondViews();
        bottomChildren.add(bondViewGroup);
        bottomChildren.add(atomViewGroup);
    }

    @Override
    void createTopChildren() {
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
        for (AtomView atomView : atomViews.values()) {
            atomView.changeRadius(factor);
        }
    }

    public void changeBondSize(Double factor) {
        for (BondView bondView : bondViews) {
            bondView.changeRadius(factor);
        }
    }

    public void changeAtomColor(String mode) {
        for (AtomView atomView : atomViews.values()) {
            Color color = atomView.getColor(mode);  // TODO getColor static?
            atomView.setMaterial(color);
        }
    }

}
