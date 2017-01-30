package protein3DViewer.presenter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import protein3DViewer.BlastSearchResultParser;
import protein3DViewer.BlastService;
import protein3DViewer.model.Protein;
import protein3DViewer.view.ColorMode;
import protein3DViewer.view.VisualizationMode;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.ProteinView;
import protein3DViewer.view.modelVisualization.SticksVisualization;

import java.io.File;
import java.util.Optional;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ProteinPresenter {

    private ProteinView proteinView;
    private Protein protein;
    private String blastFirstAlignment;

    public ProteinPresenter(ProteinView proteinView, Protein protein) {
        this.proteinView = proteinView;
        this.protein = protein;
        setUpMenuBar();
        setUpToolBar();
        setUpPieChart();
    }

    private void setUpPieChart() {  // TODO not working
        final Label caption = new Label();
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
//        Group root = (Group) proteinView.getBorderPane().getScene().getRoot();
//        root.getChildren().add(caption);

        for (PieChart.Data data : proteinView.getPieChart().getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());
                    caption.setText(String.valueOf(data.getPieValue()) + "%");
                }
            });
        }

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

        proteinView.getMenuColorBySingleColor().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(ColorMode.UNICOLOR);
                    handleColorBySingleColor();
                }
            }
        });

        proteinView.getMenuColorByAminoAcid().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(ColorMode.BY_ATOM_TYPE);
                    handleColorByAminoAcid();
                }
            }
        });

        proteinView.getMenuColorBySecondaryStructure().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(ColorMode.BY_SECONDARY_STRUCTURE);
                    handleColorBySecondaryStructure();
                }
            }
        });

        proteinView.getMenuColorByProperties().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getChooseColoring().setValue(ColorMode.BY_PROPERTIES);
                    handleColorByProperties();
                }
            }
        });

        proteinView.getMenuRunBlast().setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ProgressIndicator progressIndicator = new ProgressIndicator();
                proteinView.getToolBar().getItems().add(progressIndicator);  // TODO: not to toolbar
                BlastService blastService = new BlastService();
                blastService.setSequence(protein.getSeqResRecord().generateAminoAcidSequence());
                blastService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        proteinView.getToolBar().getItems().remove(progressIndicator);
                        proteinView.getMenuShowBlastResults().setDisable(false);

                        String[] blastResult = (String[]) t.getSource().getValue();
                        BlastSearchResultParser blastSearchResultParser = new BlastSearchResultParser(blastResult);
                        blastFirstAlignment = blastSearchResultParser.getFirstAlignment();

                        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("BLAST Results");
                        alert.setHeaderText("BLAST results are ready");
                        alert.setContentText("Do you want to view them now?");
                        ButtonType buttonYes = new ButtonType("Yes");
                        ButtonType buttonLater = new ButtonType("Later", ButtonBar.ButtonData.CANCEL_CLOSE);
                        alert.getButtonTypes().setAll(buttonYes, buttonLater);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonYes) {
                            proteinView.getMenuShowBlastResults().fire();
                        }
                    }
                });
                blastService.start();
            }
        });

        proteinView.getMenuShowBlastResults().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("BLAST Results");
                String firstAlignmentId = blastFirstAlignment.split(" ")[0].substring(1);
                alert.setHeaderText("Highest scoring result: " + firstAlignmentId);
                TextArea textArea = new TextArea(blastFirstAlignment);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);
                GridPane content = new GridPane();
                content.setMaxWidth(Double.MAX_VALUE);
                content.add(textArea, 0, 1);
                alert.getDialogPane().setExpandableContent(content);
                alert.showAndWait();
            }
        });

    }

    private void setUpToolBar() {

        proteinView.getShowAtoms().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                proteinView.getAtomSizeSlider().setDisable(!newValue);
                if (proteinView.getVisualizeSticks().isSelected()) {
                    SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
                    if (newValue) {
                        sticksVisualization.showAtomViews();
                    } else {
                        sticksVisualization.hideAtomViews();
                    }
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
                    SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
                    if (newValue) {
                        sticksVisualization.showBondViews();
                    } else {
                        sticksVisualization.hideBondViews();
                    }
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
                proteinView.getMenuShow().setDisable(!newValue);
                proteinView.getShowAtoms().setDisable(!newValue);
                proteinView.getAtomSizeSlider().setDisable(!newValue);
                proteinView.getShowBonds().setDisable(!newValue);
                proteinView.getBondSizeSlider().setDisable(!newValue);
                if (newValue) {
                    proteinView.getModelView().addVisualization(VisualizationMode.STICKS);
                } else {
                    proteinView.getModelView().removeVisualization(VisualizationMode.STICKS);
                }
            }
        });

        proteinView.getVisualizeRibbon().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getModelView().addVisualization(VisualizationMode.RIBBON);
                } else {
                    proteinView.getModelView().removeVisualization(VisualizationMode.RIBBON);
                }
            }
        });

        proteinView.getVisualizeCartoon().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getModelView().addVisualization(VisualizationMode.CARTOON);
                } else {
                    proteinView.getModelView().removeVisualization(VisualizationMode.CARTOON);
                }
            }
        });

        proteinView.getChooseColoring().valueProperty().addListener(new ChangeListener<ColorMode>() {
            @Override
            public void changed(ObservableValue<? extends ColorMode> observable, ColorMode oldValue, ColorMode newValue) {
                proteinView.getMenuColorBySingleColor().setSelected(newValue == ColorMode.UNICOLOR);
                proteinView.getMenuColorByAminoAcid().setSelected(newValue == ColorMode.BY_ATOM_TYPE);
                proteinView.getMenuColorBySecondaryStructure().setSelected(newValue == ColorMode.BY_SECONDARY_STRUCTURE);
                proteinView.getMenuColorByProperties().setSelected(newValue == ColorMode.BY_PROPERTIES);
                if (newValue == ColorMode.UNICOLOR) {
                    handleColorBySingleColor();
                } else if (newValue == ColorMode.BY_ATOM_TYPE) {
                    handleColorByAminoAcid();
                } else if (newValue == ColorMode.BY_SECONDARY_STRUCTURE) {
                    handleColorBySecondaryStructure();
                } else if (newValue == ColorMode.BY_PROPERTIES) {
                    handleColorByProperties();
                }
            }
        });

    }

    private void handleChangeAtomSize(Double factor) {
        SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
        sticksVisualization.changeAtomSize(factor);
    }


    private void handleChangeBondSize(Double factor) {
        SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
        sticksVisualization.changeBondSize(factor);
    }

    private void handleColorBySingleColor() {
        if (proteinView.getVisualizeSticks().isSelected()) {  // TODO: right now only for sticks available
            SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
            sticksVisualization.changeAtomColor(ColorMode.UNICOLOR);
        }
    }

    private void handleColorByAminoAcid() {
        if (proteinView.getVisualizeSticks().isSelected()) {  // TODO: right now only for sticks available
            SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
            sticksVisualization.changeAtomColor(ColorMode.BY_ATOM_TYPE);
        }
    }

    private void handleColorBySecondaryStructure() {
        if (proteinView.getVisualizeSticks().isSelected()) {  // TODO: right now only for sticks available
            SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
            sticksVisualization.changeAtomColor(ColorMode.BY_SECONDARY_STRUCTURE);
        }
    }

    private void handleColorByProperties() {
        if (proteinView.getVisualizeSticks().isSelected()) {   // TODO: right now only for sticks available
            SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
            sticksVisualization.changeAtomColor(ColorMode.BY_PROPERTIES);
        }
    }

}
