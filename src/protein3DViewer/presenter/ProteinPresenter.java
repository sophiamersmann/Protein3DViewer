package protein3DViewer.presenter;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Point3D;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import protein3DViewer.*;
import protein3DViewer.model.Atom;
import protein3DViewer.model.Helix;
import protein3DViewer.model.Protein;
import protein3DViewer.view.*;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.atomView.CarbonView;
import protein3DViewer.view.bondView.Line;
import protein3DViewer.view.modelVisualization.AtomLabel;
import protein3DViewer.view.modelVisualization.SticksVisualization;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Optional;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ProteinPresenter {

    private ProteinView proteinView;
    private Protein protein;
    private String blastFirstAlignment;

    private ModelPresenter modelPresenter;
    private SequencePresenter sequencePresenter;

    public ProteinPresenter(ProteinView proteinView, Protein protein) {
        this.proteinView = proteinView;
        this.protein = protein;
        this.modelPresenter = proteinView.getModelPresenter();
        this.sequencePresenter = proteinView.getSequencePresenter();
        setUpMenuBar();
        setUpToolBar();
        initSelectedItemsListener();
    }

    public void reset() { // TODO implement
        proteinView.getAtomSizeSlider().setValue(0);
        proteinView.getBondSizeSlider().setValue(0);
        proteinView.getVisualizeSticks().setSelected(true);
        proteinView.getVisualizeRibbon().setSelected(false);
        proteinView.getVisualizeCartoon().setSelected(false);
        proteinView.reset(protein);
        modelPresenter = proteinView.getModelPresenter();
        sequencePresenter = proteinView.getSequencePresenter();
        initSelectedItemsListener();
        // TODO blast first alignment and other reset
    }


    private void setUpMenuBar() {

        proteinView.getMenuOpen().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDB files (*.pdb)", "*.pdb");
                fileChooser.getExtensionFilters().add(extFilter);
                File pdbFile = fileChooser.showOpenDialog(proteinView.getModelView().getScene().getWindow());

                ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), proteinView.getModelView());
                scaleTransition.setToX(0);
                scaleTransition.setToY(0);
                scaleTransition.setToZ(0);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), proteinView.getModelView());
                fadeTransition.setToValue(0);

                ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
                parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        protein = new Protein();
                        PDBParser parser = new PDBParser(pdbFile);
                        Director director = new Director(parser, protein);
                        protein.getModel().createBonds();
                        reset();
                    }
                });

                parallelTransition.play();

            }
        });

