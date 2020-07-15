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
    public int getNumberOfAlignments();    
    public LCAHit getAlignment(int n);
    public void printEntry();
    public boolean hasUnknownTaxa();
}
