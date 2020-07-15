/*
 * Program: LCAParse
 * Author:  Richard M. Leggett
 * 
 * Copyright 2020 Earlham Institute
 */

package leggett.lcaparse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class AccessionMapFilter {
    private Taxonomy taxonomy;
    
    public AccessionMapFilter(Taxonomy t) {
        taxonomy = t;
    }
    
    public void filterMapFile(String mapFilename, String bacteriaOut, String virusesOut) {
        BufferedReader br;
        String line;
        int count = 0;
        try {
            System.out.println("Reading "+mapFilename);
            br = new BufferedReader(new FileReader(mapFilename));
            PrintWriter pwBacteria = new PrintWriter(new FileWriter(bacteriaOut)); 
            PrintWriter pwViruses = new PrintWriter(new FileWriter(virusesOut)); 

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                String accession = fields[0];
                long taxonId = Long.parseLong(fields[2]);
                //long gi = Long.parseLong(fields[3]);
                //System.out.print(accession);
                
                if (taxonId == 0) {
                    System.out.println("WARNING: Taxon 0 specified ("+line+")");
                } else {
                    if (taxonomy.isTaxonAncestor(taxonId, 2)) {
                        pwBacteria.println(accession + "\t" + taxonId);
                    } else if (taxonomy.isTaxonAncestor(taxonId, 10239)) {
                        pwViruses.println(accession + "\t" + taxonId);
                    }
                }
                
                count++;
                if (count % 1000000 == 0) {
                    System.out.println("    Read "+count+" entries");
                }                
            }
            System.out.println("Finished");
            pwBacteria.close();
            pwViruses.close();
            br.close();            
        } catch (Exception e) {
            System.out.println("AccessionTaxonConvertor exception");
            e.printStackTrace();
            System.exit(1);
        }            

    }
}
