package protein3DViewer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
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
        // get pdb file from the user
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDB files (*.pdb)", "*.pdb");
        fileChooser.getExtensionFilters().add(extFilter);
        File pdbFile = fileChooser.showOpenDialog(primaryStage);

        // read in pdb file and build protein data structure
        Protein protein = new Protein();
        PDBParser parser = new PDBParser(pdbFile);
        new Director(parser, protein);
        protein.getModel().createBonds();

        BorderPane borderPane = new BorderPane();
        ProteinView proteinView = new ProteinView(borderPane, protein);
        new ProteinPresenter(proteinView, protein);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(borderPane, screen.getWidth(), screen.getHeight());

        // bind border pane to scene
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                borderPane.setPrefWidth((double) newValue);
                borderPane.setMaxWidth((double) newValue);
                borderPane.setMinWidth((double) newValue);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                borderPane.setPrefHeight((double) newValue);
                borderPane.setMaxHeight((double) newValue);
                borderPane.setMinHeight((double) newValue);

            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
