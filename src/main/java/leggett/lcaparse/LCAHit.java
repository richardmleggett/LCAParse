/*
 * Program: LCAParse
 * Author:  Richard M. Leggett
 * 
 * Copyright 2020 Earlham Institute
 */

package leggett.lcaparse;

import java.util.ArrayList;

public interface LCAHit {    
    public String getQueryName();    
    public String getTargetName();    
    public double getQueryCover();    
    public double getIdentity();    
    public double getAlignmentScore();    
    public void setTaxonIdPath(ArrayList<Long> path);
    public long getTaxonId();
    public int getTaxonLevel();
    public long getTaxonNode(int level);
}
