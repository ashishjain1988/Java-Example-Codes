package com.iowa.gbeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class CopyGenomeDirectoryToNewFolder {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("/home/jain/Gram_Positive_Bacteria_Study/Organisms_Lists_from_PATRIC/Firmicutes/PatricOrgsGenomesMap.txt"));
		String basePath = "/home/jain/Gram_Positive_Bacteria_Study/Fericuites_Genomes_Path/";
		String line = br.readLine();
		Set<String> dirPaths = new HashSet<>();
		while(line!=null)
		{
			String lineData[] = line.split("\t");
			String folderPath = lineData[1].trim();
			if(!folderPath.equals("null"))
			{
				dirPaths.add(folderPath);
				String folderName = folderPath.split("/")[4];
				FileUtils.copyDirectory(new File(folderPath), new File(basePath+folderName));
			}
			line = br.readLine();
		}
		br.close();
		removePlasmidGenomicFiles(basePath);
	}
	
	public static void removePlasmidGenomicFiles(String basePath)
	{
		File dir = new File(basePath);
		File [] files = dir.listFiles();
		for(int i=0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				File [] genomefiles = files[i].listFiles();
				if(genomefiles.length > 1)
				{
					long longest = 0;
					String longestFile = "";
					for(int j=0;j<genomefiles.length;j++)
					{
						if(genomefiles[j].length() > longest)
						{
							longest = genomefiles[j].length();
							longestFile = genomefiles[j].getPath();
						}
					}
					for(int j=0;j<genomefiles.length;j++)
					{
						if(!genomefiles[j].getPath().equals(longestFile))
						{
							File delete = new File(genomefiles[j].getPath());
							delete.delete();
						}
					}
				}
			}
		}
		
		
	}
}
