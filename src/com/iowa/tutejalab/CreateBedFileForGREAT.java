package com.iowa.tutejalab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateBedFileForGREAT {

	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("/home/jain/Placenta_Geo_Dataset/refFlat.txt"));
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
				start += 500;
			}else
			{
				stop += 500;
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
		br = new BufferedReader(new FileReader("/home/jain/Placenta_Geo_Dataset/Cuffdiff_Output/InVivo_Male_Diff/Gene_NE-IV_E-Male/DEG_genes.txt"));
		line = br.readLine();
		PrintWriter pw = new PrintWriter("/home/jain/Placenta_Geo_Dataset/Cuffdiff_Output/InVivo_Male_Diff/Gene_NE-IV_E-Male/file.bed");
		int count = 0;
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
					pw.println(dataList.get(0)+"\t"+dataList.get(2)+"\t"+dataList.get(3)+"\t"+geneList[i]+"\t1\t"+dataList.get(1));
				}else
				{
					System.out.println(geneList[i]);
				}
			}
			line = br.readLine();
		}
		System.out.println(count);
		br.close();
		pw.close();
		
		
		
		
	}
}
