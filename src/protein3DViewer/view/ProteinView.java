package protein3DViewer.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import protein3DViewer.model.Protein;
import protein3DViewer.presenter.ModelPresenter;
import protein3DViewer.presenter.SequencePresenter;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ProteinView {

    private Protein protein;

    private BorderPane borderPane;

    private SequenceView sequenceView;
    private ModelView modelView;

    private MenuBar menuBar = new MenuBar();
    private Menu menuFile = new Menu("File");
    private MenuItem open = new MenuItem("Open...");
    private Menu menuEdit = new Menu("Edit");
    private Menu menuView = new Menu("View");
    private Menu show = new Menu("Show");
    private CheckMenuItem showAtomsMenu = new CheckMenuItem("Atoms");
    private CheckMenuItem showBondsMenu = new CheckMenuItem("Bonds");
    private Menu visualization = new Menu("Visualization");
    private CheckMenuItem sticks = new CheckMenuItem("Sticks");
    private CheckMenuItem ribbon = new CheckMenuItem("Ribbon");
    private CheckMenuItem cartoon = new CheckMenuItem("Cartoon");
    private Menu colorBy = new Menu("Color by...");
    private CheckMenuItem colorByAminoAcid = new CheckMenuItem(AtomView.COLOR_BY_AMINO_ACID);
    private CheckMenuItem colorBySecondaryStructure = new CheckMenuItem(AtomView.COLOR_BY_SECONDARY_STRUCTURE);
    private CheckMenuItem colorByProperties = new CheckMenuItem(AtomView.COLOR_BY_PROPERTIES);
    private Menu atomSizeMenu = new Menu("Atom Size");
    private MenuItem increaseAtomSize = new MenuItem("Increase");
    private MenuItem decreaseAtomSize = new MenuItem("Decrease");
    private Menu bondSizeMenu = new Menu("Bond Size");
    private MenuItem increaseBondSize = new MenuItem("Increase");
    private MenuItem decreaseBondSize = new MenuItem("Decrease");

    private ToolBar toolBar = new ToolBar();
    private CheckBox showAtoms = new CheckBox("Show Atoms");
    private CheckBox showBonds = new CheckBox("Show Bonds");
    private ChoiceBox<String> chooseVisualization = new ChoiceBox<>(FXCollections.observableArrayList("Sticks", "Ribbon", "Cartoon"));
    private Slider atomSizeSlider = new Slider(-0.8, 0.8, 0);
    private Slider bondSizeSlider = new Slider(-1, 1, 0);
    private ChoiceBox<String> chooseColoring = new ChoiceBox<>(FXCollections.observableArrayList(
            AtomView.COLOR_BY_AMINO_ACID, AtomView.COLOR_BY_SECONDARY_STRUCTURE, AtomView.COLOR_BY_PROPERTIES));

    public ProteinView(BorderPane borderPane, Protein protein) {
        this.protein = protein;
        this.borderPane = borderPane;
        initViews();
        initMenuBar();
        initToolBar();
    }

    private void initMenuBar() {
        menuFile.getItems().add(open);
        showAtomsMenu.setSelected(true);
        showBondsMenu.setSelected(true);
        show.getItems().addAll(showAtomsMenu, showBondsMenu);
        sticks.setSelected(true);
        visualization.getItems().addAll(sticks, ribbon, cartoon);
        atomSizeMenu.getItems().addAll(increaseAtomSize, decreaseAtomSize);
        bondSizeMenu.getItems().addAll(increaseBondSize, decreaseBondSize);
        menuView.getItems().addAll(show, visualization, colorBy, atomSizeMenu, bondSizeMenu);
        colorByAminoAcid.setSelected(true);
        colorBy.getItems().addAll(colorByAminoAcid, colorBySecondaryStructure, colorByProperties);
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        menuBar.useSystemMenuBarProperty().set(true);
        borderPane.getChildren().add(menuBar);
    }

    private void initToolBar() {
        showAtoms.setSelected(true);
        showBonds.setSelected(true);
        chooseVisualization.setValue("Sticks");
        chooseColoring.setValue("Amino Acids");

        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setPrefWidth(10);  // TODO: hard coded right now
        toolBar.setMaxWidth(10);
        toolBar.getItems().addAll(
                showAtoms,
                atomSizeSlider,
                new Separator(),
                showBonds,
                bondSizeSlider,
                new Separator(),
                new Label("Visualization"),
                chooseVisualization,
                new Separator(),
                new Label("Color by..."),
                chooseColoring
        );

        borderPane.setLeft(toolBar);
        BorderPane.setMargin(borderPane.getLeft(), new Insets(0, 10, 10, 0));
        BorderPane.setAlignment(borderPane.getLeft(), Pos.TOP_CENTER);
    }

    private void initViews() {
        modelView = new ModelView(protein.getModel());
        new ModelPresenter(modelView, protein.getModel());
        borderPane.setCenter(modelView);
//        BorderPane.setMargin(borderPane.getCenter(), new Insets(5));
        BorderPane.setAlignment(borderPane.getCenter(), Pos.CENTER);

        sequenceView = new SequenceView(protein.getSeqResRecord(), protein.getSecondaryStructure());
        new SequencePresenter(sequenceView);
        borderPane.setTop(sequenceView);
        BorderPane.setMargin(borderPane.getTop(), new Insets(10, 0, 10, 0));
        BorderPane.setAlignment(borderPane.getTop(), Pos.CENTER);
    }

    public ToolBar getToolBar() {
        return toolBar;
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

    public CheckMenuItem getColorByAminoAcid() {
        return colorByAminoAcid;
    }

    public CheckMenuItem getColorBySecondaryStructure() {
        return colorBySecondaryStructure;
    }

    public CheckMenuItem getColorByProperties() {
        return colorByProperties;
    }

    public CheckMenuItem getShowAtomsMenu() {
        return showAtomsMenu;
    }

    public CheckMenuItem getShowBondsMenu() {
        return showBondsMenu;
    }

    public MenuItem getIncreaseAtomSize() {
        return increaseAtomSize;
    }

    public MenuItem getDecreaseAtomSize() {
        return decreaseAtomSize;
    }

    public MenuItem getIncreaseBondSize() {
        return increaseBondSize;
    }

    public MenuItem getDecreaseBondSize() {
        return decreaseBondSize;
    }

    public Menu getAtomSizeMenu() {
        return atomSizeMenu;
    }

    public Menu getBondSizeMenu() {
        return bondSizeMenu;
    }

    public ChoiceBox<String> getChooseColoring() {
        return chooseColoring;
    }
}
