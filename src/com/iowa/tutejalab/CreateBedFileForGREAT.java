package com.iowa.tutejalab;
/**
 * @author Ashish Jain
 * This file is used to create the bed file, used as a input for
 * the GREAT GO Database. This program takes input the list of genes.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateBedFileForGREAT {

	
	public static void main(String[] args) throws IOException {
		
		File[] files = new File("/home/jain/Placenta_Geo_Dataset/Placenta_Different_Stages_Analysis/E9.5_Studies_Ouput/").listFiles();
		Map<String, List<String>> geneLocationMap = getReferenceData("/home/jain/Placenta_Geo_Dataset/refFlat.txt");
		for (File f:files)
		{
			if(f.isDirectory())
			{
				String filePath = f.getAbsolutePath()+"/Analysis";
				File[] genefiles = new File(f.getAbsolutePath()+"/Analysis/").listFiles();
				for (File geneFile:genefiles)
				{
					String fileName = geneFile.getName();
					if(fileName.endsWith("genes.txt"))
					{
						System.out.println(fileName);
						File outDir = new File(filePath+"/BED_Files/");
						if(!outDir.exists())
						{
							outDir.mkdir();
						}
						createBEDFile(filePath,fileName,geneLocationMap);
					}
				}
			}
			//break;
		}
	}
	
	public static void createBEDFile(String filePath,String fileName,Map<String, List<String>> geneLocationMap) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath+"/DEG_all_genes.txt"));
		String line = br.readLine();
		PrintWriter pw = new PrintWriter(filePath+"/BED_Files/"+"/file_"+fileName.split(".txt")[0]+".bed");
		List<String> dataList;
		int count = 0;
		int unann = 0;
		System.out.println("Gene Not found in file "+fileName+" are:");
		while(line!=null)
		{
			String gene = line.trim();
			String geneList[];
			if(gene.contains(","))
			{
				geneList = gene.split(",");
			}else
			{
				geneList = new String[] {gene};
			}
			for(int i=0;i<geneList.length;i++)
			{
				count++;
				if(geneLocationMap.containsKey(geneList[i]))
				{
					dataList = geneLocationMap.get(geneList[i]);
					pw.println(dataList.get(0)+"\t"+dataList.get(2)+"\t"+dataList.get(3)+"\t"+geneList[i]);
				}else
				{
					System.out.println(geneList[i]);
					unann += 1;
				}
			}
			line = br.readLine();
		}
		System.out.println("Total annotated genes count in file "+fileName+" is "+count);
		System.out.println("Total unannotated genes count in file "+fileName+" is "+unann);
		br.close();
		pw.close();
	}
	
	public static Map<String, List<String>> getReferenceData(String fileName) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		Map<String, List<String>> geneLocationMap = new HashMap<>();
		List<String> dataList;
		String line = br.readLine();
		while(line!=null)
		{
			String lineData[] = line.split("\t");
			String geneName = lineData[0].trim();
			String chromosome = lineData[2].trim();
			String strand = lineData[3].trim();
			int start = Integer.parseInt(lineData[4].trim());
			int stop = Integer.parseInt(lineData[5].trim());
			if(strand.equals("+"))
			{
				start -= 500;
				stop = start + 500;
			}else
			{
				stop += 500;
				start = stop -500;
			}
			dataList = new ArrayList<>();
			dataList.add(chromosome);
			dataList.add(strand);
			dataList.add(String.valueOf(start));
			dataList.add(String.valueOf(stop));
			geneLocationMap.put(geneName,dataList);
			line = br.readLine();
		}
		br.close();
		return geneLocationMap;
		
	}
	
}
