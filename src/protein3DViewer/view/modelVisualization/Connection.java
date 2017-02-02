package protein3DViewer.view.modelVisualization;

import protein3DViewer.view.atomView.AbstractAtomView;
import protein3DViewer.view.bondView.Line;

/**
 * Created by sophiamersmann on 02/02/2017.
 */
public class Connection extends Line {

    private AtomLabel label;

    private AbstractAtomView startAtomView;
    private AbstractAtomView endAtomView;

    public Connection(AbstractAtomView startAtomView, AbstractAtomView endAtomView) {
        super(startAtomView.getX(), startAtomView.getY(), startAtomView.getZ(),
                endAtomView.getX(), endAtomView.getY(), endAtomView.getZ());
        this.startAtomView = startAtomView;
        this.endAtomView = endAtomView;
    }

    public AtomLabel getLabel() {
        return label;
    }

    public void setLabel(AtomLabel label) {
        this.label = label;
    }

    public AbstractAtomView getStartAtomView() {
        return startAtomView;
    }

    public void setStartAtomView(AbstractAtomView startAtomView) {
        this.startAtomView = startAtomView;
    }

    public AbstractAtomView getEndAtomView() {
        return endAtomView;
    }

    public void setEndAtomView(AbstractAtomView endAtomView) {
        this.endAtomView = endAtomView;
    }
}