//        proteinView.getMenuIncreaseAtomSize().setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                event.consume();
//                if (proteinView.getVisualizeSticks().isSelected()) {
//                    handleChangeAtomSize(0.1);
//                }
//            }
//        });
//
//        proteinView.getMenuDecreaseAtomSize().setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                event.consume();
//                if (proteinView.getVisualizeSticks().isSelected()) {
//                    handleChangeAtomSize(-0.1);
//                }
//            }
//        });
//
//        proteinView.getMenuIncreaseBondSize().setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                event.consume();
//                if (proteinView.getVisualizeSticks().isSelected()) {
//                    handleChangeBondSize(0.1);
//                }
//            }
//        });
//
//        proteinView.getMenuDecreaseBondSize().setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                event.consume();
//                if (proteinView.getVisualizeSticks().isSelected()) {
//                    handleChangeBondSize(-0.1);
//                }
//            }
//        });

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

        proteinView.getMenuColorSelectedAtoms().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                ColorPicker colorPicker = new ColorPicker();
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Choose Color of Selected Atoms");
                ButtonType buttonApply = new ButtonType("Apply", ButtonBar.ButtonData.APPLY);
                ButtonType buttonClose = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonApply, buttonClose);
                BorderPane pane = new BorderPane();
                pane.setCenter(colorPicker);
                alert.getDialogPane().setContent(pane);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonApply) {
                    Color pickedColor = colorPicker.getValue();
                    SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
                    for (AbstractAtomView atomView: sticksVisualization.getSelectionModel().getSelectedItems()) {
                        atomView.setColor(pickedColor);
                    }
                    for (AbstractAtomView atomView: sticksVisualization.getDistanceSelectionModel().getSelectedItems()) {
                        atomView.setColor(pickedColor);
                    }
                }
            }
        });

        proteinView.getMenuChangeDefaultColors().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Change Default Colors");
                ButtonType buttonApply = new ButtonType("Apply");
                ButtonType buttonClose = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonApply, buttonClose);

                BorderPane pane = new BorderPane();
                VBox box = new VBox(10);
                ChoiceBox<ColorValue> chooseColoring = new ChoiceBox<>(FXCollections.observableArrayList(ColorValue.values()));
                chooseColoring.setValue(ColorValue.DEFAULT);
                chooseColoring.setMaxWidth(Double.MAX_VALUE);

                ColorPicker colorPicker = new ColorPicker(ColorValue.DEFAULT.getColor());
                colorPicker.setMaxWidth(Double.MAX_VALUE);

                chooseColoring.valueProperty().addListener(new ChangeListener<ColorValue>() {
                    @Override
                    public void changed(ObservableValue<? extends ColorValue> observable, ColorValue oldValue, ColorValue newValue) {
                        colorPicker.setValue(newValue.getColor());
                    }
                });

                box.getChildren().addAll(chooseColoring, colorPicker);
                pane.setCenter(box);

                alert.getDialogPane().setContent(pane);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonApply) {
                    Color pickedColor = colorPicker.getValue();
                    chooseColoring.getValue().setColor(pickedColor);
                    changeColorOfCurrentView(chooseColoring.getValue());
                }

            }
        });

        proteinView.getMenuResetToDefaultColors().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ColorValue.reset();
                for (ColorValue colorValue: ColorValue.values()) {
                    changeColorOfCurrentView(colorValue);
                }
                if (proteinView.getVisualizeSticks().isSelected() && proteinView.getChooseColoring().getValue() == ColorMode.BY_ATOM_TYPE) {
                    handleColorByAminoAcid();
                }
            }
        });

        proteinView.getMenuRunBlast().setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Separator separator = new Separator();
                Label blastLabel = new Label("BLAST running...");
                ProgressIndicator progressIndicator = new ProgressIndicator();
                proteinView.getToolBar().getItems().addAll(separator, blastLabel, progressIndicator);
                BlastService blastService = new BlastService();
                blastService.setSequence(protein.getSeqResRecord().generateAminoAcidSequence());
                blastService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        proteinView.getToolBar().getItems().removeAll(separator, blastLabel, progressIndicator);
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
                event.consume();
                final Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("BLAST Results");
                ButtonType buttonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().add(buttonType);
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

        proteinView.getMenuInformationResidueTypes().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                final Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Information");
                ButtonType buttonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().add(buttonType);
//                PieChart.Data maxData = proteinView.getPieChartResidueTypes().getData().get(0);
//                for (PieChart.Data pieChartData: proteinView.getPieChartResidueTypes().getData()) {
//                    if (pieChartData.getPieValue() > maxData.getPieValue()) {
//                        maxData = pieChartData;
//                    }
//                }
                DecimalFormat df = new DecimalFormat("#.##");
//                alert.setHeaderText("Most occurring amino acid: " + maxData.getName() + " (" + df.format(maxData.getPieValue()) + "%)");
                GridPane content = new GridPane();


                final Label caption = new Label();
                caption.setTextFill(Color.BLACK);
                caption.setStyle("-fx-font: 24 arial;");

                content.add(proteinView.getPieChartResidueTypes(), 0, 0);
                content.add(caption, 0, 1);
                GridPane.setHalignment(caption, HPos.CENTER);
                for (PieChart.Data data : proteinView.getPieChartResidueTypes().getData()) {
                    data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            caption.setText(data.getName() + ": " + df.format(data.getPieValue() * 100) + "%");
                        }
                    });
                    data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            caption.setText("");
                        }
                    });
                }

