package protein3DViewer;

import java.util.Arrays;

/**
 * Created by sophiamersmann on 22/01/2017.
 */
public class BlastSearchResultParser {

    private String firstAlignment;

    public BlastSearchResultParser(String[] blastResult) {
        int i = 0;
        while (i < blastResult.length) {
//            if (blastResult[i].contains("Sequences producing significant alignments:")) {
//                i += 2;
//                String[] splitLine = blastResult[i].split(" ");
//                String id = splitLine[0];
//                Integer score = Integer.parseInt(splitLine[splitLine.length - 2]);
//                Float eValue = Float.parseFloat(splitLine[splitLine.length - 1]);
//                System.out.println(id + " " + score + " " + eValue);
//            }
//            if (blastResult[i].startsWith(">")) {
//                String[] splitLine = blastResult[i].replaceAll(">", "").split(" ");
//                String id = splitLine[0];
//                splitLine = Arrays.copyOfRange(splitLine, 1, splitLine.length);
//                String description = String.join(" ", splitLine);
//                i++;
//                while (!blastResult[i].trim().isEmpty()) {
//                    description += blastResult[i];
//                    i++;
//                }
//                i++;
//                while (!blastResult[i].trim().isEmpty()) {
//                    splitLine = blastResult[i].trim().split(" = ");
//                    for (int j = 0; j < splitLine.length; j++) {
//                        if (splitLine[j].contains("Score")) {
//                            Integer score = Integer.parseInt(splitLine[j+1].split(" ")[0].trim());
//                        }
//                        if (splitLine[j].contains("Expect")) {
//                            Float eValue = Float.parseFloat(splitLine[j+1].split(",")[0].trim());
//                        }
//                        if (splitLine[j].contains("Identities")) {
//                            Float
//                        }
//                    }
//                }
//            }
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
