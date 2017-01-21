package protein3DViewer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import protein3DViewer.model.Protein;
import protein3DViewer.presenter.ProteinPresenter;
import protein3DViewer.view.ProteinView;

import java.awt.*;
import java.io.File;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String pdbFilename = "/Users/sophiamersmann/Dropbox/Uni_Tuebingen/studies/MSc/semester_1_ws1617/advanced_java_for_bioinformatics/project/1ey4.pdb";
        File pdbFile = new File(pdbFilename);

        Protein protein = new Protein();
        PDBParser parser = new PDBParser(pdbFile);
        new Director(parser, protein);
        protein.getModel().createBonds();

        GridPane gridPane = new GridPane();

        ProteinView proteinView = new ProteinView(gridPane, protein);
        ProteinPresenter proteinPresenter = new ProteinPresenter(proteinView, protein);
        proteinView.getToolBar().prefWidthProperty().bind(primaryStage.widthProperty());

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(gridPane, screen.getWidth(), screen.getHeight());
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
