/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leggett.lcaparse;

import java.util.ArrayList;

/**
 *
 * @author leggettr
 */
public interface LCAHitSet {    
    public void addAlignment(LCAHit ph);    
    public int getNumberOfAlignments();    
    public LCAHit getAlignment(int n);
    public void printEntry();
    public boolean hasUnknownTaxa();
}
