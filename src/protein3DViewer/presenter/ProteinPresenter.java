package protein3DViewer.presenter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import protein3DViewer.model.Protein;
import protein3DViewer.view.AtomView;
import protein3DViewer.view.ProteinView;

import java.io.File;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ProteinPresenter {

    private ProteinView proteinView;
    private Protein protein;

    public ProteinPresenter(ProteinView proteinView, Protein protein) {
        this.proteinView = proteinView;
        this.protein = protein;
        setUpMenuBar();
        setUpToolBar();
    }

    private void setUpMenuBar() {

        proteinView.getOpen().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDB files (*.pdb)", "*.pdb");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(proteinView.getModelView().getScene().getWindow());
                // TODO load new file (open new window or clear protein?)
            }
        });

        proteinView.getColorByAminoAcid().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                handleColorByAminoAcid();
            }
        });

        proteinView.getColorBySecondaryStructure().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                handleColorBySecondaryStructure();
            }
        });

        proteinView.getShowAtomsMenu().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleShowAtoms(oldValue, newValue);
            }
        });

        proteinView.getShowBondsMenu().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleShowBonds(oldValue, newValue);
            }
        });

        proteinView.getIncreaseAtomSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                handleChangeAtomSize(0.1);
            }
        });

        proteinView.getDecreaseAtomSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                handleChangeAtomSize(-0.1);
            }
        });

        proteinView.getIncreaseBondSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                handleChangeBondSize(0.1);
            }
        });

        proteinView.getDecreaseBondSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                handleChangeBondSize(-0.1);
            }
        });
    }

    private void setUpToolBar() {

        proteinView.getShowAtoms().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleShowAtoms(oldValue, newValue);
            }
        });

        proteinView.getShowBonds().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleShowBonds(oldValue, newValue);
            }
        });

        // TODO: should slider position change when size is influences from menu bar?
        proteinView.getAtomSizeSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                handleChangeAtomSize((Double) newValue - (Double) oldValue);
            }
        });

        proteinView.getBondSizeSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                handleChangeBondSize((Double) newValue - (Double) oldValue);
            }
        });

        proteinView.getChooseColoring().valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(AtomView.COLOR_BY_AMINO_ACID)) {
                    handleColorByAminoAcid();
                } else if (newValue.equals(AtomView.COLOR_BY_SECONDARY_STRUCTURE)) {
                    handleColorBySecondaryStructure();
                } else if (newValue.equals(AtomView.COLOR_BY_PROPERTIES)) {
                    // TODO
                }
            }
        });

    }

    private void handleShowBonds(Boolean oldValue, Boolean newValue) {
        proteinView.getBondSizeSlider().setDisable(!newValue);
        proteinView.getBondSizeMenu().setDisable(!newValue);
        if (newValue) {
            proteinView.getModelView().showBondViews();
        } else {
            proteinView.getModelView().hideBondViews();
        }
    }

    private void handleShowAtoms(Boolean oldValue, Boolean newValue) {
        proteinView.getAtomSizeSlider().setDisable(!newValue);
        proteinView.getAtomSizeMenu().setDisable(!newValue);
        if (newValue) {
            proteinView.getModelView().showAtomViews();
        } else {
            proteinView.getModelView().hideAtomViews();
        }
    }

    private void handleChangeAtomSize(Double factor) {
        proteinView.getModelView().changeAtomSize(factor);
    }

    private void handleChangeBondSize(Double factor) {
        proteinView.getModelView().changeBondSize(factor);
    }

    private void handleColorByAminoAcid() {
        proteinView.getColorByAminoAcid().setSelected(true);
        proteinView.getColorBySecondaryStructure().setSelected(false);
        proteinView.getColorByProperties().setSelected(false);
        proteinView.getModelView().changeAtomColor(AtomView.COLOR_BY_AMINO_ACID);
    }

    private void handleColorBySecondaryStructure() {
        proteinView.getColorBySecondaryStructure().setSelected(true);
        proteinView.getColorByAminoAcid().setSelected(false);
        proteinView.getColorByProperties().setSelected(false);
        proteinView.getModelView().changeAtomColor(AtomView.COLOR_BY_SECONDARY_STRUCTURE);
    }

}
