/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leggett.lcaparse;

/**
 *
 * @author leggettr
 */
public class LCAParse {    
    public void parseFile(LCAParseOptions options) {
        System.out.println("");
        System.out.println("LCAParse "+LCAParseOptions.version);
        System.out.println("");        
 
        Taxonomy taxonomy = new Taxonomy(options.getTaxonomyDirectory() + "/nodes.dmp", options.getTaxonomyDirectory() + "/names.dmp");  
        options.displayMemory();
        AccessionTaxonConvertor atc = new AccessionTaxonConvertor();
        atc.readMapFile(options.getMapFilename(), true);
        options.displayMemory();
        LCAFileParser pfp = new LCAFileParser(taxonomy, options, atc);        
                
        System.out.println("Parsing...");
        pfp.parseFile(options.getInputFilename());
        pfp.writeResults(options.getOutputFilename());
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
    
 

    public static void main(String[] args) {
        LCAParseOptions lpo = new LCAParseOptions();
        LCAParse pp = new LCAParse();
        lpo.processCommandLine(args);
        pp.parseFile(lpo);
    }
}
