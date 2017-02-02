package protein3DViewer.presenter;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import protein3DViewer.MySelectionModel;
import protein3DViewer.model.SeqResRecord;
import protein3DViewer.view.SelectableLabel;
import protein3DViewer.view.SequenceView;
import protein3DViewer.view.VisualizationMode;
import protein3DViewer.view.modelVisualization.SticksVisualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class SequencePresenter {

    private ModelPresenter modelPresenter;

    private SeqResRecord seqResRecord;
    private SequenceView sequenceView;

    private List<SelectableLabel> residueViews;

    public SequencePresenter(ModelPresenter modelPresenter, SequenceView sequenceView, SeqResRecord seqResRecord) {
        this.modelPresenter = modelPresenter;
        this.sequenceView = sequenceView;
        this.seqResRecord = seqResRecord;
        setUpResidueViews();
        setUpSecondaryStructureAnnotations();
    }

    /**
     * set up labels for the SeqRes record sequence
     */
    private void setUpResidueViews() {
        residueViews = new ArrayList<>(sequenceView.getResidueViews().values());
        for (SelectableLabel label: residueViews) {
            setUpSelectableLabel(label, residueViews, sequenceView.getSelectionModelResidueSeq());
        }
    }

    /**
     * set up labels annotating each residue by its secondary structure
     */
    private void setUpSecondaryStructureAnnotations() {
        for (SelectableLabel label: sequenceView.getSecondaryStructureAnnotations()) {
            setUpSelectableLabel(label, sequenceView.getSecondaryStructureAnnotations(), sequenceView.getSelectionModelAnnotationSeq());

        }
    }

    /**
     * make a selectable label clickable
     *
     * @param label the label
     * @param labels list of labels containing the given label
     * @param selectionModel corresponding selection model
     */
    private void setUpSelectableLabel(SelectableLabel label, List<SelectableLabel> labels, MySelectionModel selectionModel) {

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                if (event.isShiftDown()) {
                    label.setShiftSelected(!label.isShiftSelected());
                } else {
                    label.setSelected(!label.isSelected());
                    int atomID = (int) label.getResidue().getAtoms().keySet().toArray()[0];
                    SticksVisualization sticksVis = (SticksVisualization) modelPresenter.getModelView().getModelVisualization(VisualizationMode.STICKS);
                    modelPresenter.shiftSelectAllAtomViewsOfResidue(sticksVis.getAtomViews().get(atomID));
                }
            }
        });

        label.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    selectionModel.clearAndSelect(labels.indexOf(label));
                } else {
                    selectionModel.clearSelection();
                }
            }
        });

        label.shiftSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    selectionModel.select(labels.indexOf(label));
                } else {
                    selectionModel.clearSelection(labels.indexOf(label));
                }
            }
        });
    }

}
