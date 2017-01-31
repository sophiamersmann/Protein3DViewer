package protein3DViewer.view.modelVisualization;

import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import protein3DViewer.MySelectionModel;
import protein3DViewer.model.*;
import protein3DViewer.view.ColorMode;
import protein3DViewer.view.ModelView;
import protein3DViewer.view.VisualizationMode;
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

//    private List<List<AbstractAtomView>> atomViewsPerResidue;
    private MySelectionModel<AbstractAtomView> selectionModel;
//    private MySelectionModel<List<AbstractAtomView>> selectionModel;



    public SticksVisualization(Model model, ModelView modelView) {
        super(model, modelView);
    }

    @Override
    void createBottomGroup() {
        atomViewGroup = new Group();
        bondViewGroup = new Group();
        initAtomViews();
        initBondViews();
        showAtomViews();
        showBondViews();
//        setUpSelectionModelList();
        initSelectionModel();
        bottomGroup.getChildren().addAll(bondViewGroup, atomViewGroup);
    }

//    private void setUpSelectionModelList() {
//        atomViewsPerResidue = new ArrayList<>();
//        for (Chain chain: model.getChains().values()) {
//            for (Residue residue: chain.getResidues().values()) {
//                List<AbstractAtomView> views = new ArrayList<>();
//                for (int atomID: residue.getAtoms().keySet()) {
//                    views.add(atomViews.get(atomID));
//                }
//                atomViewsPerResidue.add(views);
//            }
//        }
//    }

    @Override
    void createTopGroup() {

    }

    private void initSelectionModel() {
        List<AbstractAtomView> atomViewsList = new ArrayList<>(atomViews.values());
        selectionModel = new MySelectionModel(atomViewsList.toArray());
        selectionModel.getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
            @Override
            public void onChanged(Change<? extends AbstractAtomView> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (AbstractAtomView atomView : c.getAddedSubList()) {
//                               atomView.addMarker();
                            BoundingBox bb = new BoundingBox(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty());
                            atomView.setBoundingBox(bb);
                            modelView.getTopPane().getChildren().add(bb);
//                            atomView.setColor(Color.BLACK);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (AbstractAtomView atomView : c.getRemoved()) {
                            modelView.getTopPane().getChildren().remove(atomView.getBoundingBox());
//                            atomView.resetColor();
//                               atomView.removeMarker();
                        }
                    }
                }
            }
        });


    }

//    private void initSelectionModel() {
//        selectionModel = new MySelectionModel(atomViewsPerResidue.toArray());
//        selectionModel.getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
//            @Override
//            public void onChanged(Change<? extends AbstractAtomView> c) {
//                while (c.next()) {
//                    if (c.wasAdded()) {
//                        for (AbstractAtomView atomView : c.getAddedSubList()) {
////                               atomView.addMarker();
//                            BoundingBox bb = new BoundingBox(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty());
//                            atomView.setBoundingBox(bb);
//                            modelView.getTopPane().getChildren().add(bb);
////                            atomView.setColor(Color.BLACK);
//                        }
//                    }
//                    if (c.wasRemoved()) {
//                        for (AbstractAtomView atomView : c.getRemoved()) {
//                            modelView.getTopPane().getChildren().remove(atomView.getBoundingBox());
////                            atomView.resetColor();
////                               atomView.removeMarker();
//                        }
//                    }
//                }
//            }
//        });
//
//
//    }

//    private void initSelectionModel() {
//        selectionModel = new MySelectionModel(atomViewsPerResidue.toArray());
//        selectionModel.getSelectedItems().addListener(new ListChangeListener<List<AbstractAtomView>>() {
//            @Override
//            public void onChanged(Change<? extends List<AbstractAtomView>> c) {
//                while (c.next()) {
//                    if (c.wasAdded()) {
//                        for (List<AbstractAtomView> atomViews : c.getAddedSubList()) {
////                               atomView.addMarker();
//                            for (AbstractAtomView atomView: atomViews) {
//                                BoundingBox bb = new BoundingBox(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty());
//                                atomView.setBoundingBox(bb);
//                                modelView.getTopPane().getChildren().add(bb);
//                            }
//
////                            atomView.setColor(Color.BLACK);
//                        }
//                    }
//                    if (c.wasRemoved()) {
//                        for (List<AbstractAtomView> atomViews : c.getRemoved()) {
//                            for (AbstractAtomView atomView: atomViews) {
//                                modelView.getTopPane().getChildren().remove(atomView.getBoundingBox());
//                            }
//
////                            atomView.resetColor();
////                               atomView.removeMarker();
//                        }
//                    }
//                }
//            }
//        });
//
//
//    }

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
            atomView.setColor(color);
        }
    }

    public Map<Integer, AbstractAtomView> getAtomViews() {
        return atomViews;
    }

//    public MySelectionModel<List<AbstractAtomView>> getSelectionModel() {
//        return selectionModel;
//    }
//
//    public List<List<AbstractAtomView>> getAtomViewsPerResidue() {
//        return atomViewsPerResidue;
//    }


    public MySelectionModel<AbstractAtomView> getSelectionModel() {
        return selectionModel;
    }
}
