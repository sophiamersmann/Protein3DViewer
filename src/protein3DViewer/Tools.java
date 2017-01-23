package protein3DViewer;


/**
 * Created by sophiamersmann on 20/01/2017.
 */
public class Tools {

    /**
     * translates amino acid three letter code to one letter code
     *
     * @param residue amino acid in three letter code
     * @return amino acid in one letter code
     */
    public static Character aminoAcid3to1(String residue) {
        switch (residue) {
            case "ALA":
                return 'A';
            case "ARG":
                return 'R';
            case "ASN":
                return 'N';
            case "ASP":
                return 'D';
            case "ASX":
                return 'B';
            case "CYS":
                return 'C';
            case "GLU":
                return 'E';
            case "GLN":
                return 'Q';
            case "GLX":
                return 'Z';
            case "GLY":
                return 'G';
            case "HIS":
                return 'H';
            case "ILE":
                return 'I';
            case "LEU":
                return 'L';
            case "LYS":
                return 'K';
            case "MET":
                return 'M';
            case "PHE":
                return 'F';
            case "PRO":
                return 'P';
            case "SER":
                return 'S';
            case "THR":
                return 'T';
            case "TRP":
                return 'W';
            case "TYR":
                return 'Y';
            case "VAL":
                return 'V';
            default:
                System.err.println(residue + " does not exist.");
                System.exit(1);
                return null;
        }
    }

    /**
     * decodes numerial value describing the class of the helix
     *
     * @param helixClass numerical description of helix class
     * @return human readable description of helix class
     */
    public static String translateHelixClass(Integer helixClass) {
        switch (helixClass) {
            case 1:
                return "Right-handed alpha";
            case 2:
                return "Right-handed omega";
            case 3:
                return "Right-handed pi";
            case 4:
                return "Right-handed gamma";
            case 5:
                return "Right-handed 3 - 10";
            case 6:
                return "Left-handed alpha";
            case 7:
                return "Left-handed omega";
            case 8:
                return "Left-handed gamma";
            case 9:
                return "Left-handed gamma";
            case 10:
                return "Polyproline";
            default:
                System.err.println(helixClass + " does not exist.");
                System.exit(1);
                return null;
        }
    }

}