//                alert.getDialogPane().setExpandableContent(content);
                alert.getDialogPane().setContent(content);
                alert.showAndWait();
            }
        });

        proteinView.getMenuInformationSecondaryStructure().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                final Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Information");
                ButtonType buttonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().add(buttonType);

                String content = "Number of Helices: " + protein.getSecondaryStructure().getHelices().size() + "\n";
                content += "Helix types: ";
                for (Helix helix: protein.getSecondaryStructure().getHelices().values()) {
                    content += helix.getType() + ", ";
                }
                content += "\n\nNumber of Sheets: " + protein.getSecondaryStructure().getSheets().size();

//                alert.setContentText(content);

                GridPane pane = new GridPane();
                final Label caption = new Label();
                caption.setTextFill(Color.BLACK);
                caption.setStyle("-fx-font: 24 arial;");
                DecimalFormat df = new DecimalFormat("#.##");
//                pane.add(new TextField(content), 0, 0);
                pane.add(proteinView.getPieChartSecondaryStructure(), 0, 0);
                pane.add(caption, 0, 1);
                GridPane.setHalignment(caption, HPos.CENTER);
                for (PieChart.Data data : proteinView.getPieChartSecondaryStructure().getData()) {
                    data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            caption.setText(data.getName() + ": " + df.format(data.getPieValue() * 100) + "%");
                        }
                    });
                    data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            caption.setText("");
                        }
                    });
                }
                alert.getDialogPane().setContent(pane);
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
                    proteinView.getChooseColoring().setDisable(false);
                    proteinView.getMenuColorBy().setDisable(false);
                    proteinView.getMenuColorSelectedAtoms().setDisable(true);
                    modelPresenter.setUpAtomViews();
                    proteinView.initCrossLinking();
                    proteinView.getCalculateDistanceButton().setDisable(false);
                    initSelectedItemsListener();

                } else {
                    proteinView.getAtomSizeSlider().setValue(0);
                    proteinView.getBondSizeSlider().setValue(0);
                    proteinView.getShowAtoms().setSelected(true);
                    proteinView.getShowBonds().setSelected(true);
                    proteinView.getChooseColoring().setValue(ColorMode.BY_ATOM_TYPE);
                    proteinView.getMenuColorByAminoAcid().setSelected(true);
                    proteinView.getMenuColorBySingleColor().setSelected(false);
                    proteinView.getMenuColorBySecondaryStructure().setSelected(false);
                    proteinView.getMenuColorByProperties().setSelected(false);
                    proteinView.getChooseColoring().setDisable(true);
                    proteinView.getMenuColorBy().setDisable(true);
                    proteinView.getCalculateDistanceButton().setDisable(true);
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

        proteinView.getCalculateDistanceButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
                    if (sticksVisualization.getDistanceSelectionModel().getSelectedItems().size() == 2) {
                        AbstractAtomView atomView1 = sticksVisualization.getDistanceSelectionModel().getSelectedItems().get(0);
                        AbstractAtomView atomView2 = sticksVisualization.getDistanceSelectionModel().getSelectedItems().get(1);

                        Line connection = new Line(atomView1.getX(), atomView1.getY(), atomView1.getZ(),
                                atomView2.getX(), atomView2.getY(), atomView2.getZ()
                        );
                        connection.setMaterial(new PhongMaterial(Color.BLACK));
                        sticksVisualization.getConnectionGroup().getChildren().add(connection);

                        Point3D start = new Point3D(connection.getStartX(), connection.getStartY(), connection.getStartZ());
                        Point3D end = new Point3D(connection.getEndX(), connection.getEndY(), connection.getEndZ());
                        Point3D midPoint = end.midpoint(start);
                        Atom pseudoAtom = new Atom(-1, "", "");
                        pseudoAtom.setX(midPoint.getX());
                        pseudoAtom.setY(midPoint.getY());
                        pseudoAtom.setZ(midPoint.getZ());
                        AbstractAtomView pseudo = new CarbonView(pseudoAtom);
                        pseudo.setVisible(false);
                        sticksVisualization.getPseudoGroup().getChildren().add(pseudo);

                        DecimalFormat df = new DecimalFormat("#.##");
                        AtomLabel distanceLabel = new AtomLabel(pseudo, proteinView.getModelView().getBottomPane(), proteinView.getModelView().getBottomGroup().worldTransformProperty(),
                                df.format(Math.abs(end.subtract(start).magnitude())) + "A");
                        proteinView.getModelView().getTopPane().getChildren().add(distanceLabel);
                    } else {
                        final Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Distance can't be calculated.");
                        alert.setContentText("Please select exactly two atoms.");
                        alert.showAndWait();
                    }
                }

            });


    }

    private void initSelectedItemsListener() {
        SticksVisualization sticksVisualization = (SticksVisualization) proteinView.getModelView().getModelVisualization(VisualizationMode.STICKS);
        sticksVisualization.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
            @Override
            public void onChanged(Change<? extends AbstractAtomView> c) {
                while (c.next()) {
                    if (sticksVisualization.getSelectionModel().getSelectedItems().size() > 0 || sticksVisualization.getDistanceSelectionModel().getSelectedItems().size() > 0) {
                        proteinView.getMenuColorSelectedAtoms().setDisable(false);
                    } else {
                        proteinView.getMenuColorSelectedAtoms().setDisable(true);
                    }
                }
            }
        });
        sticksVisualization.getDistanceSelectionModel().getSelectedItems().addListener(new ListChangeListener<AbstractAtomView>() {
            @Override
            public void onChanged(Change<? extends AbstractAtomView> c) {
                while (c.next()) {
                    if (sticksVisualization.getSelectionModel().getSelectedItems().size() > 0 || sticksVisualization.getDistanceSelectionModel().getSelectedItems().size() > 0) {
                        proteinView.getMenuColorSelectedAtoms().setDisable(false);
                    } else {
                        proteinView.getMenuColorSelectedAtoms().setDisable(true);
                    }
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

    private void changeColorOfCurrentView(ColorValue changedColorValue) {
        switch (changedColorValue) {  //TODO
            case DEFAULT:
                if (proteinView.getVisualizeSticks().isSelected() && proteinView.getChooseColoring().getValue() == ColorMode.UNICOLOR) {
                    handleColorBySingleColor();
                }
                break;
            case RIBBON:
                if (proteinView.getVisualizeRibbon().isSelected()) {
                    proteinView.getModelView().removeVisualization(VisualizationMode.RIBBON);
                    proteinView.getModelView().addVisualization(VisualizationMode.RIBBON);
                }
                break;
            case HELIX:
            case SHEET:
            case LOOPS:
                proteinView.getSequenceView().changeColor();
                if (proteinView.getVisualizeCartoon().isSelected()) {
                    proteinView.getModelView().removeVisualization(VisualizationMode.CARTOON);
                    proteinView.getModelView().addVisualization(VisualizationMode.CARTOON);
                }
                if (proteinView.getVisualizeSticks().isSelected() && proteinView.getChooseColoring().getValue() == ColorMode.BY_SECONDARY_STRUCTURE) {
                    handleColorBySecondaryStructure();
                }
                break;
            case LARGE_HYDROPHOBIC:
            case SMALL_HYDROPHOBIC:
            case POLAR:
            case POSITIVE_CHARGED:
            case NEGATIVE_CHARGED:
                if (proteinView.getVisualizeSticks().isSelected() && proteinView.getChooseColoring().getValue() == ColorMode.BY_PROPERTIES) {
                    handleColorByProperties();
                }
                break;
        }
    }


}
