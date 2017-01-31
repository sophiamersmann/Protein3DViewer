package protein3DViewer.view;

import javafx.collections.ListChangeListener;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import protein3DViewer.BlastSearchResultParser;
import protein3DViewer.BlastService;
import protein3DViewer.MySelectionModel;
import protein3DViewer.model.Chain;
import protein3DViewer.model.Residue;
import protein3DViewer.model.SecondaryStructure;
import protein3DViewer.model.SeqResRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class SequenceView extends Group {

    private SeqResRecord seqResRecord;
    private SecondaryStructure secondaryStructure;

    private BlastService blastService = new BlastService();
    private String[] blastResult;

    private Map<Integer, SelectableLabel> residueViews = new HashMap<>();
    private List<SelectableLabel> secondaryStructureAnnotations = new ArrayList<>();

    private HBox sequenceBox = new HBox();

    private MySelectionModel<SelectableLabel> selectionModelResidueSeq;
    private MySelectionModel<SelectableLabel> selectionModelAnnotationSeq;

    public SequenceView(SeqResRecord seqResRecord, SecondaryStructure secondaryStructures) {
        this.seqResRecord = seqResRecord;
        this.secondaryStructure = secondaryStructures;
        initTextViews();
        initSelectionModels();
        getChildren().addAll(sequenceBox);
    }

    private void initSelectionModels() {
        List<SelectableLabel> residueViewsAsList = new ArrayList<>(residueViews.values());
        selectionModelResidueSeq = new MySelectionModel(residueViewsAsList.toArray());
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

        selectionModelAnnotationSeq = new MySelectionModel(secondaryStructureAnnotations.toArray());
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

    private void initTextViews() {
        for (Chain chain : seqResRecord.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                SelectableLabel resLabel = new SelectableLabel(residue, residue.getName().toString());
                residueViews.put(residue.getId(), resLabel);

                SelectableLabel secStrLabel = new SelectableLabel(residue, " ");
                if (residue.isInHelix()) {
                    secStrLabel = new SelectableLabel(residue, "H");
                } else if (residue.isInSheet()) {
                    secStrLabel = new SelectableLabel(residue, "E");
                }
                secondaryStructureAnnotations.add(secStrLabel);

                resLabel.selectedProperty().bindBidirectional(secStrLabel.selectedProperty());
                resLabel.shiftSelectedProperty().bindBidirectional(secStrLabel.shiftSelectedProperty());

                VBox box = new VBox(resLabel, secStrLabel);
                box.setAlignment(Pos.CENTER);
                sequenceBox.getChildren().add(box);
            }
        }
    }

    public Map<Integer, SelectableLabel> getResidueViews() {
        return residueViews;
    }

    public List<SelectableLabel> getSecondaryStructureAnnotations() {
        return secondaryStructureAnnotations;
    }

    public MySelectionModel<SelectableLabel> getSelectionModelResidueSeq() {
        return selectionModelResidueSeq;
    }

    public MySelectionModel<SelectableLabel> getSelectionModelAnnotationSeq() {
        return selectionModelAnnotationSeq;
    }
}
