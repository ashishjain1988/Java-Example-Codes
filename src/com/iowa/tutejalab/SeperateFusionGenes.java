package com.iowa.tutejalab;

/**
 * @author Ashish Jain
 * This file is used to count the number of differntailly expressed genes.
 * This program takes the output path of cuffdiff_analysis.sh script as its input
 * asn works on that data
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeperateFusionGenes {

	public static void main(String[] args) throws IOException {
		
		File[] files = new File("/home/jain/Placenta_Geo_Dataset/Placenta_Different_Stages_Analysis/Cuffdiff_Output/").listFiles();
		PrintWriter pw1 = new PrintWriter("Results");
		for (File f:files)
		{
			if(f.isDirectory())
			{
				File[] diffFiles=new File(f.getAbsolutePath()+"/Analysis/").listFiles();
				for (File f1:diffFiles)
				{
					if(f1.getName().endsWith("genes.txt"))
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
		
	}
}
