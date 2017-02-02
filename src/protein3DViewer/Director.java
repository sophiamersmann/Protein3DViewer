package protein3DViewer;

import protein3DViewer.model.*;

import java.util.List;
import java.util.Map;

/**
 * Created by sophiamersmann on 19/01/2017.
 */
public class Director {

    public Director(PDBParser p, Protein protein) {

        protein.setPath(p.pdbFile.toPath());
        protein.setId(p.idCode);
        protein.setName(p.title);

        // set 3D model of the protein
        Model model = new Model(1);
        model.setProtein(protein);
        for (Integer atomID : p.atomNames.keySet()) {
            Character chainID = p.atomChainNames.get(atomID);
            if (!model.getChains().containsKey(chainID)) {
                model.setChain(chainID, new Chain(chainID));
            }
            Integer residueID = p.atomResNums.get(atomID);
            if (!model.getChain(chainID).getResidues().containsKey(residueID)) {
                String residueName3 = p.atomResNames.get(atomID);
                Residue residue = new Residue(residueID, residueName3);
                residue.setChain(model.getChain(chainID));
                model.getChain(chainID).setResidue(residueID, residue);
            }
            Atom atom = new Atom(atomID, p.atomNames.get(atomID), p.atomElements.get(atomID));
            atom.setX(p.atomCoordinates.get(atomID).getX());
            atom.setY(p.atomCoordinates.get(atomID).getY());
            atom.setZ(p.atomCoordinates.get(atomID).getZ());
            atom.setResidue(model.getChain(chainID).getResidue(residueID));
            model.getChain(chainID).getResidue(residueID).setAtom(atomID, atom);
        }
        protein.setModel(model);

        // set primary sequence of the protein
        SeqResRecord seqResRecord = new SeqResRecord();
        seqResRecord.setProtein(protein);
        for (Map.Entry<Character, List<String>> entry : p.seqResRecord.entrySet()) {
            Chain seqResChain = new Chain(entry.getKey());
            Chain modelChain = protein.getModel().getChain(entry.getKey());
            for (int i = 0; i < entry.getValue().size(); i++) {
                Integer residueID = i + 1;
                seqResChain.setResidue(residueID, findModelResidue(modelChain, residueID, entry.getValue().get(i)));
            }
            seqResRecord.setChain(entry.getKey(), seqResChain);
        }
        protein.setSeqResRecord(seqResRecord);

        // set secondary structure of the protein
        SecondaryStructure secondaryStructure = new SecondaryStructure();
        secondaryStructure.setProtein(protein);
        protein.setSecondaryStructure(secondaryStructure);
        for (String helixId : p.helixChainNames.keySet()) {
            Helix helix = new Helix(helixId);
            helix.setChainName(p.helixChainNames.get(helixId));
            Chain chain = protein.getModel().getChain(p.helixChainNames.get(helixId));
            helix.setInitResidue(findModelResidue(chain, p.helixInitResNums.get(helixId), p.helixInitResNames.get(helixId)));
            helix.setEndResidue(findModelResidue(chain, p.helixEndResNums.get(helixId), p.helixEndResNames.get(helixId)));
            helix.setType(Tools.translateHelixClass(p.helixClasses.get(helixId)));
            protein.getSecondaryStructure().setHelix(helixId, helix);
        }

        for (String sheetId : p.strandChainNames.keySet()) {
            Sheet sheet = new Sheet(sheetId);
            for (Integer strandId : p.strandChainNames.get(sheetId).keySet()) {
                Strand strand = new Strand(strandId);
                strand.setChainName(p.strandChainNames.get(sheetId).get(strandId));
                Chain chain = protein.getModel().getChain(p.strandChainNames.get(sheetId).get(strandId));
                strand.setInitResidue(findModelResidue(chain, p.strandInitResNums.get(sheetId).get(strandId), p.strandInitResNames.get(sheetId).get(strandId)));
                strand.setEndResidue(findModelResidue(chain, p.strandEndResNums.get(sheetId).get(strandId), p.strandEndResNames.get(sheetId).get(strandId)));
                strand.setSense(p.strandSenses.get(sheetId).get(strandId));
                sheet.getStrands().put(strandId, strand);
            }
            protein.getSecondaryStructure().setSheet(sheetId, sheet);
        }

        for (Chain chain : protein.getSeqResRecord().getChains().values()) {
            for (Residue residue : chain.getResidues().values()) {
                for (Helix helix : protein.getSecondaryStructure().getHelices().values()) {
                    residue.setInHelix(residue.isInHelix() || helix.hasResidue(residue));
                }
                for (Sheet sheet : protein.getSecondaryStructure().getSheets().values()) {
                    residue.setInSheet(residue.isInSheet() || sheet.hasResidue(residue));
                }
            }
        }


    }

    private static Residue findModelResidue(Chain chain, Integer resNum, String resName3) {
        if (!chain.getResidues().containsKey(resNum)) {
            Residue residue = new Residue(resNum, resName3);
            residue.setChain(chain);
            return residue;
        }
        return chain.getResidues().get(resNum);
    }

}
