package protein3DViewer;

/**
 * Created by sophiamersmann on 22/01/2017.
 */
public class BlastSearchResultParser {

    private String firstAlignment;

    public BlastSearchResultParser(String[] blastResult) {
        int i = 0;
        while (i < blastResult.length) {
            if (blastResult[i].startsWith(">")) {
                StringBuilder sb = new StringBuilder();
                do {
                    sb.append(blastResult[i] + "\n");
                    i++;
                } while (!blastResult[i].startsWith(">"));
                firstAlignment = sb.toString();
                break;
            }
            i++;
        }


    }

    public String getFirstAlignment() {
        return firstAlignment;
    }
}
