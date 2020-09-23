/*
 * Program: LCAParse
 * Author:  Richard M. Leggett
 * 
 * Copyright 2020 Earlham Institute
 */

package leggett.lcaparse;

import java.util.ArrayList;

public interface LCAHitSet {    
    public void addAlignment(LCAHit ph);    
    public String getQueryName();
    public int getNumberOfAlignments();    
    public LCAHit getAlignment(int n);
    public void printEntry();
    public boolean hasUnknownTaxa();
    public void setAssignedTaxon(long id);
    public long getAssignedTaxon();
    public boolean hasGoodAlignment();
}
