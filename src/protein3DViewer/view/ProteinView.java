package protein3DViewer.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import protein3DViewer.model.Chain;
import protein3DViewer.model.Protein;
import protein3DViewer.model.Residue;
import protein3DViewer.presenter.ModelPresenter;
import protein3DViewer.presenter.SequencePresenter;
import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.modelVisualization.SticksVisualization;

import java.util.*;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ProteinView {

    private Protein protein;

    private BorderPane borderPane;

    private SequenceView sequenceView;
    private ModelView modelView;

    private ModelPresenter modelPresenter;
    private SequencePresenter sequencePresenter;

    private MenuBar menuBar = new MenuBar();

    // menu open items
    private Menu menuFile = new Menu("File");
    private MenuItem menuOpen = new MenuItem("Open...");

    // menu edit items
    private Menu menuEdit = new Menu("Edit");
    private Menu menuColorBy = new Menu("Color by...");
    private CheckMenuItem menuColorBySingleColor = new CheckMenuItem(ColorMode.UNICOLOR.getDescription());
    private CheckMenuItem menuColorByAminoAcid = new CheckMenuItem(ColorMode.BY_ATOM_TYPE.getDescription());
    private CheckMenuItem menuColorBySecondaryStructure = new CheckMenuItem(ColorMode.BY_SECONDARY_STRUCTURE.getDescription());
    private CheckMenuItem menuColorByProperties = new CheckMenuItem(ColorMode.BY_PROPERTIES.getDescription());
    private MenuItem menuChangeDefaultColors = new MenuItem("Change Default Colors");
    private MenuItem menuResetToDefaultColors = new MenuItem("Reset to Default Colors");
    private MenuItem menuColorSelectedAtoms = new MenuItem("Change Color of Selected Atoms");

    // menu view items
    private Menu menuView = new Menu("View");
    private Menu menuShow = new Menu("Show");
    private CheckMenuItem menuShowAtoms = new CheckMenuItem("Atoms");
    private CheckMenuItem menuShowBonds = new CheckMenuItem("Bonds");
    private Menu menuVisualization = new Menu("Visualization");
    private CheckMenuItem menuVisualizeSticks = new CheckMenuItem(VisualizationMode.STICKS.getDescription());
    private CheckMenuItem menuVisualizeRibbon = new CheckMenuItem(VisualizationMode.RIBBON.getDescription());
    private CheckMenuItem menuVisualizeCartoon = new CheckMenuItem(VisualizationMode.CARTOON.getDescription());
    private Menu menuInformation = new Menu("Information about...");
    private MenuItem menuInformationResidueTypes = new MenuItem("Residue Types");
    private MenuItem menuInformationSecondaryStructure = new MenuItem("Secondary Structure");

    // menu service items
    private Menu menuServices = new Menu("Services");
    private MenuItem menuRunBlast = new MenuItem("Run BLAST...");
    private MenuItem menuShowBlastResults = new MenuItem("Show BLAST Results");

    // tool bar items
    private ToolBar toolBar = new ToolBar();
    private CheckBox showAtoms = new CheckBox("Show Atoms");
    private CheckBox showBonds = new CheckBox("Show Bonds");
    private CheckBox visualizeSticks = new CheckBox(VisualizationMode.STICKS.getDescription());
    private CheckBox visualizeRibbon = new CheckBox(VisualizationMode.RIBBON.getDescription());
    private CheckBox visualizeCartoon = new CheckBox(VisualizationMode.CARTOON.getDescription());
    private Slider atomSizeSlider = new Slider(-0.5, 20, 0);
    private Slider bondSizeSlider = new Slider(-0.5, 1, 0);
    private ChoiceBox<ColorMode> chooseColoring = new ChoiceBox<>(FXCollections.observableArrayList(
            ColorMode.UNICOLOR, ColorMode.BY_ATOM_TYPE, ColorMode.BY_SECONDARY_STRUCTURE, ColorMode.BY_PROPERTIES
    ));
    private Button calculateDistanceButton = new Button("Calculate Distance");

    private PieChart pieChartResidueTypes = new PieChart();
    private PieChart pieChartSecondaryStructure = new PieChart();

    public ProteinView(BorderPane borderPane, Protein protein) {
        this.protein = protein;
        this.borderPane = borderPane;
        initPieCharts();
        initViews();
        initMenuBar();
        initToolBar();
        initBindingsBetweenToolBarAndMenu();
        initCrossLinking();
        setDefaults();
    }

    /**
     * reset GUI
     *
     * @param protein protein to be displayed
     */
    public void reset(Protein protein) {
        modelView.clear();
        sequenceView.clear();
        this.protein = protein;
        initPieCharts();
        initViews();
        initCrossLinking();
        setDefaults();
    }

    private void initPieCharts() {
        int totalNumberOfResidues = 0;
        Map<String, Integer> numberOfEachResidueType = new HashMap<>();
        Map<String, Integer> numberOfEachSecondaryStructure = new HashMap<>();
        numberOfEachSecondaryStructure.put("Helix", 0);
        numberOfEachSecondaryStructure.put("Sheet", 0);
        numberOfEachSecondaryStructure.put("Loop", 0);

        for (Chain chain: protein.getModel().getChains().values()) {
            for (Residue residue: chain.getResidues().values()) {
                if (!numberOfEachResidueType.containsKey(residue.getName3())) {
                    numberOfEachResidueType.put(residue.getName3(), 0);
                }
                numberOfEachResidueType.put(residue.getName3(), numberOfEachResidueType.get(residue.getName3()) + 1);
                if (residue.isInHelix()) {
                    numberOfEachSecondaryStructure.put("Helix", numberOfEachSecondaryStructure.get("Helix") + 1);
                } else if (residue.isInSheet()) {
                    numberOfEachSecondaryStructure.put("Sheet", numberOfEachSecondaryStructure.get("Sheet") + 1);
                } else {
                    numberOfEachSecondaryStructure.put("Loop", numberOfEachSecondaryStructure.get("Loop") + 1);
                }
            }
            totalNumberOfResidues += chain.getResidues().size();
        }

        createPieChart(pieChartResidueTypes, numberOfEachResidueType, totalNumberOfResidues, "Residue Types");
        createPieChart(pieChartSecondaryStructure, numberOfEachSecondaryStructure, totalNumberOfResidues, "Secondary Structure");
    }

    /**
     * create pie chart
     *
     * @param pieChart pie chart
     * @param typeCount number of items of a certain type
     * @param totalNumberOfResidues total number of residues of the protein
     * @param title title of pie chart
     */
    private static void createPieChart(PieChart pieChart, Map<String, Integer> typeCount, int totalNumberOfResidues, String title) {
        List<PieChart.Data> pieChartDataArray = new ArrayList<>();
        Iterator it = typeCount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Double percentage = (Integer) pair.getValue() / (double) totalNumberOfResidues;
            pieChartDataArray.add(new PieChart.Data((String) pair.getKey(), percentage));
            it.remove();
        }
        pieChart.setData(FXCollections.observableArrayList(pieChartDataArray));
        pieChart.setTitle(title);
        pieChart.setMaxHeight(400);
        pieChart.setMaxWidth(400);
    }

    private void initMenuBar() {
        menuFile.getItems().add(menuOpen);

        menuShow.getItems().addAll(menuShowAtoms, menuShowBonds);
        menuVisualization.getItems().addAll(menuVisualizeSticks, menuVisualizeRibbon, menuVisualizeCartoon);
        menuColorBy.getItems().addAll(menuColorBySingleColor, menuColorByAminoAcid, menuColorBySecondaryStructure, menuColorByProperties);
        menuInformation.getItems().addAll(menuInformationResidueTypes, menuInformationSecondaryStructure);
        menuView.getItems().addAll(menuShow, menuVisualization, menuInformation);

        menuEdit.getItems().addAll(menuColorBy,
                new SeparatorMenuItem(),
                menuChangeDefaultColors,
                menuResetToDefaultColors,
                new SeparatorMenuItem(),
                menuColorSelectedAtoms
        );

        menuServices.getItems().addAll(menuRunBlast, menuShowBlastResults);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuServices);
        menuBar.useSystemMenuBarProperty().set(true);
        borderPane.getChildren().add(menuBar);
    }

    private void setDefaults() {
        menuShowAtoms.setSelected(true);
        menuShowBonds.setSelected(true);
        menuVisualizeSticks.setSelected(true);
        menuColorByAminoAcid.setSelected(true);
        menuShowBlastResults.setDisable(true);

        showAtoms.setSelected(true);
        showBonds.setSelected(true);
        visualizeSticks.setSelected(true);
        chooseColoring.setValue(ColorMode.BY_ATOM_TYPE);
        calculateDistanceButton.setMaxWidth(Double.MAX_VALUE);
    }

    private void initToolBar() {
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.getItems().addAll(
                showAtoms,
                atomSizeSlider,
                new Separator(),
                showBonds,
                bondSizeSlider,
                new Separator(),
                new Label("Visualization"),
                visualizeSticks,
                visualizeRibbon,
                visualizeCartoon,
                new Separator(),
                new Label("Color by..."),
                chooseColoring,
                new Separator(),
                calculateDistanceButton
        );
        borderPane.setLeft(toolBar);
        BorderPane.setAlignment(borderPane.getLeft(), Pos.TOP_CENTER);
    }

    private void initViews() {
        // init 3D model view and presenter
        modelView = new ModelView(protein.getModel());
        modelPresenter = new ModelPresenter(modelView, protein.getModel());
        borderPane.setCenter(modelView);
        BorderPane.setAlignment(borderPane.getCenter(), Pos.CENTER);

        // init sequence view and presenter
        sequenceView = new SequenceView(protein.getSeqResRecord(), protein.getSecondaryStructure());
        sequencePresenter = new SequencePresenter(modelPresenter, sequenceView, protein.getSeqResRecord());
        borderPane.setTop(sequenceView);
        BorderPane.setAlignment(borderPane.getTop(), Pos.CENTER_LEFT);

        createBindingsForResizing();

    }

    private void createBindingsForResizing() {
        modelView.getStackPane().setPrefWidth(borderPane.getWidth());
        modelView.getStackPane().setMaxWidth(borderPane.getWidth());
        modelView.getStackPane().setMinWidth(borderPane.getWidth());
        sequenceView.getScrollPane().setPrefWidth(borderPane.getWidth());
        sequenceView.getScrollPane().setMaxWidth(borderPane.getWidth());
        sequenceView.getScrollPane().setMinWidth(borderPane.getWidth());
        borderPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                modelView.getStackPane().setPrefWidth((double) newValue);
                modelView.getStackPane().setMaxWidth((double) newValue);
                modelView.getStackPane().setMinWidth((double) newValue);
                sequenceView.getScrollPane().setPrefWidth((double) newValue);
                sequenceView.getScrollPane().setMaxWidth((double) newValue);
                sequenceView.getScrollPane().setMinWidth((double) newValue);
            }
        });
        modelView.getStackPane().setPrefHeight(borderPane.getHeight());
        modelView.getStackPane().setMinHeight(borderPane.getHeight());
        modelView.getStackPane().setMaxHeight(borderPane.getHeight());
        borderPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                modelView.getStackPane().setPrefHeight((double) newValue);
                modelView.getStackPane().setMinHeight((double) newValue);
                modelView.getStackPane().setMaxHeight((double) newValue);
            }
        });
    }

    private void initBindingsBetweenToolBarAndMenu() {
        showAtoms.selectedProperty().bindBidirectional(menuShowAtoms.selectedProperty());
        showBonds.selectedProperty().bindBidirectional(menuShowBonds.selectedProperty());
        visualizeSticks.selectedProperty().bindBidirectional(menuVisualizeSticks.selectedProperty());
        visualizeRibbon.selectedProperty().bindBidirectional(menuVisualizeRibbon.selectedProperty());
        visualizeCartoon.selectedProperty().bindBidirectional(menuVisualizeCartoon.selectedProperty());
    }

    /**
     * links selected properties between sequence and 3D model
     */
    public void initCrossLinking() {
        if (modelView.getModelVisualizations().containsKey(VisualizationMode.STICKS)) {
            SticksVisualization sticksVisualization = (SticksVisualization) modelView.getModelVisualization(VisualizationMode.STICKS);
            for (AbstractAtomView atomView: sticksVisualization.getAtomViews().values()) {
                Integer residueId = atomView.getAtom().getResidue().getId();
                SelectableLabel associatedResidueView = sequenceView.getResidueViews().get(residueId);
                atomView.selectedProperty().bindBidirectional(associatedResidueView.selectedProperty());
                atomView.shiftSelectedProperty().bindBidirectional(associatedResidueView.shiftSelectedProperty());
            }
        }
    }

    public ModelPresenter getModelPresenter() {
        return modelPresenter;
    }

    public SequencePresenter getSequencePresenter() {
        return sequencePresenter;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public SequenceView getSequenceView() {
        return sequenceView;
    }

    public ModelView getModelView() {
        return modelView;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Menu getMenuFile() {
        return menuFile;
    }

    public MenuItem getMenuOpen() {
        return menuOpen;
    }

    public Menu getMenuEdit() {
        return menuEdit;
    }

    public Menu getMenuView() {
        return menuView;
    }

    public Menu getMenuShow() {
        return menuShow;
    }

    public CheckMenuItem getMenuShowAtoms() {
        return menuShowAtoms;
    }

    public CheckMenuItem getMenuShowBonds() {
        return menuShowBonds;
    }

    public Menu getMenuVisualization() {
        return menuVisualization;
    }

    public CheckMenuItem getMenuVisualizeSticks() {
        return menuVisualizeSticks;
    }

    public CheckMenuItem getMenuVisualizeRibbon() {
        return menuVisualizeRibbon;
    }

    public CheckMenuItem getMenuVisualizeCartoon() {
        return menuVisualizeCartoon;
    }

    public Menu getMenuColorBy() {
        return menuColorBy;
    }

    public CheckMenuItem getMenuColorBySingleColor() {
        return menuColorBySingleColor;
    }

    public CheckMenuItem getMenuColorByAminoAcid() {
        return menuColorByAminoAcid;
    }

    public CheckMenuItem getMenuColorBySecondaryStructure() {
        return menuColorBySecondaryStructure;
    }

    public CheckMenuItem getMenuColorByProperties() {
        return menuColorByProperties;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public CheckBox getShowAtoms() {
        return showAtoms;
    }

    public CheckBox getShowBonds() {
        return showBonds;
    }

    public CheckBox getVisualizeSticks() {
        return visualizeSticks;
    }

    public CheckBox getVisualizeRibbon() {
        return visualizeRibbon;
    }

    public CheckBox getVisualizeCartoon() {
        return visualizeCartoon;
    }

    public Slider getAtomSizeSlider() {
        return atomSizeSlider;
    }

    public Slider getBondSizeSlider() {
        return bondSizeSlider;
    }

    public ChoiceBox<ColorMode> getChooseColoring() {
        return chooseColoring;
    }

    public MenuItem getMenuRunBlast() {
        return menuRunBlast;
    }

    public MenuItem getMenuShowBlastResults() {
        return menuShowBlastResults;
    }

    public PieChart getPieChartResidueTypes() {
        return pieChartResidueTypes;
    }

    public MenuItem getMenuInformationResidueTypes() {
        return menuInformationResidueTypes;
    }

    public MenuItem getMenuColorSelectedAtoms() {
        return menuColorSelectedAtoms;
    }

    public MenuItem getMenuChangeDefaultColors() {
        return menuChangeDefaultColors;
    }

    public MenuItem getMenuResetToDefaultColors() {
        return menuResetToDefaultColors;
    }

    public MenuItem getMenuInformationSecondaryStructure() {
        return menuInformationSecondaryStructure;
    }

    public PieChart getPieChartSecondaryStructure() {
        return pieChartSecondaryStructure;
    }

    public Button getCalculateDistanceButton() {
        return calculateDistanceButton;
    }
}
