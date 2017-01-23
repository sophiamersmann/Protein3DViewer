package protein3DViewer;

import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import protein3DViewer.model.Protein;
import protein3DViewer.presenter.ProteinPresenter;
import protein3DViewer.view.ProteinView;

import java.awt.*;
import java.io.File;

public class Main extends Application {

    private String[] blastResult;

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

        BlastService blastService = new BlastService();
        blastService.setSequence(protein.getSeqResRecord().generateAminoAcidSequence());
        blastService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                blastResult = (String[]) t.getSource().getValue();
                new BlastSearchResultParser(blastResult);
            }
        });
        blastService.start();

        BorderPane borderPane = new BorderPane();
        ProteinView proteinView = new ProteinView(borderPane, protein);
        new ProteinPresenter(proteinView, protein);
//        proteinView.getToolBar().prefWidthProperty().bind(primaryStage.widthProperty());
//        proteinView.getSequenceView().getSequenceTextArea().prefWidthProperty().bind(primaryStage.widthProperty());

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(borderPane, screen.getWidth(), screen.getHeight());
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
