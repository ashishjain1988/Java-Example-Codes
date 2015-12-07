package com.iowa.tutejalab;

/**
 * @author Ashish Jain
 * This file is used to count the number of differentially expressed genes.
 * This program takes the output path of cuffdiff_analysis.sh script as its input
 * and works on that data
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SeperateFusionGenes {

	public static void main(String[] args) throws IOException {
		String Studies_Array [] = {"E9.5_Male", "E9.5_Female", "E9.5_TGC", "In-Vitro_TGC", "E7.5_Whole", "E9.5_Whole", "E14.5_Whole", "In-Vitro_TSC"};
		String FileNamesGeneric [] = {"DEG_all_genes","Placenta_all_genes","DEG_Equally_Exp_"};
		String Files_names [] = {"DEG_Only_Exp_","DEG_High_In_", "Placenta_Only_Exp_","Placenta_High_In_"};
		String geneFilesSuffix = "genes.txt";
		File[] files = new File("/home/jain/Placenta_Geo_Dataset/Placenta_Different_Stages_Analysis/Cuffdiff_Output/").listFiles();
		PrintWriter pw1 = new PrintWriter("Results.txt");
		for (File f:files)
		{
			if(f.isDirectory())
			{
				File[] diffFiles=new File(f.getAbsolutePath()+"/Analysis/").listFiles();
				for (File f1:diffFiles)
				{
					if(f1.getName().endsWith(geneFilesSuffix))
					{
						BufferedReader br = new BufferedReader(new FileReader(f1));
						List<String> geneList = new ArrayList<>();
						String line = br.readLine();
						int count = 0;
						while(line!=null)
						{
							if(line.contains(","))
							{
								String genes[] = line.split(",");
								count += genes.length;
								geneList.addAll(Arrays.asList(genes));
							}else
							{
								geneList.add(line.trim());
								count +=1;
							}
							line = br.readLine();
						}
						br.close();
						pw1.println(f.getName()+"\t"+f1.getName()+"\t"+count);
						PrintWriter pw = new PrintWriter(f.getAbsolutePath()+"/Analysis/"+f1.getName()+".new");
						for(String gene:geneList)
						{
							pw.println(gene);
						}
						pw.close();
					}
				}
			}
		}
		
		pw1.close();
		createMatplotlibMatrix("Results.txt", Studies_Array, Files_names, geneFilesSuffix,false,"matrix.csv");
		createMatplotlibMatrix("Results.txt", Studies_Array, FileNamesGeneric, geneFilesSuffix,true,"matrix_generic.csv");
		
	}
	
	public static void createMatplotlibMatrix(String filename,String[] Studies_Array, String[] Files_names,String geneFilesSuffix,boolean generic,String out) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		Map<String, String> cuffDiffStudyMapping = new HashMap<>();
		String line = br.readLine();
		while(line != null){
			String lineData[] = line.split("\t");
			cuffDiffStudyMapping.put(lineData[0].trim()+":"+lineData[1].trim(), lineData[2].trim());
			line=br.readLine();
		}
		br.close();
		System.out.println(cuffDiffStudyMapping.size());
		PrintWriter pw = new PrintWriter(out);
		for(String fileName: Files_names)
		{
			pw.println(fileName);
			for(String study:Studies_Array)
			{
				Map<String, String> tempMap = new HashMap<>();
				for(Entry<String, String> entry:cuffDiffStudyMapping.entrySet())
				{
					String key = entry.getKey();
					String StudyKey = key.split(":")[0];
					String fileKey = key.split(":")[1];
					if(generic)
					{
						if(StudyKey.contains(study) && fileKey.contains(fileName))
						{
							tempMap.put(StudyKey, entry.getValue());
						}
					}else
					{
						if(StudyKey.contains(study) && fileKey.contains(fileName+study))
						{
							tempMap.put(StudyKey, entry.getValue());
						}
					}
				}
				for(String study1:Studies_Array)
				{
					if(!study1.equals(study))
					{
						for(Entry<String, String> entry1:tempMap.entrySet())
						{
							if(entry1.getKey().contains(study1))
							{
								pw.print(entry1.getValue()+",");
							}
						}
					}else
					{
						pw.print("0,");
					}
				}
				
				pw.println();
			}
			
		}
		pw.close();
	}
	
	
	
}
