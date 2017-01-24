package protein3DViewer.presenter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import protein3DViewer.model.Protein;
import protein3DViewer.view.AtomView;
import protein3DViewer.view.ProteinView;
import protein3DViewer.view.SticksVisualization;

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

        proteinView.getMenuOpen().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDB files (*.pdb)", "*.pdb");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(proteinView.getModelView().getScene().getWindow());
//                Main.launch(null);
                // TODO load new file (open new window or clear protein?)
            }
        });

        proteinView.getMenuShowAtoms().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                proteinView.getAtomSizeSlider().setDisable(!newValue);
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleShowAtoms(newValue);
                }
            }
        });

        proteinView.getMenuIncreaseAtomSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleChangeAtomSize(0.1);
                }
            }
        });

        proteinView.getMenuDecreaseAtomSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleChangeAtomSize(-0.1);
                }
            }
        });

        proteinView.getMenuShowBonds().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleShowBonds(newValue);
                }
            }
        });

        proteinView.getMenuIncreaseBondSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleChangeBondSize(0.1);
                }
            }
        });

        proteinView.getMenuDecreaseBondSize().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleChangeBondSize(-0.1);
                }
            }
        });

        proteinView.getMenuVisualizeSticks().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                proteinView.getMenuShow().setDisable(!newValue);
                proteinView.getShowAtoms().setDisable(!newValue); // TODO join with next method
                proteinView.getAtomSizeSlider().setDisable(!newValue);
                proteinView.getShowBonds().setDisable(!newValue);
                proteinView.getBondSizeSlider().setDisable(!newValue);
            }
        });

        proteinView.getMenuColorBySingleColor().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(AtomView.COLOR_BY_SINGLE_COLOR);
                    handleColorBySingleColor();
                }
            }
        });

        proteinView.getMenuColorByAminoAcid().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(AtomView.COLOR_BY_AMINO_ACID);
                    handleColorByAminoAcid();
                }
            }
        });

        proteinView.getMenuColorBySecondaryStructure().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(AtomView.COLOR_BY_SECONDARY_STRUCTURE);
                    handleColorBySecondaryStructure();
                }
            }
        });

        proteinView.getMenuColorByProperties().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(AtomView.COLOR_BY_PROPERTIES);
                }
            }
        });

    }

    private void setUpToolBar() {

        proteinView.getShowAtoms().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                proteinView.getAtomSizeSlider().setDisable(!newValue);
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleShowAtoms(newValue);
                }
            }
        });

        // TODO: should slider position change when size is influences from menu bar?
        proteinView.getAtomSizeSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleChangeAtomSize((Double) newValue - (Double) oldValue);
                }
            }
        });

        proteinView.getShowBonds().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                proteinView.getBondSizeSlider().setDisable(!newValue);
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleShowBonds(newValue);
                }
            }
        });

        proteinView.getBondSizeSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (proteinView.getVisualizeSticks().isSelected()) {
                    handleChangeBondSize((Double) newValue - (Double) oldValue);
                }
            }
        });

        proteinView.getVisualizeSticks().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                proteinView.getShowAtoms().setDisable(!newValue);
                proteinView.getAtomSizeSlider().setDisable(!newValue);
                proteinView.getShowBonds().setDisable(!newValue);
                proteinView.getBondSizeSlider().setDisable(!newValue);
                if (newValue) {
                    proteinView.getModelView().addVisualization("Sticks");
                } else {
                    proteinView.getModelView().removeVisualization("Sticks");
                }
            }
        });

        proteinView.getVisualizeRibbon().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getModelView().addVisualization("Ribbon");
                } else {
                    proteinView.getModelView().removeVisualization("Ribbon");
                }
            }
        });

        proteinView.getChooseColoring().valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                proteinView.getMenuColorBySingleColor().setSelected(newValue.equals(AtomView.COLOR_BY_SINGLE_COLOR));
                proteinView.getMenuColorByAminoAcid().setSelected(newValue.equals(AtomView.COLOR_BY_AMINO_ACID));
                proteinView.getMenuColorBySecondaryStructure().setSelected(newValue.equals(AtomView.COLOR_BY_SECONDARY_STRUCTURE));
                proteinView.getMenuColorByProperties().setSelected(newValue.equals(AtomView.COLOR_BY_PROPERTIES));
                if (newValue.equals(AtomView.COLOR_BY_SINGLE_COLOR)) {
                    handleColorBySingleColor();
                }
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

    private void handleShowAtoms(Boolean newValue) {
        SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization("Sticks");
        if (newValue) {
            sticksVisualization.showAtomViews();
        } else {
            sticksVisualization.hideAtomViews();
        }
    }

    private void handleChangeAtomSize(Double factor) {
        SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization("Sticks");
        sticksVisualization.changeAtomSize(factor);
    }

    private void handleShowBonds(Boolean newValue) {
        SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization("Sticks");
        if (newValue) {
            sticksVisualization.showBondViews();
        } else {
            sticksVisualization.hideBondViews();
        }
    }

    private void handleChangeBondSize(Double factor) {
        SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization("Sticks");
        sticksVisualization.changeBondSize(factor);
    }

    private void handleColorBySingleColor() {
        if (proteinView.getVisualizeSticks().isSelected()) {  // TODO: right now only for sticks available
            SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization("Sticks");
            sticksVisualization.changeAtomColor(AtomView.COLOR_BY_SINGLE_COLOR);
        }
    }

    private void handleColorByAminoAcid() {
        if (proteinView.getVisualizeSticks().isSelected()) {  // TODO: right now only for sticks available
            SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization("Sticks");
            sticksVisualization.changeAtomColor(AtomView.COLOR_BY_AMINO_ACID);
        }
    }

    private void handleColorBySecondaryStructure() {
        if (proteinView.getVisualizeSticks().isSelected()) {  // TODO: right now only for sticks available
            SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization("Sticks");
            sticksVisualization.changeAtomColor(AtomView.COLOR_BY_SECONDARY_STRUCTURE);
        }
    }

}
