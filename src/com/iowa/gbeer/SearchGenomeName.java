package com.iowa.gbeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SearchGenomeName {

	public static void main(String[] args) throws IOException {
		Set<String> orgNames = new HashSet<String>();
		Map<String, String> orgPathMap = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader("/home/jain/Gram_Positive_Bacteria_Study/Reference_Orgs_List/GramPoistiveOrganismsList_60.txt"));
		String line = br.readLine();
		while(line!=null)
		{
			orgNames.add(line.trim());
			orgPathMap.put(line.trim(), null);
			line = br.readLine();
		}
		br.close();
		File dir = new File("/home/jain/Bacterial_Genomic_Data_NCBI/");
		File [] files = dir.listFiles();
		for(int i=0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				String dirName = files[i].getName();
				for(String org:orgNames)
				{
					if(dirName.contains(org))
					{
						orgPathMap.put(org,files[i].getPath());
					}
				}
			}
		}
		PrintWriter pw = new PrintWriter("orgs_Genomes_Map.txt");
		for(Entry<String, String> entry : orgPathMap.entrySet())
		{
			System.out.println(entry.getKey()+"\t"+entry.getValue());
			pw.println(entry.getKey()+"\t"+entry.getValue());
		}
		pw.close();
		
	}
}
