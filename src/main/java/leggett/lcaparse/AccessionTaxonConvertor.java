/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leggett.lcaparse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;

/**
 *
 * @author leggettr
 */
public class AccessionTaxonConvertor {
    private Hashtable<String, Long> accessionToTaxon = new Hashtable();
    int count = 0;
    
    public AccessionTaxonConvertor() {  
    }
    
    public void readMapFile(String mapFilename, boolean twoColumn) {
        BufferedReader br;
        String line;
        try {
            System.out.println("Reading "+mapFilename);
            br = new BufferedReader(new FileReader(mapFilename));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                String accession;
                long taxonId;
                if (twoColumn) {
                    accession = fields[0];
                    taxonId = Long.parseLong(fields[1]);                
                } else {
                    accession = fields[0];
                    taxonId = Long.parseLong(fields[2]);
                }
                                
                //long gi = Long.parseLong(fields[3]);
                accessionToTaxon.put(accession, taxonId);
                count++;
                if (count % 1000000 == 0) {
                    System.out.println("    Read "+count+" entries");
                }
            }
            System.out.println("Finished");
            br.close();            
        } catch (Exception e) {
            System.out.println("AccessionTaxonConvertor exception");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public long getTaxonFromAccession(String accession) {
        long r = -1;
        
        if (accession.contains(".")) {
            accession = accession.substring(0, accession.indexOf('.'));
        }
        
        if (accessionToTaxon.containsKey(accession)) {
            r = accessionToTaxon.get(accession);
        }
        
        return r;
    }
    
}
