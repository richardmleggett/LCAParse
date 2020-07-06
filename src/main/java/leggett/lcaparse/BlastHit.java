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
public class BlastHit implements LCAHit {
    private Taxonomy taxonomy;
    private AccessionTaxonConvertor accTaxConvert;
    private String queryName;
    private int queryLength;
    private int queryStart;
    private int queryEnd;
    private String targetName;
    private int targetStart;
    private int targetEnd;
    private int matches;
    private int length;
    private int mismatches;
    private double bitscore;
    private double identity;
    private double eValue;
    private long taxonId = -1;
    private ArrayList<Long> taxonIdPath;
    
    public BlastHit(Taxonomy t, AccessionTaxonConvertor atc, String line, boolean isNanoOK) {
        String[] fields = line.split("\t");
        
        if (!isNanoOK) {
            System.out.println("Haven't yet implemented plain BlastTab, just NanoOK format");
            System.exit(1);
        }
        
        taxonomy = t;
        accTaxConvert = atc;
        
        
        //"qseqid sseqid pident length mismatch gapopen qstart qend sstart send evalue bitscore stitle staxids"
        
        if (fields.length >= 14) {        
            queryName = fields[0];
            targetName = fields[1];
            identity = Double.parseDouble(fields[2]);
            length = Integer.parseInt(fields[3]);
            mismatches = Integer.parseInt(fields[4]);
            // gapopen            
            queryStart = Integer.parseInt(fields[6]);
            queryEnd = Integer.parseInt(fields[7]);
            targetStart = Integer.parseInt(fields[8]);
            targetEnd = Integer.parseInt(fields[9]);
            eValue = Double.parseDouble(fields[10]);
            bitscore = Double.parseDouble(fields[11]);
            // title
            String taxaString = fields[13];
            String[] taxa = taxaString.split(";");
            taxonId = Integer.parseInt(taxa[0]);
        } else {
            System.out.println("Couldn't split "+line);
            System.exit(1);
        }        
    }
    
    public String getQueryName() {
        return queryName;
    }
    
    public String getTargetName() {
        return targetName;
    }
    
    public double getQueryCover() {
        return 0;
    }
    
    public double getIdentity() {
        return identity;
    }
    
    public double getAlignmentScore() {
        return bitscore;
    }
    
    public double getEValue() {
        return eValue;
    }
    
    public void setTaxonIdPath(ArrayList<Long> path) {
        taxonIdPath = path;
    }

    public long getTaxonId() {
        return taxonId;
    }
            
    public int getTaxonLevel() {
        if (taxonIdPath != null) {
            return taxonIdPath.size(); // 1-offset
        }
        return 0;
    }    
        
    // Note level is 1-offset
    public long getTaxonNode(int level) {
        if (level <= taxonIdPath.size()) {
            return taxonIdPath.get(taxonIdPath.size() - level);
        }
        return 0;
    }    
}
