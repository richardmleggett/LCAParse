/*
 * Program: LCAParse
 * Author:  Richard M. Leggett
 * 
 * Copyright 2020 Earlham Institute
 */

package leggett.lcaparse;

import java.util.ArrayList;

public class SAMHitSet implements LCAHitSet{    
    private String queryName;
    private ArrayList<LCAHit> alignments = new ArrayList<LCAHit>();
    private int unknownTaxa = 0;
    
    public SAMHitSet(String query) {
        queryName = query;
    }

    public void addAlignment(LCAHit ph) {        
        if (alignments.size() > 0) {
            System.out.println("Error: current version of LCAParse only supports SAM files with one hit per query.");
        } else {
            alignments.add(ph);
            if (ph.getTaxonId() == -1) {
                unknownTaxa++;
            }
        }        
    }
    
    public boolean hasUnknownTaxa() {
        return unknownTaxa == 0 ? false:true;
    }
    
    public int getNumberOfAlignments() {
        return alignments.size();
    }
    
    public LCAHit getAlignment(int n) {
        LCAHit ph = null;
        
        if (n < alignments.size()) {
            ph = alignments.get(n);
        }
        
        return ph;
    }
    
    public void printEntry() {
        for (int i=0; i<alignments.size(); i++) {
            System.out.println(queryName +
                               "\t" + alignments.get(i).getTargetName() +
                               "\t" + alignments.get(i).getTaxonId() +
                               "\t" + alignments.get(i).getQueryCover() + 
                               "\t" + alignments.get(i).getIdentity());
        }
    }
}
