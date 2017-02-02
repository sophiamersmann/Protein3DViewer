package protein3DViewer.view.modelVisualization;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import protein3DViewer.MySelectionModel;
import protein3DViewer.model.*;
import protein3DViewer.view.ColorMode;
import protein3DViewer.view.ModelView;
import protein3DViewer.view.VisualizationMode;
import protein3DViewer.view.atomView.CarbonView;
import protein3DViewer.view.bondView.BondView;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.atomView.AtomViewFactory;
import protein3DViewer.view.bondView.Line;

import java.text.DecimalFormat;
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

//    private List<List<AbstractAtomView>> atomViewsPerResidue;
    private MySelectionModel<AbstractAtomView> selectionModel;
//    private MySelectionModel<List<AbstractAtomView>> selectionModel;

    private MySelectionModel<AbstractAtomView> distanceSelectionModel;

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
//        setUpSelectionModelList();
        initSelectionModel();
        initDistanceSelectionModel();
        bottomGroup.getChildren().addAll(bondViewGroup, atomViewGroup, connectionGroup, pseudoGroup);
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
//        for (AbstractAtomView atomView: atomViews.values()) {
//            topGroup.getChildren().add(atomView.getLabel());
//        }
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
                            AtomLabel atomLabel = new AtomLabel(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty(),
                                    atomView.getAtom().getResidue().getName3() + atomView.getAtom().getResidue().getId());

                            boolean labelAlreadySet = false;
                            for (int atomID: atomView.getAtom().getResidue().getAtoms().keySet()) {
                                if (atomViews.get(atomID).getLabel() != null) {
                                    labelAlreadySet = true;
                                    break;
                                }
                            }
                            if (!labelAlreadySet) {
                                atomView.setLabel(atomLabel);
                                modelView.getTopPane().getChildren().add(atomLabel);
                            }


//                            atomView.setColor(Color.BLACK);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (AbstractAtomView atomView : c.getRemoved()) {

                            modelView.getTopPane().getChildren().remove(atomView.getBoundingBox());
                            if (atomView.getLabel() != null) {
                                modelView.getTopPane().getChildren().remove(atomView.getLabel());
                                atomView.setLabel(null);
                            }

//                            atomView.resetColor();
//                               atomView.removeMarker();
                        }
                    }
                }
            }
        });


    }


    private void initDistanceSelectionModel() {
        List<AbstractAtomView> atomViewsList = new ArrayList<>(atomViews.values());
        distanceSelectionModel = new MySelectionModel(atomViewsList.toArray());
        distanceSelectionModel.getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
            @Override
            public void onChanged(Change<? extends AbstractAtomView> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (AbstractAtomView atomView : c.getAddedSubList()) {
                            BoundingBox bb = new BoundingBox(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty());
                            atomView.setBoundingBox(bb);
                            modelView.getTopPane().getChildren().add(bb);

                            AtomLabel atomLabel = new AtomLabel(atomView, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty(),
                                    atomView.getAtom().getResidue().getName3() + atomView.getAtom().getResidue().getId() + " (" + atomView.getAtom().getName().getPdbName() + ")");
                            atomView.setLabel(atomLabel);
                            modelView.getTopPane().getChildren().add(atomLabel);

//                            if (distanceSelectionModel.getSelectedItems().size() % 2 == 0) {  // TODO not good
//                                AbstractAtomView prevAtomView = distanceSelectionModel.getSelectedItems().get(distanceSelectionModel.getSelectedItems().size() - 2);
//                                Line connection = new Line(prevAtomView.getX(), prevAtomView.getY(), prevAtomView.getZ(),
//                                        atomView.getX(), atomView.getY(), atomView.getZ()
//                                );
//                                connection.setMaterial(new PhongMaterial(Color.BLACK));
//                                connectionGroup.getChildren().add(connection);
//
//                                Point3D start = new Point3D(connection.getStartX(), connection.getStartY(), connection.getStartZ());
//                                Point3D end = new Point3D(connection.getEndX(), connection.getEndY(), connection.getEndZ());
//                                Point3D midPoint = end.midpoint(start);
//                                Atom pseudoAtom = new Atom(-1, "", "");
//                                pseudoAtom.setX(midPoint.getX());
//                                pseudoAtom.setY(midPoint.getY());
//                                pseudoAtom.setZ(midPoint.getZ());
//                                AbstractAtomView pseudo = new CarbonView(pseudoAtom);
//                                pseudo.setVisible(false);
//                                pseudoGroup.getChildren().add(pseudo);
//
//                                DecimalFormat df = new DecimalFormat("#.##");
//                                AtomLabel distanceLabel = new AtomLabel(pseudo, modelView.getBottomPane(), modelView.getBottomGroup().worldTransformProperty(),
//                                        df.format(Math.abs(end.subtract(start).magnitude())) + "A");
//                                modelView.getTopPane().getChildren().add(distanceLabel);
//
//                            }
                        }
                    }
                    if (c.wasRemoved()) {
                        for (AbstractAtomView atomView: c.getRemoved()) {
                            modelView.getTopPane().getChildren().removeAll(atomView.getBoundingBox(), atomView.getLabel());
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
//            atomView.changeRadius(factor);
            atomView.changeSize(factor);
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

    public MySelectionModel<AbstractAtomView> getDistanceSelectionModel() {
        return distanceSelectionModel;
    }

    public Group getConnectionGroup() {
        return connectionGroup;
    }

    public Group getPseudoGroup() {
        return pseudoGroup;
    }
}
