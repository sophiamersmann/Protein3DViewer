package protein3DViewer.view.modelVisualization;

import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import protein3DViewer.MySelectionModel;
import protein3DViewer.model.*;
import protein3DViewer.view.ColorMode;
import protein3DViewer.view.ModelView;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.atomView.AtomViewFactory;
import protein3DViewer.view.bondView.BondView;

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

    private Group connectionGroup;
    private Group pseudoGroup;

    private MySelectionModel<AbstractAtomView> residueSelectionModel;
    private MySelectionModel<AbstractAtomView> atomSelectionModel;

    public SticksVisualization(Model model, ModelView modelView) {
        super(model, modelView);
    }

    @Override
    void createBottomGroup() {
        atomViewGroup = new Group();
        bondViewGroup = new Group();
        connectionGroup = new Group();
        pseudoGroup = new Group();
        initAtomViews();
        initBondViews();
        showAtomViews();
        showBondViews();
        List<AbstractAtomView> atomViewsList = new ArrayList<>(atomViews.values());
        initResidueSelectionModel(atomViewsList);
        initAtomSelectionModel(atomViewsList);
        bottomGroup.getChildren().addAll(bondViewGroup, atomViewGroup, connectionGroup, pseudoGroup);
    }

    /**
     * init model keeping track of the selection of residues
     *
     * @param atomViewsList list of atom views
     */
    private void initResidueSelectionModel(List<AbstractAtomView> atomViewsList) {
        residueSelectionModel = new MySelectionModel(atomViewsList.toArray());
        residueSelectionModel.getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
            @Override
            public void onChanged(Change<? extends AbstractAtomView> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (AbstractAtomView atomView : c.getAddedSubList()) {
                            displayBoundingBox(atomView);
                            AtomLabel atomLabel = createLabel(atomView, atomView.getAtom().getResidue().getName3() + atomView.getAtom().getResidue().getId());
                            boolean labelAlreadySet = false;
                            for (int atomID : atomView.getAtom().getResidue().getAtoms().keySet()) {
                                if (atomViews.get(atomID).getLabel() != null) {
                                    labelAlreadySet = true;
                                    break;
                                }
                            }
                            if (!labelAlreadySet) {
                                atomView.setLabel(atomLabel);
                                modelView.getTopPane().getChildren().add(atomLabel);
                            }

                        }
                    }
                    if (c.wasRemoved()) {
                        for (AbstractAtomView atomView : c.getRemoved()) {
                            modelView.getTopPane().getChildren().remove(atomView.getBoundingBox());
                            if (atomView.getLabel() != null) {
                                modelView.getTopPane().getChildren().remove(atomView.getLabel());
                                atomView.setLabel(null);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * init model keeping track of atoms selected
     *
     * @param atomViewsList list of atom views
     */
    private void initAtomSelectionModel(List<AbstractAtomView> atomViewsList) {
        atomSelectionModel = new MySelectionModel(atomViewsList.toArray());
        atomSelectionModel.getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
            @Override
            public void onChanged(Change<? extends AbstractAtomView> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (AbstractAtomView atomView : c.getAddedSubList()) {
                            displayBoundingBox(atomView);
                            AtomLabel atomLabel = createLabel(atomView, atomView.getAtom().getResidue().getName3() + atomView.getAtom().getResidue().getId() + " (" + atomView.getAtom().getName().getPdbName() + ")");
                            atomView.setLabel(atomLabel);
                            modelView.getTopPane().getChildren().add(atomLabel);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (AbstractAtomView atomView : c.getRemoved()) {
                            modelView.getTopPane().getChildren().removeAll(atomView.getBoundingBox(), atomView.getLabel());
                            for (Connection connection: atomView.getConnections()) {
                                connectionGroup.getChildren().remove(connection);
                                modelView.getTopPane().getChildren().remove(connection.getLabel());
                            }
                            for (int i = atomView.getConnections().size() - 1; i >= 0; i--) {
                                Connection connection = atomView.getConnections().get(i);
                                connection.getStartAtomView().getConnections().remove(connection);
                                connection.getEndAtomView().getConnections().remove(connection);
                            }
                        }
                    }

                }
            }
        });
    }

    /**
     * display bounding box around a specific atom
     *
     * @param atomView atom view for which the bounding box is created
     */
    private void displayBoundingBox(AbstractAtomView atomView) {
        BoundingBox bb = new BoundingBox(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty());
        atomView.setBoundingBox(bb);
        modelView.getTopPane().getChildren().add(bb);
    }

    /**
     * create label for a specific atom
     *
     * @param atomView atom view for which label is created
     * @param text     text that is displayed from the label
     * @return label of atom
     */
    private AtomLabel createLabel(AbstractAtomView atomView, String text) {
        return new AtomLabel(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty(), text);
    }

    private void initAtomViews() {
        atomViews = new HashMap<>();
        for (Chain chain : model.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                for (Atom atom : residue.getAtoms().values()) {
                    AbstractAtomView atomView = AtomViewFactory.createAtomView(atom);
                    atomViews.put(atom.getId(), atomView);
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

    /**
     * change size of all atoms
     *
     * @param factor
     */
    public void changeAtomSize(Double factor) {
        for (AbstractAtomView atomView : atomViews.values()) {
            atomView.changeSize(factor);
        }
    }

    /**
     * change size of all bonds
     *
     * @param factor
     */
    public void changeBondSize(Double factor) {
        for (BondView bondView : bondViews) {
            bondView.changeRadius(factor);
        }
    }

    /**
     * change color of all atoms according to specific color mode
     *
     * @param colorMode
     */
    public void changeAtomColor(ColorMode colorMode) {
        for (AbstractAtomView atomView : atomViews.values()) {
            Color color = atomView.getColor(colorMode);  // TODO getColor static?
            atomView.setColor(color);
        }
    }

    /**
     * deselect all atoms
     */
    public void clearSelection() {
        atomViews.values().forEach(atomView -> atomView.setSelected(false));
        atomViews.values().forEach(atomView -> atomView.setShiftSelected(false));
        atomViews.values().forEach(atomView -> atomView.setAltSelected(false));
        connectionGroup.getChildren().clear();
        pseudoGroup.getChildren().clear();
    }

    public Map<Integer, AbstractAtomView> getAtomViews() {
        return atomViews;
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

    public MySelectionModel<AbstractAtomView> getResidueSelectionModel() {
        return residueSelectionModel;
    }

    public MySelectionModel<AbstractAtomView> getAtomSelectionModel() {
        return atomSelectionModel;
    }

    public Group getConnectionGroup() {
        return connectionGroup;
    }

    public Group getPseudoGroup() {
        return pseudoGroup;
    }
}
