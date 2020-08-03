/*
 * Program: LCAParse
 * Author:  Richard M. Leggett
 * 
 * Copyright 2020 Earlham Institute
 */

package leggett.lcaparse;

public class LCAParse {    
    public void parseFile(LCAParseOptions options) {
        AccessionTaxonConvertor atc = null;
        
        System.out.println("");
        System.out.println("LCAParse "+LCAParseOptions.version);
        System.out.println("");        
 
        Taxonomy taxonomy = new Taxonomy(options, options.getTaxonomyDirectory() + "/nodes.dmp", options.getTaxonomyDirectory() + "/names.dmp");  
        options.displayMemory();
        
        if (options.requiresAccessionMapping()) {
            atc = new AccessionTaxonConvertor();
            atc.readMapFile(options.getMapFilename(), true);
        }
        
        options.displayMemory();
        LCAFileParser pfp = new LCAFileParser(taxonomy, options, atc);        
                
        System.out.println("Parsing...");
        pfp.parseFile(options.getInputFilename());
        pfp.writeResults(options.getTaxaSummaryOutputFilename(), options.getPerReadOutputFilename());
        System.out.println("");
        System.out.println("Done");
        System.out.println("");
        //pfp.printResults(atc);
        
        //Taxonomy taxonomy = new Taxonomy("/Users/leggettr/Documents/Databases/taxonomy_27Mar20/nodes.dmp", "/Users/leggettr/Documents/Databases/taxonomy_27Mar20/names.dmp");  
        //atc.readMapFile("/Users/leggettr/Desktop/PAFparse/bacteriamap.txt", true);
        //AccessionMapFilter amf = new AccessionMapFilter(taxonomy);
        //amf.filterMapFile("/Users/leggettr/Documents/Databases/taxonomy_27Mar20/accession2taxid/nucl_gb.accession2taxid", "bacteriamap.txt", "virusesmap.txt");
        //pfp.parseFile("/Users/leggettr/Desktop/PAFparse/output_sorted.paf");
        //pfp.writeResults("/Users/leggettr/Desktop/PAFparse/results.txt");        
    }
    
    public void makeMapFile(LCAParseOptions options) {
        Taxonomy taxonomy = new Taxonomy(options, options.getTaxonomyDirectory() + "/nodes.dmp", options.getTaxonomyDirectory() + "/names.dmp");  
        AccessionMapFilter amf = new AccessionMapFilter(taxonomy);
        amf.filterMapFile(options.getInputFilename(),options.getOutputPrefix());
    }

    public static void main(String[] args) {
        LCAParseOptions lpo = new LCAParseOptions();
        lpo.processCommandLine(args);

        if (lpo.doingMakeMap()) {
            LCAParse pp = new LCAParse();
            pp.makeMapFile(lpo);
        } else {
            LCAParse pp = new LCAParse();
            pp.parseFile(lpo);
        }
    }
}
