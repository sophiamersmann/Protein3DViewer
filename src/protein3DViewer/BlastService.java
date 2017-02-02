package protein3DViewer;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by sophiamersmann on 22/01/2017.
 */
public class BlastService extends Service<String[]> {

    private String sequence;

    protected Task<String[]> createTask() {
        return new Task<String[]>() {
            protected String[] call() throws InterruptedException {
                final RemoteBlastClient remoteBlastClient = new RemoteBlastClient();
                remoteBlastClient.setProgram(RemoteBlastClient.BlastProgram.blastp).setDatabase("nr");
                remoteBlastClient.startRemoteSearch(sequence);

                RemoteBlastClient.Status status = null;
                do {
                    if (status != null)
                        Thread.sleep(5000);
                    status = remoteBlastClient.getRemoteStatus();
                }
                while (status == RemoteBlastClient.Status.searching);

                return remoteBlastClient.getRemoteAlignments();
            }
        };
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
