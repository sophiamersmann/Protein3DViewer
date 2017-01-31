package protein3DViewer.presenter;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import protein3DViewer.MySelectionModel;
import protein3DViewer.model.SeqResRecord;
import protein3DViewer.view.SelectableLabel;
import protein3DViewer.view.SequenceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class SequencePresenter {

    private SeqResRecord seqResRecord;
    private SequenceView sequenceView;

    private List<SelectableLabel> residueViews;
    private MySelectionModel<SelectableLabel> selectionModelResidueSeq;

    private MySelectionModel<SelectableLabel> selectionModelAnnotationSeq;

    public SequencePresenter(SequenceView sequenceView, SeqResRecord seqResRecord) {
        this.sequenceView = sequenceView;
        this.seqResRecord = seqResRecord;
        initSelectionModels();
        setUpResidueViews();
        setUpSecondaryStructureAnnotations();
    }

    private void initSelectionModels() {
        residueViews = new ArrayList<>(sequenceView.getResidueViews().values());
        selectionModelResidueSeq = new MySelectionModel(residueViews.toArray());
        selectionModelResidueSeq.getSelectedItems().addListener(new ListChangeListener<SelectableLabel>() {
            @Override
            public void onChanged(Change<? extends SelectableLabel> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (SelectableLabel label: c.getAddedSubList()) {
                            label.setTextFill(Color.RED);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (SelectableLabel label: c.getRemoved()) {
                            label.setTextFill(Color.BLACK);
                        }
                    }
                }
            }

        });

        selectionModelAnnotationSeq = new MySelectionModel(sequenceView.getSecondaryStructureAnnotations().toArray());
        selectionModelAnnotationSeq.getSelectedItems().addListener(new ListChangeListener<SelectableLabel>() {
            @Override
            public void onChanged(Change<? extends SelectableLabel> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (SelectableLabel label: c.getAddedSubList()) {
                            label.setTextFill(Color.RED);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (SelectableLabel label: c.getRemoved()) {
                            label.setTextFill(Color.BLACK);
                        }
                    }
                }
            }
        });
    }

    private void setUpResidueViews() {
        for (SelectableLabel label: residueViews) {
            setUpSelectableLabel(label, residueViews, selectionModelResidueSeq);
        }
    }

    private void setUpSecondaryStructureAnnotations() {
        for (SelectableLabel label: sequenceView.getSecondaryStructureAnnotations()) {
            if (!label.getText().equals(" ")) {
                setUpSelectableLabel(label, sequenceView.getSecondaryStructureAnnotations(), selectionModelAnnotationSeq);
            }
        }
    }

    private void setUpSelectableLabel(SelectableLabel label, List<SelectableLabel> labels, MySelectionModel selectionModel) {
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                label.setSelected(!label.isSelected());
            }
        });

        label.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    selectionModel.select(labels.indexOf(label));
                } else {
                    selectionModel.clearSelection();
                }
            }
        });
    }





}
