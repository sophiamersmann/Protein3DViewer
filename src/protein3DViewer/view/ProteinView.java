package protein3DViewer.view;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import protein3DViewer.model.Protein;
import protein3DViewer.presenter.SequencePresenter;
import protein3DViewer.presenter.ModelPresenter;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ProteinView {

    private Protein protein;

    private GridPane gridPane;

    private SequenceView sequenceView;
    private ModelView modelView;

    private MenuBar menuBar = new MenuBar();
    private Menu menuFile = new Menu("File");
    private Menu menuEdit = new Menu("Edit");
    private Menu menuView = new Menu("View");
    private MenuItem open = new MenuItem("Open...");
    private Menu colorBy = new Menu("Color by...");
    private MenuItem colorByAminoAcid = new MenuItem("Amino acid");
    private MenuItem colorBySecondaryStructure = new MenuItem("Secondary structure");
    private MenuItem colorByProperties = new MenuItem("Physicochemical Properties");

    private ToolBar toolBar = new ToolBar();
    private HBox box = new HBox(10);
    private CheckBox showAtoms = new CheckBox("Show Atoms");
    private CheckBox showBonds = new CheckBox("Show Bonds");
    private ChoiceBox chooseVisualization = new ChoiceBox(FXCollections.observableArrayList("Sticks", "Ribbon", "Cartoon"));
    private Slider atomSizeSlider = new Slider(-0.8, 0.8, 0);
    private Slider bondSizeSlider = new Slider(-1, 1, 0);

    private int OFFSET = 10;
    private int[] PERC_HEIGHT_OF_ROWS = new int[]{5, 5, 90};

    public ProteinView(GridPane gridPane, Protein protein) {
        this.protein = protein;
        this.gridPane = gridPane;
        initLayout();
        createMenuBar();
        createToolBar();
        initViews();
    }

    private void initLayout() {
        for (int percHeight : PERC_HEIGHT_OF_ROWS) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(percHeight);
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void createMenuBar() {
        menuFile.getItems().add(open);
        menuView.getItems().add(colorBy);
        colorBy.getItems().addAll(colorByAminoAcid, colorBySecondaryStructure, colorByProperties);
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        menuBar.useSystemMenuBarProperty().set(true);
        gridPane.getChildren().add(menuBar);
    }

    private void createToolBar() {
        showAtoms.setSelected(true);
        showBonds.setSelected(true);
        chooseVisualization.setValue("Sticks");
        box.getChildren().addAll(showAtoms, showBonds, chooseVisualization, atomSizeSlider, bondSizeSlider);
        box.setAlignment(Pos.CENTER_LEFT);

        toolBar.prefWidthProperty().bind(gridPane.prefWidthProperty());
        toolBar.getItems().add(box);
        GridPane.setConstraints(toolBar, 0, 0);
        gridPane.getChildren().add(toolBar);
    }

    private void initViews() {
        SequencePresenter sequencePresenter = new SequencePresenter();
        sequenceView = new SequenceView(sequencePresenter, protein.getSeqResRecord(), protein.getSecondaryStructure());
        GridPane.setConstraints(sequenceView, 0, 1);
        GridPane.setHalignment(sequenceView, HPos.CENTER);

        ModelPresenter modelPresenter = new ModelPresenter();
        modelView = new ModelView(modelPresenter, protein.getModel());
        GridPane.setConstraints(modelView, 0, 2);
        GridPane.setHalignment(modelView, HPos.CENTER);

        gridPane.getChildren().addAll(sequenceView, modelView);


    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public SequenceView getSequenceView() {
        return sequenceView;
    }

    public ModelView getModelView() {
        return modelView;
    }

    public CheckBox getShowAtoms() {
        return showAtoms;
    }

    public CheckBox getShowBonds() {
        return showBonds;
    }

    public ChoiceBox getChooseVisualization() {
        return chooseVisualization;
    }

    public Slider getAtomSizeSlider() {
        return atomSizeSlider;
    }

    public Slider getBondSizeSlider() {
        return bondSizeSlider;
    }

    public MenuItem getOpen() {
        return open;
    }

    public MenuItem getColorByAminoAcid() {
        return colorByAminoAcid;
    }

    public MenuItem getColorBySecondaryStructure() {
        return colorBySecondaryStructure;
    }

    public MenuItem getColorByProperties() {
        return colorByProperties;
    }
}
