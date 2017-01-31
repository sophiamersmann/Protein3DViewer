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

    public SequenceView(SeqResRecord seqResRecord, SecondaryStructure secondaryStructures) {
        this.seqResRecord = seqResRecord;
        this.secondaryStructure = secondaryStructures;
        initTextViews();
        getChildren().addAll(sequenceBox);
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

                if (!secStrLabel.getText().equals(" ")) {
                    resLabel.selectedProperty().bindBidirectional(secStrLabel.selectedProperty());
                }

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
}
