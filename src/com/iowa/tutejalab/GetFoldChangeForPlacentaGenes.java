package com.iowa.tutejalab;
/**
 * @author Ashish Jain
 * 
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GetFoldChangeForPlacentaGenes {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("/home/jain/Downloads/Combined_GeneNames.txt"));
		List<String> geneList = new ArrayList<>();
		String line = br.readLine();
		String output = "/home/jain/Placenta_Geo_Dataset/Cuffdiff_Output_New/Cuffdiff_Output_New/InVivo_InVitro_Diff_Study/Gene_E-IV_NE-Cell";
		while(line!=null)
		{
			geneList.add(line.trim());
			line = br.readLine();
		}
		br.close();
		br = new BufferedReader(new FileReader(output+"/gene_exp.diff"));
		line = br.readLine();
		PrintWriter pw = new PrintWriter(output+"/Placenta_genes_exp_1.txt");
		pw.println("Gene\tIn Vivo Exp\tIn Vitro Exp\tFold Change\tLog(Fold Change)");
		while(line!=null)
		{
			String lineData[] = line.split("\t");
			if(geneList.contains(lineData[2]) && lineData[13].equals("yes"))
			{
				pw.println(lineData[2]+"\t"+lineData[7]+"\t"+lineData[8]+"\t"+(Float.valueOf(lineData[7])/Float.valueOf(lineData[8]))+"\t"+lineData[9]);
			}
			line = br.readLine();
		}
		br.close();
		pw.close();
	}
}
