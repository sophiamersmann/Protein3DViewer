package protein3DViewer;

import javafx.application.Application;
import javafx.stage.Stage;
import protein3DViewer.model.Protein;

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

        System.out.println(protein.getSeqResRecord());
        System.out.println(protein.getSecondaryStructures());
        System.out.println(protein.getModel());
        System.exit(0);

    }
}
