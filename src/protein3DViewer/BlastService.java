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
                remoteBlastClient.setProgram(RemoteBlastClient.BlastProgram.blastx).setDatabase("nr");

                remoteBlastClient.startRemoteSearch(sequence);

                System.err.println("Request id: " + remoteBlastClient.getRequestId());
                System.err.println("Estimated time: " + remoteBlastClient.getEstimatedTime() + "s");

                RemoteBlastClient.Status status = null;
                do {
                    if (status != null)
                        Thread.sleep(5000);
                    status = remoteBlastClient.getRemoteStatus();
                }
                while (status == RemoteBlastClient.Status.searching);

                switch (status) {
                    case hitsFound:
                        for (String line : remoteBlastClient.getRemoteAlignments()) {
                            System.out.println(line);
                        }
                        break;
                    case noHitsFound:
                        System.err.println("No hits");
                        break;
                    default:
                        System.err.println("Status: " + status);
                }

                System.err.println("Actual time: " + remoteBlastClient.getActualTime() + "s");

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
