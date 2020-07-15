/*
 * Program: LCAParse
 * Author:  Richard M. Leggett
 * 
 * Copyright 2020 Earlham Institute
 */

package leggett.lcaparse;

import java.util.ArrayList;

public class BlastHitSet implements LCAHitSet{    
    private String queryName;
    private ArrayList<LCAHit> alignments = new ArrayList<LCAHit>();
    private double bestBitScore = 0;
    private double bitScoreThreshold = 0;
    private double bestQueryCover = 0;
    private double bestIdentity = 0;
    private double bestEValue = 0;
    private LCAParseOptions options;
    
    public BlastHitSet(String query, LCAParseOptions o) {
        queryName = query;
        options = o;
        
    }

    public void addAlignment(LCAHit hit) {
        BlastHit bh = (BlastHit)hit;
        boolean addAlignment = false;
        boolean updateBest = false;
        
        if (alignments.size() > 0) {
            // Is this better than our best?
            boolean foundNewBest = false;

            if (bh.getAlignmentScore() > bestBitScore) {
                updateBest = true;
            } else if (bh.getAlignmentScore() == bestBitScore) {
                if (bh.getEValue() < bestEValue) {
                    updateBest = true;
                }
            }
            
            if (bh.getAlignmentScore() >= bitScoreThreshold) {
                addAlignment = true;
            }
        } else {
            addAlignment = true;
            updateBest = true;
        }
        
        if (addAlignment) {
            alignments.add(bh);
        }
        
        if (updateBest) {
            bestBitScore = bh.getAlignmentScore();
            bitScoreThreshold = (options.getScorePercent() * bestBitScore) / 100;
            bestIdentity = bh.getIdentity();
            bestEValue = bh.getEValue();
        }
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
    
     public boolean hasUnknownTaxa() {
         return false;
     }
}
