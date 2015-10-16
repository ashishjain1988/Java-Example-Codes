package com.iowa.gbeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class RemoveGenomesWithMoreThanOneChrom {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("/home/jain/Gram_Positive_Bacteria_Study/Organisms_Lists_from_PATRIC/Genomes_With_Morethan_1_Chrom.txt"));
		String basePath = "/home/jain/Bacterial_Genomic_Data_NCBI/";
		String line = br.readLine();
		Set<String> orgNames = new HashSet<>();
		while(line!=null)
		{
			orgNames.add(line.trim());
			line = br.readLine();
		}
		br.close();
		removePlasmidGenomicFiles(basePath,orgNames);
	}
	
	public static void removePlasmidGenomicFiles(String basePath,Set<String> orgs) throws IOException
	{
		File dir = new File(basePath);
		File [] files = dir.listFiles();
		int count = 0; 
		for(int i=0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				String name = files[i].getPath().split("/")[4];
				System.out.println(name);
				if(orgs.contains(name))
				{
					FileUtils.deleteDirectory(files[i]);
					count++;
				}
			}
		}
		System.out.println(count);
		
		
	}
}
