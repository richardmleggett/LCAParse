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
public class PAFHit {
    private Taxonomy taxonomy;
    private AccessionTaxonConvertor accTaxConvert;
    private String queryName;
    private int queryLength;
    private int queryStart;
    private int queryEnd;
    private String strand;
    private String targetName;
    private int targetLength;
    private int targetStart;
    private int targetEnd;
    private int matches;
    private int basesInMapping;
    private int quality;
    private int mismatchesAndGaps = 0;
    private double identity;
    private double queryCover = 0;    
    private int alignmentScore;
    private long taxonId = -1;
    private ArrayList<Long> taxonIdPath;
    
    public PAFHit(Taxonomy t, AccessionTaxonConvertor atc, String line) {
        String[] fields = line.split("\t");
        
        taxonomy = t;
        accTaxConvert = atc;
        
        if (fields.length >= 12) {        
            queryName = fields[0];
            queryLength = Integer.parseInt(fields[1]);
            queryStart = Integer.parseInt(fields[2]);
            queryEnd = Integer.parseInt(fields[3]);
            strand = fields[4];
            targetName = fields[5];
            targetLength = Integer.parseInt(fields[6]);
            targetStart = Integer.parseInt(fields[7]);
            targetEnd = Integer.parseInt(fields[8]);
            matches = Integer.parseInt(fields[9]);
            basesInMapping = Integer.parseInt(fields[10]);
            quality = Integer.parseInt(fields[11]);
            identity = (double)((double)matches / (double)basesInMapping);
            
            if (fields[12].startsWith("NM:i:")) {
                mismatchesAndGaps = Integer.parseInt(fields[12].substring(5));
                queryCover = (double)((double)(basesInMapping - mismatchesAndGaps) / (double)queryLength);
            } else {
                System.out.println("Warning: couldn't read NM field "+fields[12]);
            }

            if (fields[14].startsWith("AS:i:")) {
                alignmentScore = Integer.parseInt(fields[14].substring(5));
            } else {
                System.out.println("Warning: couldn't read AS field "+fields[12]);
            }
            
            //System.out.println(queryName + " " +targetName + " " +queryCover);
        } else {
            System.out.println("Couldn't split "+line);
            System.exit(1);
        }
        
        taxonId = accTaxConvert.getTaxonFromAccession(targetName);
        if (taxonId == -1) {
            //System.out.println("Error: couldn't get ID "+targetName);
        } else {
            cacheTaxonIdPath();
        }
    }
    
    public String getQueryName() {
        return queryName;
    }
    
    public String getTargetName() {
        return targetName;
    }
    
    public double getQueryCover() {
        return queryCover;
    }
    
    public double getIdentity() {
        return identity;
    }
    
    public int getAlignmentScore() {
        return alignmentScore;
    }
    
    private void cacheTaxonIdPath() {
        if (taxonId != -1) {
            taxonIdPath = taxonomy.getTaxonIdPathFromId(taxonId);
        }
    }    
    
    public int getTaxonLevel() {
        if (taxonIdPath != null) {
            return taxonIdPath.size(); // 1-offset
        }
        return 0;
    }    
    
    public long getLeafNode() {
        return taxonId;
    }    
    
    // Note level is 1-offset
    public long getTaxonNode(int level) {
        if (level <= taxonIdPath.size()) {
            return taxonIdPath.get(taxonIdPath.size() - level);
        }
        return 0;
    }    
    
    public long getTaxonId() {
        return taxonId;
    }
    
}
