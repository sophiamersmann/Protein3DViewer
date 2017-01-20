package protein3DViewer;

import protein3DViewer.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Director {

    public Director(PDBParser p, Protein protein) {

        protein.setPath(p.pdbFile.toPath());
        protein.setId(p.idCode);
        protein.setName(p.title);

        SeqResRecord seqResRecord = new SeqResRecord();
        for (Map.Entry<Character, List<String>> entry : p.seqResRecord.entrySet()) {
            SeqResChain seqResChain = new SeqResChain(entry.getKey());
            seqResChain.setSequence(sequence3to1(entry.getValue()));
            seqResRecord.getChains().add(seqResChain);
        }
        protein.setSeqResRecord(seqResRecord);

        for (String helixId : p.helixChainNames.keySet()) {
            Helix helix = new Helix(helixId);
            helix.setChainName(p.helixChainNames.get(helixId));
            helix.setInitResidue(new Residue(
                    p.helixInitResNums.get(helixId),
                    aminoAcid3to1(p.helixInitResNames.get(helixId)),
                    p.helixInitResNames.get(helixId)
            ));
            helix.setEndResidue(new Residue(
                    p.helixEndResNums.get(helixId),
                    aminoAcid3to1(p.helixEndResNames.get(helixId)),
                    p.helixEndResNames.get(helixId)
            ));
            helix.setType(translateHelixClass(p.helixClasses.get(helixId)));
            protein.getSecondaryStructures().add(helix);
        }

        for (String sheetId : p.strandChainNames.keySet()) {
            Sheet sheet = new Sheet(sheetId);
            for (Integer strandId : p.strandChainNames.get(sheetId).keySet()) {
                Strand strand = new Strand(strandId);
                strand.setInitResidue(new Residue(
                        p.strandInitResNums.get(sheetId).get(strandId),
                        aminoAcid3to1(p.strandInitResNames.get(sheetId).get(strandId)),
                        p.strandInitResNames.get(sheetId).get(strandId)
                ));
                strand.setEndResidue(new Residue(
                        p.strandEndResNums.get(sheetId).get(strandId),
                        aminoAcid3to1(p.strandEndResNames.get(sheetId).get(strandId)),
                        p.strandEndResNames.get(sheetId).get(strandId)
                ));
                strand.setSense(p.strandSenses.get(sheetId).get(strandId));
                sheet.getStrands().add(strand);
            }
            protein.getSecondaryStructures().add(sheet);
        }

        protein.setModel(new Model(1));
        Set<Character> chainNames = new HashSet<>(p.atomChainNames.values());
        for (Character chainName : chainNames) {
            protein.getModel().getChains().put(chainName, new Chain(chainName));
        }

        Set<Integer> resNums = new HashSet<>(p.atomResNums.values());
    }


    /**
     * translates amino acid sequence in three letter code to simple one letter code sequence
     *
     * @param residues list of amino acids in three letter code
     * @return amino acid sequence in one letter code
     */
    private static String sequence3to1(List<String> residues) {
        String sequence = "";
        for (String res : residues) {
            sequence += aminoAcid3to1(res);
        }
        return sequence;
    }

    /**
     * translates amino acid three letter code to one letter code
     *
     * @param residue amino acid in three letter code
     * @return amino acid in one letter code
     */
    private static Character aminoAcid3to1(String residue) {
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
    private static String translateHelixClass(Integer helixClass) {
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
