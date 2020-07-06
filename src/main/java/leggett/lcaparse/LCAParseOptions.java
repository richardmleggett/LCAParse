/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leggett.lcaparse;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author leggettr
 */
public class LCAParseOptions {
    public final static int FORMAT_UNKNOWN = 0;
    public final static int FORMAT_PAF = 1;
    public final static int FORMAT_NANOOK = 2;
    public final static int FORMAT_BLASTTAB = 3;
    private String inputFilename = null;
    private String outputFilename = null;
    private String taxonomyDirectory = null;
    private String mapFilename = null;
    private int fileFormat = 0;
    public final static String version="v0.1";
    private int maxHitsToConsider = 20;
    private double scorePercent = 90;
    private boolean limitToSpecies = false;
    private long expectedTaxon = 0;
    private long relatedTaxon = 0;
    private boolean doingMakeMap = false;
    
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
        System.out.println("    -maxhits specifies maximum number of hits to consider for given read (default 20)");
        System.out.println("    -scorepercent specifies minimum score threshold as percentage of top score for given read (default 90)");
        System.out.println("    -limitspecies limits taxonomy to species level (default: off)");
        System.out.println("Analysis options:");
        System.out.println("    -expected specifies the taxon ID of expected species");
        System.out.println("    -relative specifies the taxon ID of close relative species");
        System.out.println("");
        System.out.println("To create mapping file:");
        System.out.println("    lcaparse -makemap -input <filename>");
        System.out.println("Where:");
        System.out.println("    -input specifies the name of a nucl_gb.accession2taxid file");
        System.out.println("    -output specifes the prefix for output files");
        System.out.println("    -taxonomy specifies the directory containing NCBI taxonomy files");
        System.out.println("              (files needed are nodes.dmp and names.dmp)");
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
            } else if (args[i].equalsIgnoreCase("-expected")) {
                expectedTaxon = Long.parseLong(args[i+1]);
                i+=2;
            } else if (args[i].equalsIgnoreCase("-related")) {
                relatedTaxon = Long.parseLong(args[i+1]);
                i+=2;
            } else if (args[i].equalsIgnoreCase("-maxhits")) {
                maxHitsToConsider = Integer.parseInt(args[i+1]);
                i+=2;
            } else if (args[i].equalsIgnoreCase("-scorepercent")) {
                scorePercent = Double.parseDouble(args[i+1]);
                i+=2;
            } else if (args[i].equalsIgnoreCase("-limitspecies")) {
                limitToSpecies = true;
                i++;
            } else if (args[i].equalsIgnoreCase("-format")) {
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
            } else if (args[i].equalsIgnoreCase("-makemap")) {
                doingMakeMap = false;
                i++;
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
        
        if (doingMakeMap == false) {            
            if (mapFilename == null) {
                System.out.println("Error: you must specify a -mapfile parameter");
                System.exit(1);
            }        
        }
     }
    
    public String getInputFilename() {
        return inputFilename;
    }
    
    public String getOutputFilename() {
        return outputFilename;
    }
    
    public String getTaxonomyDirectory() {
        return taxonomyDirectory;
    }
    
    public String getMapFilename() {
        return mapFilename;
    }
    
    public int getFileFormat() {
        return fileFormat;
    }
    
    public int getMaxHitsToConsider() {
        return maxHitsToConsider;
    }
    
    public double getScorePercent() {
        return scorePercent;
    }
    
    public boolean limitToSpecies() {
        return limitToSpecies;
    }
    
    public String getTimeString() {
        GregorianCalendar timeNow = new GregorianCalendar();
        String s = String.format("%d/%d/%d %02d:%02d:%02d",
                                 timeNow.get(Calendar.DAY_OF_MONTH),
                                 timeNow.get(Calendar.MONTH)+1,
                                 timeNow.get(Calendar.YEAR),
                                 timeNow.get(Calendar.HOUR_OF_DAY),
                                 timeNow.get(Calendar.MINUTE),
                                 timeNow.get(Calendar.SECOND));
        return s;
    }
    
    public long getExpectedTaxon() {
        return expectedTaxon;
    }
    
    public long getRelatedTaxon() {
        return relatedTaxon;
    }
    
    public void displayMemory() {
        System.out.println("Total memory: "+ (Runtime.getRuntime().totalMemory() / (1024*1024)) + " Mb");
        System.out.println(" Free memory: "+ (Runtime.getRuntime().freeMemory() / (1024*1024)) + " Mb");
        System.out.println(" Used memory: "+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024)) + " Mb");
    }    
}
