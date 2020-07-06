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
    private final static int FORMAT_UNKNOWN = 0;
    private final static int FORMAT_PAF = 1;
    private final static int FORMAT_NANOOK = 2;
    private final static int FORMAT_BLASTTAB = 3;
    private String inputFilename = null;
    private String outputFilename = null;
    private String taxonomyDirectory = null;
    private String mapFilename = null;
    private int fileFormat = 0;
    private String version="v0.1";
    
    public void parseFile() {
        Taxonomy taxonomy = new Taxonomy(taxonomyDirectory + "/nodes.dmp", taxonomyDirectory + "/names.dmp");  
        //Taxonomy taxonomy = new Taxonomy("/Users/leggettr/Documents/Databases/taxonomy_27Mar20/nodes.dmp", "/Users/leggettr/Documents/Databases/taxonomy_27Mar20/names.dmp");  
        AccessionTaxonConvertor atc = new AccessionTaxonConvertor();
        //atc.readMapFile("/Users/leggettr/Desktop/PAFparse/bacteriamap.txt", true);
        atc.readMapFile(mapFilename, true);
        PAFFileParser pfp = new PAFFileParser(taxonomy, atc);
        //AccessionMapFilter amf = new AccessionMapFilter(taxonomy);
        //amf.filterMapFile("/Users/leggettr/Documents/Databases/taxonomy_27Mar20/accession2taxid/nucl_gb.accession2taxid", "bacteriamap.txt", "virusesmap.txt");
        
        
        System.out.println("Parsing...");
        //pfp.parseFile("/Users/leggettr/Desktop/PAFparse/output_sorted.paf");
        //pfp.writeResults("/Users/leggettr/Desktop/PAFparse/results.txt");
        pfp.parseFile(inputFilename);
        pfp.writeResults(outputFilename);
        System.out.println("Done");
        pfp.printResults(atc);
    }
    
    public void displayHelp() {
        System.out.println("");
        System.out.println("LCAParse "+version);
        System.out.println("Comments/queries: richard.leggett@earlham.ac.uk");
        System.out.println("");
        System.out.println("To parse:");
        System.out.println("    lcaparse [-input <filename>|-inputlist <filename>] -output <filename> -taxonomy <directory> -mapfile <filename> -format <string>");
        System.out.println("Where:");
        System.out.println("    -input specifies the name of an input file to parse");
        System.out.println("    -inputlist specifies a file of filenames of input files to parse");
        System.out.println("    -output specifies the output filename");
        System.out.println("    -taxonomy specifies the directory containing NCBI taxonomy files");
        System.out.println("              (files needed are nodes.dmp and names.dmp)");
        System.out.println("    -mapfile specifies the location of an accession to taxon ID mapping file");
        System.out.println("    -format specifies input file format - either 'nanook', 'blasttab' or 'PAF'");
        System.out.println("");
        System.out.println("To create mapping file:");
        System.out.println("    lcaparse -makemap -input <filename>");
        System.out.println("");
        System.out.println("To do:");
        System.out.println("    - Implement makemap option");
        System.out.println("    - Implement blasttab option");
        System.out.println("");
    }
    
    public void processCommandLine(String[] args) {
        int i = 0;
        
        while (i < (args.length)) {
            if (args[i].equalsIgnoreCase("-help")) {
                displayHelp();
                System.exit(0);
            } else if (args[i].equalsIgnoreCase("-input")) {
                inputFilename = args[i+1];
                i+=2;
            } else if (args[i].equalsIgnoreCase("-inputlist")) {
                System.out.println("Inputlist not yet implemented");
                System.exit(1);
            } else if (args[i].equalsIgnoreCase("-output")) {
                outputFilename = args[i+1];
                i+=2;
            } else if (args[i].equalsIgnoreCase("-taxonomy")) {
                taxonomyDirectory = args[i+1];
                i+=2;
            } else if (args[i].equalsIgnoreCase("-mapfile")) {
                mapFilename = args[i+1];
                i+=2;
            } else if (args[1].equalsIgnoreCase("-format")) {
                if (args[i+1].equalsIgnoreCase("nanook")) {
                    fileFormat = FORMAT_NANOOK;
                } else if (args[i+1].equalsIgnoreCase("blasttab")) {
                    fileFormat = FORMAT_BLASTTAB;
                } else if (args[i+1].equalsIgnoreCase("paf")) {
                    fileFormat = FORMAT_PAF;
                } else {
                    System.out.println("Unknown file format: "+args[i+1]);
                    System.exit(1);
                }
                i+=2;
            } else {                
                System.out.println("Unknown parameter: " + args[i]);
                System.exit(1);
            }           
        }
        
        if (args.length == 0) {
            displayHelp();
            System.exit(0);
        }
        
        if (inputFilename == null) {
            System.out.println("Error: you must specify a -input parameter");
            System.exit(1);
        }
        if (outputFilename == null) {
            System.out.println("Error: you must specify a -output parameter");
            System.exit(1);
        }
        if (taxonomyDirectory == null) {
            System.out.println("Error: you must specify a -taxonomy parameter");
            System.exit(1);
        }
        if (mapFilename == null) {
            System.out.println("Error: you must specify a -mapfile parameter");
            System.exit(1);
        }        
     }

    public static void main(String[] args) {
        LCAParse pp = new LCAParse();
        pp.processCommandLine(args);
        pp.parseFile();
    }
}
