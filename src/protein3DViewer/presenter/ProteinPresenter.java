package protein3DViewer.presenter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import protein3DViewer.model.Protein;
import protein3DViewer.view.AtomView;
import protein3DViewer.view.ProteinView;

import java.io.File;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class ProteinPresenter {

    private ProteinView proteinView;
    private Protein protein;

    public ProteinPresenter(ProteinView proteinView, Protein protein) {
        this.proteinView = proteinView;
        this.protein = protein;
        setUpPane();
        setUpMenuBar();
        setUpToolBar();
    }

    private void setUpPane() {

        proteinView.getGridPane().setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();
                if (event.getDeltaY() == 0) {
                    return;
                }
                double scaleDelta = 1.1;
                double scaleFactor = (event.getDeltaY() > 0) ? scaleDelta : 1 / scaleDelta;
                proteinView.getModelView().setScaleX(proteinView.getModelView().getScaleX() * scaleFactor);
                proteinView.getModelView().setScaleY(proteinView.getModelView().getScaleY() * scaleFactor);
            }
        });
    }

    private void setUpMenuBar() {

        proteinView.getOpen().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDB files (*.pdb)", "*.pdb");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(proteinView.getModelView().getScene().getWindow());
                // TODO load new file (observable lists?)
            }
        });

        proteinView.getColorByAminoAcid().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                proteinView.getModelView().changeAtomColor(AtomView.COLOR_BY_AMINO_ACID);
            }
        });

        proteinView.getColorBySecondaryStructure().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                proteinView.getModelView().changeAtomColor(AtomView.COLOR_BY_SECONDARY_STRUCTURE);
            }
        });
    }

    private void setUpToolBar() {

        proteinView.getShowAtoms().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    proteinView.getModelView().showAtomViews();
                } else {
                    proteinView.getModelView().hideAtomViews();
                }
            }
        });

        proteinView.getAtomSizeSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                proteinView.getModelView().changeAtomSize((Double) newValue - (Double) oldValue);
            }
        });

    }

}
