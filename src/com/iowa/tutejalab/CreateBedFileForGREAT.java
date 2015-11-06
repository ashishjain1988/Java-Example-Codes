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
			String start = lineData[4].trim();
			String stop = lineData[5].trim();
			dataList = new ArrayList<>();
			dataList.add(chromosome);
			dataList.add(strand);
			dataList.add(start);
			dataList.add(stop);
			geneLocationMap.put(geneName,dataList);
			line = br.readLine();
		}
		br.close();
		br = new BufferedReader(new FileReader("/home/jain/Placenta_Geo_Dataset/Cuffdiff_Output/Female_Male_Diff/DEG_genes.txt"));
		line = br.readLine();
		PrintWriter pw = new PrintWriter("/home/jain/Placenta_Geo_Dataset/Cuffdiff_Output/Female_Male_Diff/file.bed");
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
				if(geneLocationMap.containsKey(geneList[i]))
				{
					dataList = geneLocationMap.get(geneList[i]);
					pw.println(dataList.get(0)+"\t"+dataList.get(2)+"\t"+dataList.get(3)+"\t"+gene+"\t1\t"+dataList.get(1));
				}else
				{
					System.out.println(geneList[i]);
				}
			}
			line = br.readLine();
		}
		br.close();
		pw.close();
		
		
		
		
	}
}
