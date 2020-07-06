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
