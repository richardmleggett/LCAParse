/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leggett.lcaparse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author leggettr
 */
public class PAFFileParser {

    private Hashtable<String, Integer> countPerTarget = new Hashtable<String, Integer>();
    private Hashtable<String, HitSet> hitsByQuery = new Hashtable<String, HitSet>();
    private Hashtable<Long, Integer> countsPerTaxon = new Hashtable<Long, Integer>(); 
    private Hashtable<String, Integer> equalsPerQuery = new Hashtable<String, Integer>();
    private Taxonomy taxonomy;
    private AccessionTaxonConvertor accTaxConvert;

    public PAFFileParser(Taxonomy t, AccessionTaxonConvertor atc) {
        taxonomy = t;
        accTaxConvert = atc;
    }
    
    private void logEqual(String queryName) {
        int count = 1;
        if (equalsPerQuery.contains(queryName)) {
            count = equalsPerQuery.get(queryName);
        }
        count++;
        equalsPerQuery.put(queryName, count);
    }

    public void parseFile(String filename) {
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(filename));
            String line;
            String lastQuery = "";
            int equal = 0;

            do {
                line = br.readLine();
                if (line != null) {
                    if (line.length() > 1) {
                        PAFHit ph = new PAFHit(taxonomy, accTaxConvert, line);
                        String queryName = ph.getQueryName();
                        HitSet hs;

                        boolean storeIt = false;
                        if (hitsByQuery.containsKey(queryName)) {
                            hs = hitsByQuery.get(queryName);
                        } else {
                            hs = new HitSet(queryName);
                        }
                        hs.addAlignment(ph);
                        
                        hitsByQuery.put(queryName, hs);
                            
//                            PAFHit currentBest = hitsByQuery.get(queryName);
//                            if (ph.getQueryCover() > currentBest.getQueryCover()) {
//                                storeIt = true;
//                            } else if (ph.getQueryCover() == currentBest.getQueryCover()) {
//                                if (ph.getIdentity() > currentBest.getIdentity()) {
//                                    storeIt = true;
//                                } else if (ph.getIdentity() == currentBest.getIdentity()) {
//                                    logEqual(queryName);
//                                }
//                            }
//                        } else {
//                            storeIt = true;
//                        }
//                        
//                        if (storeIt) {
//                            hitsByQuery.put(queryName, ph);
//                        }
                    }
                }
            } while (line != null);
            br.close();
            
            //printEquals();
        } catch (Exception e) {
            System.out.println("readProcessFile Exception:");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void writeResults(String filename) {
        int totalCount = 0;
        Set<String> keys = hitsByQuery.keySet();
        for (String queryName : keys) {
            //System.out.println("--------------------------------------------------------------------------------");
            HitSet hs = hitsByQuery.get(queryName);
            //hs.printEntry();
            long ancestor = taxonomy.findAncestor(hs, 1000);
            //System.out.println("Ancestor is "+ancestor+" "+taxonomy.getTaxonomyStringFromId(ancestor));

            int count = 0;
            if (countsPerTaxon.containsKey(ancestor)) {
                count = countsPerTaxon.get(ancestor);
            }
            count++;
            countsPerTaxon.put(ancestor, count);
            totalCount++;
        }

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename));
            Set<Long> allTaxon = countsPerTaxon.keySet();
            for (Long taxon : allTaxon) {
                pw.println(taxon + "\t" + countsPerTaxon.get(taxon));
                System.out.println(countsPerTaxon.get(taxon) + "\t" + taxon +"\t" + taxonomy.getTaxonomyStringFromId(taxon));
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("readProcessFile Exception:");
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Total count = " + totalCount);

    }
    
    private void printEquals() {
        int count = 0;
        Set<String> keys = equalsPerQuery.keySet();
        for (String queryName : keys) {
            System.out.println(queryName + " has " + equalsPerQuery.get(queryName));
            count++;
        } 
        System.out.println("Total equals count " + count);
    }
    
    public void printResults(AccessionTaxonConvertor atc) {
//        Set<String> keys = countPerTarget.keySet();
//        for (String targetName : keys) {
//            System.out.println(targetName + " " + countPerTarget.get(targetName));
//        }
        
       List<Map.Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(countPerTarget.entrySet());

       Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
         public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
             return entry1.getValue().compareTo( entry2.getValue() );
         }
       });

        for( Map.Entry<String, Integer> entry : list  ){
            System.out.println(entry.getKey() + " " + atc.getTaxonFromAccession(entry.getKey()) + " " + entry.getValue());
        }        
    }
}
