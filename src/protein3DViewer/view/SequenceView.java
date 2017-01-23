package protein3DViewer.view;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import protein3DViewer.BlastSearchResultParser;
import protein3DViewer.BlastService;
import protein3DViewer.model.Chain;
import protein3DViewer.model.Residue;
import protein3DViewer.model.SecondaryStructure;
import protein3DViewer.model.SeqResRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class SequenceView extends Group {

    private SeqResRecord seqResRecord;
    private SecondaryStructure secondaryStructure;

    private BlastService blastService = new BlastService();
    private String[] blastResult;

    private Map<Integer, Label> residueViews = new HashMap<>();
    private Map<Integer, Label> secondaryStructureAnnotations = new HashMap<>();

    private VBox sequenceBox = new VBox();
    private HBox residueViewBox = new HBox(2);
    private HBox secondaryStructureAnnotationBox = new HBox(2);

    public SequenceView(SeqResRecord seqResRecord, SecondaryStructure secondaryStructures) {
        this.seqResRecord = seqResRecord;
        this.secondaryStructure = secondaryStructures;
//        startBlastService();
        initResidueViews();
        initSecondaryStructureAnnotation();
        sequenceBox.setAlignment(Pos.CENTER_LEFT);
        sequenceBox.getChildren().addAll(residueViewBox, secondaryStructureAnnotationBox);
        getChildren().add(sequenceBox);
    }

    private void startBlastService() {  // TODO: is not happening
        blastService.setSequence(seqResRecord.generateAminoAcidSequence());
        blastService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                blastResult = (String[]) t.getSource().getValue();
                new BlastSearchResultParser(blastResult);
            }
        });
        blastService.start();
    }

    private void initResidueViews() {
        for (Chain chain : seqResRecord.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                Label resLabel = new Label(residue.getName().toString());
                residueViews.put(residue.getId(), resLabel);
                residueViewBox.getChildren().add(resLabel);
            }
        }
    }

    private void initSecondaryStructureAnnotation() {
        for (Chain chain : seqResRecord.getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                Label secStrLabel = new Label(" ");
                if (residue.isInHelix()) {
                    secStrLabel = new Label("H");
                } else if (residue.isInSheet()) {
                    secStrLabel = new Label("E");
                }
                secondaryStructureAnnotations.put(residue.getId(), secStrLabel);
                secondaryStructureAnnotationBox.getChildren().add(secStrLabel);
            }
        }
    }

}
