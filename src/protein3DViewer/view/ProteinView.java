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
    private MenuItem menuOpen = new MenuItem("Open...");
    private Menu menuEdit = new Menu("Edit");
    private Menu menuView = new Menu("View");
    private Menu menuShow = new Menu("Show");
    private CheckMenuItem menuShowAtoms = new CheckMenuItem("Atoms");
    private CheckMenuItem menuShowBonds = new CheckMenuItem("Bonds");
    private Menu menuVisualization = new Menu("Visualization");
    private CheckMenuItem menuVisualizeSticks = new CheckMenuItem("Sticks");
    private CheckMenuItem menuVisualizeRibbon = new CheckMenuItem("Ribbon");
    private CheckMenuItem menuVisualizeCartoon = new CheckMenuItem("Cartoon");
    private Menu menuColorBy = new Menu("Color by...");
    private CheckMenuItem menuColorBySingleColor = new CheckMenuItem(AtomView.COLOR_BY_SINGLE_COLOR);
    private CheckMenuItem menuColorByAminoAcid = new CheckMenuItem(AtomView.COLOR_BY_AMINO_ACID);
    private CheckMenuItem menuColorBySecondaryStructure = new CheckMenuItem(AtomView.COLOR_BY_SECONDARY_STRUCTURE);
    private CheckMenuItem menuColorByProperties = new CheckMenuItem(AtomView.COLOR_BY_PROPERTIES);
    private Menu menuAtomSize = new Menu("Atom Size");
    private MenuItem menuIncreaseAtomSize = new MenuItem("Increase");
    private MenuItem menuDecreaseAtomSize = new MenuItem("Decrease");
    private Menu menuBondSize = new Menu("Bond Size");
    private MenuItem menuIncreaseBondSize = new MenuItem("Increase");
    private MenuItem menuDecreaseBondSize = new MenuItem("Decrease");

    private ToolBar toolBar = new ToolBar();
    private CheckBox showAtoms = new CheckBox("Show Atoms");
    private CheckBox showBonds = new CheckBox("Show Bonds");
    private CheckBox visualizeSticks = new CheckBox("Sticks");
    private CheckBox visualizeRibbon = new CheckBox("Ribbon");
    private CheckBox visualizeCartoon = new CheckBox("Cartoon");
    private Slider atomSizeSlider = new Slider(-0.8, 0.8, 0);
    private Slider bondSizeSlider = new Slider(-1, 1, 0);
    private ChoiceBox<String> chooseColoring = new ChoiceBox<>(FXCollections.observableArrayList(
            AtomView.COLOR_BY_SINGLE_COLOR, AtomView.COLOR_BY_AMINO_ACID,
            AtomView.COLOR_BY_SECONDARY_STRUCTURE, AtomView.COLOR_BY_PROPERTIES
    ));

    public ProteinView(BorderPane borderPane, Protein protein) {
        this.protein = protein;
        this.borderPane = borderPane;
        initViews();
        initMenuBar();
        initToolBar();
        initBindings();
    }

    private void initMenuBar() {
        menuFile.getItems().add(menuOpen);

        menuShow.getItems().addAll(menuShowAtoms, menuShowBonds);
        menuVisualization.getItems().addAll(menuVisualizeSticks, menuVisualizeRibbon, menuVisualizeCartoon);
        menuColorBy.getItems().addAll(menuColorBySingleColor, menuColorByAminoAcid, menuColorBySecondaryStructure, menuColorByProperties);
        menuAtomSize.getItems().addAll(menuIncreaseAtomSize, menuDecreaseAtomSize);
        menuBondSize.getItems().addAll(menuIncreaseBondSize, menuDecreaseBondSize);
        menuView.getItems().addAll(menuShow, menuVisualization, menuColorBy, menuAtomSize, menuBondSize);

        menuShowAtoms.setSelected(true);
        menuShowBonds.setSelected(true);
        menuVisualizeSticks.setSelected(true);
        menuColorByAminoAcid.setSelected(true);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        menuBar.useSystemMenuBarProperty().set(true);
        borderPane.getChildren().add(menuBar);
    }

    private void initToolBar() {
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
                visualizeSticks,
                visualizeRibbon,
                visualizeCartoon,
                new Separator(),
                new Label("Color by..."),
                chooseColoring
        );

        showAtoms.setSelected(true);
        showBonds.setSelected(true);
        visualizeSticks.setSelected(true);
        chooseColoring.setValue("Amino Acids");

        borderPane.setLeft(toolBar);
        BorderPane.setMargin(borderPane.getLeft(), new Insets(0, 10, 10, 0));
        BorderPane.setAlignment(borderPane.getLeft(), Pos.TOP_CENTER);
    }

    private void initViews() {
        modelView = new ModelView(protein.getModel());
        new ModelPresenter(modelView, protein.getModel());
        borderPane.setCenter(modelView);
        BorderPane.setAlignment(borderPane.getCenter(), Pos.CENTER);

        sequenceView = new SequenceView(protein.getSeqResRecord(), protein.getSecondaryStructure());
        new SequencePresenter(sequenceView);
        borderPane.setTop(sequenceView);
        BorderPane.setMargin(borderPane.getTop(), new Insets(10, 0, 10, 0));
        BorderPane.setAlignment(borderPane.getTop(), Pos.CENTER);
    }

    private void initBindings() {
        showAtoms.selectedProperty().bindBidirectional(menuShowAtoms.selectedProperty());
        showBonds.selectedProperty().bindBidirectional(menuShowBonds.selectedProperty());
        visualizeSticks.selectedProperty().bindBidirectional(menuVisualizeSticks.selectedProperty());
        visualizeRibbon.selectedProperty().bindBidirectional(menuVisualizeRibbon.selectedProperty());
        visualizeCartoon.selectedProperty().bindBidirectional(menuVisualizeCartoon.selectedProperty());
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

    public Menu getMenuAtomSize() {
        return menuAtomSize;
    }

    public MenuItem getMenuIncreaseAtomSize() {
        return menuIncreaseAtomSize;
    }

    public MenuItem getMenuDecreaseAtomSize() {
        return menuDecreaseAtomSize;
    }

    public Menu getMenuBondSize() {
        return menuBondSize;
    }

    public MenuItem getMenuIncreaseBondSize() {
        return menuIncreaseBondSize;
    }

    public MenuItem getMenuDecreaseBondSize() {
        return menuDecreaseBondSize;
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

    public ChoiceBox<String> getChooseColoring() {
        return chooseColoring;
    }
}
