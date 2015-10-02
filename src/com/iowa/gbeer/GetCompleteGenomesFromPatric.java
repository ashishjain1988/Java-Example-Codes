package com.iowa.gbeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GetCompleteGenomesFromPatric {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("/home/jain/Gram_Positive_Bacteria_Study/Organisms_Lists_fromPATRIC/Firmicutes/GenomeFinder.txt"));
		String line = br.readLine();
		line = br.readLine();
		Map<String, String> genomeAccessionMap = new HashMap<String, String>();
		while(line != null)
		{
			String lineData[] = line.split("\t");
			String genomeName = lineData[1].trim();
			String genomeStatus = lineData[21].trim();
			String accession = lineData[19].trim();
			if(genomeStatus.trim().equals("complete") && !accession.trim().equals("-") && !accession.trim().equals(""))
			{
				genomeAccessionMap.put(accession, genomeName);
			}
			line = br.readLine();
		}
		br.close();
		PrintWriter pw = new PrintWriter("genomeAccessionMapfromPATRIC.txt");
		pw.println("Accession Number\tOrganism Name");
		Map<String, String> orgPathMap = new HashMap<String, String>();
		for(Entry<String, String> entry:genomeAccessionMap.entrySet())
		{
			String orgName = entry.getKey();
			orgPathMap.put(orgName, null);
			pw.println(orgName+"\t"+entry.getValue());
		}
		pw.close();
		File dir = new File("/home/jain/Bacterial_Genomic_Data_NCBI/");
		File [] files = dir.listFiles();
		searchGenomeDir(files, orgPathMap,genomeAccessionMap);
		pw = new PrintWriter("PatricOrgsGenomesMap.txt");
		for(Entry<String, String> entry : orgPathMap.entrySet())
		{
			//System.out.println(entry.getKey()+"\t"+entry.getValue());
			pw.println(genomeAccessionMap.get(entry.getKey())+"\t"+entry.getValue());
		}
		pw.close();
	}
	
	public static void searchGenomeDir(File[] files,Map<String, String> orgPathMap,Map<String, String> genomeAccessionMap)
	{
		for(int i=0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				File[] genomeFiles = files[i].listFiles();
				for(int j=0;j<genomeFiles.length;j++)
				{
					String dirName = genomeFiles[j].getName();
					for(String accessionNumbers:genomeAccessionMap.keySet())
					{
						//System.out.println(accessionNumbers);
						/*if(accessionNumbers.split(",")[0].contains("."))
						{
							System.out.println(accessionNumbers.split(",")[0].split("\\.").length);
						}*/
						String accessionNumber = (accessionNumbers.split(",")[0].contains(".")?accessionNumbers.split(",")[0].split("\\.")[0]:accessionNumbers.split(",")[0]); 
						if(dirName.contains(accessionNumber))
						{
							/*if(accessionNumber.equals("NC_012034"))
							{
								System.out.println("It is there");
								System.out.println(files[i].getPath());
							}*/
							orgPathMap.put(accessionNumbers,files[i].getPath());
							break;
						}
					}
				}
			}
		}
	}
	
}
