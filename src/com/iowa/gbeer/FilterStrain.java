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

import org.apache.commons.io.FileUtils;

public class FilterStrain {
	
	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("/home/jain/Gram_Positive_Bacteria_Study/Organisms_Lists_from_PATRIC/Proteobacteria/PatricOrgsGenomesMap.txt"));
		//String basePath = "/home/jain/Gram_Positive_Bacteria_Study/Bacteroidetes_Genomes_Path/";
		String line = br.readLine();
		Set<String> dirPaths = new HashSet<>();
		Map<String, String> distinctSpeciesGenomeLocationMap = new HashMap<String,String>();
		while(line!=null)
		{
			String lineData[] = line.split("\t");
			String strainName = lineData[0].trim();
			String folderPath = lineData[1].trim();
			if(!folderPath.equals("null"))
			{
				String orgName = getOrgName(strainName);
				if(distinctSpeciesGenomeLocationMap.get(orgName) != null)
				{
					String lastOrgPath = distinctSpeciesGenomeLocationMap.get(orgName);
					String newOrgPath = checkAccession(lastOrgPath, folderPath);
					distinctSpeciesGenomeLocationMap.put(orgName, newOrgPath);
					
				}else
				{
					distinctSpeciesGenomeLocationMap.put(orgName, getGenomeFileName(new File(folderPath)).getParent());
				}
				dirPaths.add(folderPath);
				String folderName = folderPath.split("/")[4];
				//FileUtils.copyDirectory(new File(folderPath), new File(basePath+folderName));
			}
			line = br.readLine();
		}
		br.close();
		PrintWriter pw = new PrintWriter("newOrgListAfterStrainFiltering.txt");
		for(Entry<String, String> entry : distinctSpeciesGenomeLocationMap.entrySet())
		{
			pw.println(entry.getKey()+"\t"+entry.getValue());
		}
		pw.close();	
	}
	
	public static String getOrgName(String strainName)
	{
		String names[] = strainName.split(" ");
		return names[0]+"_"+names[1];
	}
	
	public static String checkAccession(String oldPath,String newPath)
	{
		File olddir = new File(oldPath);
		File oldfile = getGenomeFileName(olddir);
		File newdir = new File(newPath);
		File newfile = getGenomeFileName(newdir);
		//System.out.println(newfile.getName().split("_")[1].split(".g")[0]);
		String oldAccession = oldfile.getName().split("_")[1].split(".g")[0];
		String newAccession = newfile.getName().split("_")[1].split(".g")[0];
		if(Integer.parseInt(oldAccession) < Integer.parseInt(newAccession))
		{
			return oldfile.getParent();
		}else
		{
			return newfile.getParent();
		}
	}
	
	public static File getGenomeFileName(File dir)
	{
		File [] genomefiles = dir.listFiles();
		File genomeFile = null;
		if(genomefiles.length > 1)
		{
			long longest = 0;
			for(int j=0;j<genomefiles.length;j++)
			{
				if(genomefiles[j].length() > longest)
				{
					longest = genomefiles[j].length();
					genomeFile = genomefiles[j];
				}
			}
			
		}else
		{
			genomeFile = genomefiles[0];
		}
		return genomeFile;
	}
}
