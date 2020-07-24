/*
 * Program: LCAParse
 * Author:  Richard M. Leggett
 * 
 * Copyright 2020 Earlham Institute
 */

package leggett.lcaparse;

import java.util.ArrayList;

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
    
    public BlastHit(Taxonomy t, AccessionTaxonConvertor atc, String line, int format) {
        String[] fields = line.split("\t");
                
        taxonomy = t;
        accTaxConvert = atc;
        
        if (format == LCAParseOptions.FORMAT_NANOOK) {
            parseNanoOK(fields);
        } else if ((format == LCAParseOptions.FORMAT_BLASTTAB) ||
                   (format == LCAParseOptions.FORMAT_BLASTTAXON)) {
            parseBlastTab(fields);
        }
                
        if (taxonId == -1) {            
            taxonomy.warnTaxa(targetName);
        } else {
            cacheTaxonIdPath();
        }        
    }
    
    private void parseNanoOK(String[] fields) {
        //"qseqid sseqid pident length mismatch gapopen qstart qend sstart send evalue bitscore stitle staxids"

        if (fields.length == 14) {        
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
            System.out.println("Error: input format doesn't seem to be NanoOK");
            System.exit(1);
        }
    }

    private void parseBlastTab(String[] fields) {
        // 1.	 qseqid	 query (e.g., unknown gene) sequence id
        // 2.	 sseqid	 subject (e.g., reference genome) sequence id
        // 3.	 pident	 percentage of identical matches
        // 4.	 length	 alignment length (sequence overlap)
        // 5.	 mismatch	 number of mismatches
        // 6.	 gapopen	 number of gap openings
        // 7.	 qstart	 start of alignment in query
        // 8.	 qend	 end of alignment in query
        // 9.	 sstart	 start of alignment in subject
        // 10.	 send	 end of alignment in subject
        // 11.	 evalue	 expect value
        // 12.	 bitscore	 bit score

        if (fields.length >= 12) {        
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
            
            if (fields.length == 13) {
                String taxaString = fields[12];
                String[] taxa = taxaString.split(";");
                taxonId = Integer.parseInt(taxa[0]);
            } else {
                if (accTaxConvert != null) {
                    taxonId = accTaxConvert.getTaxonFromAccession(targetName);       
                } else {
                    System.out.println("Error: you haven't specified a mapfile and the input file doesn't have taxon IDs");
                    System.exit(1);                
                }
            }
        } else {
            System.out.println("Error: input format doesn't seem to be BlastTab");
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
        if (taxonIdPath != null) {
            if (level <= taxonIdPath.size()) {
                return taxonIdPath.get(taxonIdPath.size() - level);
            }
        }
        return 0;
    }    
    
    private void cacheTaxonIdPath() {
        if (taxonId != -1) {
            taxonIdPath = taxonomy.getTaxonIdPathFromId(taxonId);
        }
    }     
}
