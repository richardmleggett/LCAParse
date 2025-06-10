/*
 * Author: Richard M. Leggett
 * © Copyright 2021 Earlham Institute
 */

package uk.ac.earlham.lcaparse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import uk.ac.earlham.marti.core.MARTiLog;

/**
 * Main class for command line utility.
 * 
 * @author Richard M. Leggett
 */
public class LCAParse {    
    public void parseFile(LCAParseOptions options) {
        AccessionTaxonConvertor atc = null;
        MARTiLog logFile = new MARTiLog();
        
        System.out.println("");
        System.out.println("LCAParse "+LCAParseOptions.version);
        System.out.println("");        

        Taxonomy taxonomy = new Taxonomy(null, options, options.getTaxonomyDirectory() + "/nodes.dmp", options.getTaxonomyDirectory() + "/names.dmp");  
        options.displayMemory();
        
        if (options.requiresAccessionMapping()) {
            atc = new AccessionTaxonConvertor();
            if (options.getMapFilename() != null) {
                atc.readMapFile(options.getMapFilename(), true);
            }
        }
        
        options.displayMemory();
        LCAFileParser pfp = new LCAFileParser(taxonomy, options, atc, false, logFile);        
        options.displayOptions();
                
        System.out.println("Parsing...");
        pfp.parseFile(options.getInputFilename());
        
        System.out.println("Removing poor alignments");
        ArrayList<String> queriesToRemove = pfp.removePoorAlignments();
        int readsRemoved = queriesToRemove.size();
        System.out.println("Removed "+readsRemoved+" reads");        
        
        pfp.findAncestorAndWriteResults(options.getTaxaSummaryOutputFilename(), options.getPerReadOutputFilename());
        System.out.println("");
        System.out.println("Done");
        System.out.println("");
        //pfp.printResults(atc);
        
        //Taxonomy taxonomy = new Taxonomy("/Users/leggettr/Documents/Databases/taxonomy_27Mar20/nodes.dmp", "/Users/leggettr/Documents/Databases/taxonomy_27Mar20/names.dmp");  
        //atc.readMapFile("/Users/leggettr/Desktop/PAFparse/bacteriamap.txt", true);
        //AccessionMapFilter amf = new AccessionMapFilter(taxonomy);
        //amf.filterMapFile("/Users/leggettr/Documents/Databases/taxonomy_27Mar20/accession2taxid/nucl_gb.accession2taxid", "bacteriamap.txt", "virusesmap.txt");
        //pfp.parseFile("/Users/leggettr/Desktop/PAFparse/output_sorted.paf");
        //pfp.findAncestorAndWriteResults("/Users/leggettr/Desktop/PAFparse/results.txt");        
    }
    
    public void makeMapFile(LCAParseOptions options) {
        Taxonomy taxonomy = new Taxonomy(null, options, options.getTaxonomyDirectory() + "/nodes.dmp", options.getTaxonomyDirectory() + "/names.dmp");  
        AccessionMapFilter amf = new AccessionMapFilter(taxonomy);
        amf.filterMapFile(options.getInputFilename(),options.getOutputPrefix());
    }
    
    public void discernTaxonomy(LCAParseOptions options) {
        Taxonomy taxonomy = new Taxonomy(null, options, options.getTaxonomyDirectory() + "/nodes.dmp", options.getTaxonomyDirectory() + "/names.dmp");  
        options.displayMemory();
        taxonomy.discernRanks();
    }
    
    public void annotateBlast(LCAParseOptions options) {
        AccessionTaxonConvertor atc = new AccessionTaxonConvertor();
        atc.readMapFile(options.getMapFilename(), true);
        BufferedReader br;
        PrintWriter pw;

        try {
            br = new BufferedReader(new FileReader(options.getInputFilename()));
            pw = new PrintWriter(new FileWriter(options.getInputFilename() + ".annotated"));
            String line;

            do {
                line = br.readLine();
                if (line != null) {
                    String[] fields = line.split("\t");
                    String accession=fields[4];
                    Long taxon = atc.getTaxonFromAccession(accession);
                    pw.print(line);
                    pw.println("\t"+taxon);
                }
            } while (line != null);
            br.close();
            pw.close();
            
            //printEquals();
        } catch (Exception e) {
            System.out.println("annotateBlast Exception:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        LCAParseOptions lpo = new LCAParseOptions();
        lpo.processCommandLine(args);

        if (lpo.doingRanks()) {
            LCAParse pp = new LCAParse();
            pp.discernTaxonomy(lpo);
        } else if (lpo.doingMakeMap()) {
            LCAParse pp = new LCAParse();
            pp.makeMapFile(lpo);
        } else if (lpo.doingAnnotate()) {
            LCAParse pp = new LCAParse();
            pp.annotateBlast(lpo);
        } else {
            LCAParse pp = new LCAParse();
            pp.parseFile(lpo);
        }
    }
}
