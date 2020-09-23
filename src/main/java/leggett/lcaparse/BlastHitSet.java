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
    private ArrayList<BlastHit> alignments = new ArrayList<BlastHit>();
    private double bestBitScore = 0;
    private double bitScoreThreshold = 0;
    private double bestQueryCover = 0;
    private double bestIdentity = 0;
    private double bestEValue = 0;
    private int bestLength = 0;
    private double bestQueryCoverage = 0;
    private LCAParseOptions options;
    private long assignedTaxon = -2;
    
    public BlastHitSet(String query, LCAParseOptions o) {
        queryName = query;
        options = o;
        
    }

    public void addAlignment(LCAHit hit) {
        BlastHit bh = (BlastHit)hit;
        boolean addAlignment = false;
        boolean updateBest = false;
        
        if (!hit.getQueryName().equals(queryName)) {
            System.out.println("Error in addAlignment: queryName doesn't match");
            System.exit(1);
        }
                
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
            bestLength = bh.getLength();
            bestQueryCoverage = bh.getQueryCover();
        }
    }
    
    public boolean hasGoodAlignment() {
        boolean goodAlignment = true;
        
        if (alignments.size() == 0) {
            goodAlignment = false;
        } else {        
            // Only accept alignment if long enough
            if (bestLength < options.getMinLength()) {
                goodAlignment = false;
            }

            // Only accept if high enough identity
            if (bestIdentity < options.getMinIdentity()) {
                goodAlignment = false;
            }

            // Only accept if query coverage high enough and combined high enough
            if (bestQueryCoverage != BlastHit.UNKNOWN) { 
                if (bestQueryCoverage < options.getMinQueryCoverage()) {
                    goodAlignment = false;
                }

                double combined = bestQueryCoverage + bestIdentity;
                if (combined < options.getMinCombinedScore()) {
                    goodAlignment = false;
                }
            }        
        }
        
        return goodAlignment;
    }
    
    public int getNumberOfAlignments() {
        return alignments.size();
    }
    
    public BlastHit getAlignment(int n) {
        BlastHit ph = null;
        
        if (n < alignments.size()) {
            ph = alignments.get(n);
        }
        
        return ph;
    }
    
    public String getQueryName() {
        return queryName;
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
     
     public void setAssignedTaxon(long id) {
         assignedTaxon = id;
     }
     
     public long getAssignedTaxon() {
         return assignedTaxon;
     }
}
