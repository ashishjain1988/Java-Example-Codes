package com.iupui.info.spinner;
/**
 * 
 * @author Ashish Jain
 * 
 * This class is used to calculate and store the Initial and 
 * Iterative rank score of the the interaction of the protein 
 * node in a protein network by reading the protein-protein 
 * interaction data.
 * 
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class SpinnerMain {

	private final static double SIGMA = 0.85;
	private final static int FILE_LENGTH = 200;
	private final static int ALPHA = 2;
	private final static String FILE_PATH = /*"C:\\Users\\Ashish\\Documents\\workspace-sts-3.6.1.RELEASE\\Advanced_Search_HAPPI_DB.csv"*/"D:\\Lab_Projects\\SPINNER_Project\\export_HAPPI_REVIWED.dsv";
	private final static String INPUT_FILE_PATH = "D:\\Lab_Projects\\SPINNER_Project\\PAGER_GENE_TO_UNIPROT_ID.txt";
	
	public static void main(String[] args) {
		//String FILE_PATH = args[0];
		String diseaseName = "ALS_Delta_HAPPI_Query_test";
		/*if(args[2] != null)
		{
			diseaseName = args[2];
		}*/
		int ITERATION_NUMBER = FILE_LENGTH;
		/*if(args[1] != null)
		{
			try {
				ITERATION_NUMBER = Integer.parseInt(args[1]);
			} catch (Exception e) {
				//Default value
				ITERATION_NUMBER = FILE_LENGTH;
			}
		}*/
		Map<String,Integer> proteinIndexMap = new HashMap<String, Integer>();
		Map<Integer,String> indexProteinMap = new HashMap<Integer,String>();
		Map<Integer,Double> initialRankingMap = new HashMap<Integer,Double>();
		Map<Integer,Double> iterativeRankingMap = new HashMap<Integer,Double>();
		double [] outDegree;
		List<String> fileData = new LinkedList<String>();
		double networkData[][];
		double r[][];
		double a[][];
		int N = 0;
		Map<Integer, Integer> networkDivisionMap = new HashMap<Integer, Integer>();//Node No and Network Id
		Map<Integer, Integer> networkDivisionFinal = new HashMap<Integer, Integer>();
		Map<Integer, Integer> networkSize = new HashMap<Integer, Integer>();
		List<String> genesList;
		BufferedReader br;
		try {
			
			br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			//Skipping the first header line
			String line = br.readLine();
			genesList = new ArrayList<String>();
			while (line != null) {
				String lineData[] = line.split("\t");
				genesList.add(lineData[1].trim());
				line = br.readLine();
			}
			br.close();
			
			br = new BufferedReader(new FileReader(FILE_PATH));
			//Skipping the first header line
			line = br.readLine();
			line = br.readLine();
			int count = 0;

			//Index Creation
			while (line != null) {
				fileData.add(line);
				String lineData[] = line.split("\t");
				if(proteinIndexMap.get(lineData[0].trim()) == null)
				{
					proteinIndexMap.put(lineData[0].trim(),count);
					networkDivisionMap.put(count,count);
					networkSize.put(count, 1);
					indexProteinMap.put(count, lineData[0].trim());
					count++;
				}
				if(proteinIndexMap.get(lineData[1].trim()) == null)
				{
					proteinIndexMap.put(lineData[1].trim(),count);
					networkDivisionMap.put(count,count);
					networkSize.put(count, 1);
					indexProteinMap.put(count, lineData[1].trim());
					count++;
				}
				line = br.readLine();
			}
			br.close();

			N = indexProteinMap.size();
			//System.out.println("value of N is "+N);
			//Network Creation
			networkData = new double[N][N];
			a = new double[N][N];
			r = new double [N][ITERATION_NUMBER];
			int indexA,indexB;
			for(int j=0;j<fileData.size();j++)
			{
				line = fileData.get(j);
				String lineData[] = line.split("\t");
				indexA = proteinIndexMap.get(lineData[0].trim());
				indexB = proteinIndexMap.get(lineData[1].trim());
				networkData[indexA][indexB] = Double.parseDouble(lineData[2].trim());
				networkData[indexB][indexA] = Double.parseDouble(lineData[2].trim());
				int check = Double.compare(Double.parseDouble(lineData[2].trim()),0);
				/*if(lineData[0].trim().equalsIgnoreCase("CHP1_HUMAN") || lineData[1].trim().equalsIgnoreCase("CHP1_HUMAN"))
				{
					System.out.println(indexProteinMap.get(indexA)+" "+indexProteinMap.get(indexB)+ " "+Double.parseDouble(lineData[2].trim())) ;
				}*/
				if(check > 0)
				{
					a[indexA][indexB] = 1;
					a[indexB][indexA] = 1;
				}else if(check == 0)
				{
					a[indexA][indexB] = 0;
					a[indexB][indexA] = 0;
				}else
				{
					a[indexA][indexB] = -1;
					a[indexB][indexA] = -1;
				}
			}

			//Calculating OutDegree
			outDegree = new double [N];
			double degreeCount;
			for(int i=0;i<N;i++)
			{
				degreeCount = 0;
				for(int j=0;j<N;j++)
				{
					/*degreeCount+=networkData[i][j];*/
					
					if(a[i][j]>0)
					{
						degreeCount = degreeCount + 1;
					}
				}
				outDegree[i] = degreeCount;
			}

			// Calculating Initial Rp Score
			double intialRSum = 0;
			double intialASum = 0;
			for(int i=0;i<N;i++)
			{
				intialRSum = 0;
				intialASum = 0;
				for(int j=0;j<N;j++)
				{
					intialRSum += networkData[i][j];
					intialASum +=a[i][j];
				}
				intialRSum = Math.pow(intialRSum, ALPHA);
				if(intialASum != 0)
				{
					//System.out.println("size of r is"+r.length);
					//System.out.println(i);
					r[i][0] = intialRSum/intialASum;
					/*DecimalFormat df = new DecimalFormat("####0.00");
					System.out.println("Value: " + df.format(value));*/
					initialRankingMap.put(i, r[i][0]);
				}
			}
			
			//*************Network division finding Start*****************//
			int temp;
			//Network Division
			for(int i=0;i<N;i++)
			{
				for(int j=0;j<N;j++)
				{
					if(a[i][j] != 0)
					{
						if(networkDivisionMap.get(i) == networkDivisionMap.get(j))
						{
							if(networkSize.get(networkDivisionMap.get(i)) != networkSize.get(networkDivisionMap.get(j)))
							{
								System.err.println("Something Wrong for "+indexProteinMap.get(i)+" "+indexProteinMap.get(j));
							}
						}
						if(i!=j && Integer.compare(networkDivisionMap.get(i), networkDivisionMap.get(j)) != 0)
						{
							int largerNetworkNode = 0;
							int smallerNetworkNode = 0;
							if(Integer.compare(networkSize.get(networkDivisionMap.get(i)),networkSize.get(networkDivisionMap.get(j))) < 0)
							{
								largerNetworkNode = j;
								smallerNetworkNode = i;
							}else if(Integer.compare(networkSize.get(networkDivisionMap.get(i)),networkSize.get(networkDivisionMap.get(j))) > 0)
							{
								largerNetworkNode = i;
								smallerNetworkNode = j;
							}else
							{
								largerNetworkNode = i;
								smallerNetworkNode = j;
							}
							
							temp = networkSize.get(networkDivisionMap.get(largerNetworkNode)) + networkSize.get(networkDivisionMap.get(smallerNetworkNode));
							networkSize.put(networkDivisionMap.get(largerNetworkNode), (temp));
							networkSize.put(networkDivisionMap.get(smallerNetworkNode), 0);
							for(int k=0;k<N;k++)
							{
								if(Integer.compare(networkDivisionMap.get(k),networkDivisionMap.get(smallerNetworkNode)) == 0)
								{
									networkDivisionMap.put(k, networkDivisionMap.get(largerNetworkNode));
								}
							}
						}
					}
				}
			}
			temp = 0;
			//System.out.println(networkSize.size());
			for(Entry<Integer, Integer> entry : networkSize.entrySet())
			{
				if(entry.getValue() > 0)
				{
					temp = temp + entry.getValue();
				}
			}
			//System.out.println("Total Number of nodes : "+temp);
			//System.out.println("Total Number of nodes : "+proteinIndexMap.size());
			//System.out.println("Size of the network data is : "+networkSize);
			Map<Integer, Integer> networkSizeMap = new HashMap<Integer, Integer>();
			//Networks Nodes Counting
			for(int i=0;i<N;i++)
			{
				if(networkSizeMap.containsKey(networkDivisionMap.get(i)))
				{
					int count1 = networkSizeMap.get(networkDivisionMap.get(i)) + 1;
					networkSizeMap.put(networkDivisionMap.get(i),(count1));
				}else
				{
					//System.out.println(networkDivision.get(i));
					networkSizeMap.put(networkDivisionMap.get(i),1);
				}
			}
			

			System.out.println("Size of the network data is : "+networkSizeMap.size());
			
			//Network Division Sorting
			List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(networkSizeMap.entrySet());
			Collections.sort(list, new ValueTanKeyComparator<Integer, Integer>());
			   
			   //Network Division Normalization on the basis of size of tree
			   int count1 = 1;
			   for(Map.Entry<Integer, Integer> key : list)
			   {
				  
				   for(int i=0;i<N;i++)
				   {
					   //System.out.println(networkDivisionMap.get(i)+"  "+ key.getKey());
					   if(Integer.compare(networkDivisionMap.get(i), key.getKey()) == 0)
					   {
						   networkDivisionFinal.put(i, count1);
					   }
				   }
				   count1++;
			   }
			  //*************Network division finding End*****************//
			   
			//Map<String,Integer> nodeNetworkList1 = new HashMap<String, Integer>();
			//Calculating Iterative Confidence
			for(int k=1;k<ITERATION_NUMBER;k++)
			{
				for(int i=0;i<N;i++)
				{
					double sumR = 0;
					for(int j=0;j<N;j++)
					{
						if(outDegree[j] != 0)
						{
							sumR += ((networkData[j][i]*r[j][k-1]/outDegree[j]));
						}else
						{
							sumR+=0;
						}
					}
					r[i][k] = (1-SIGMA)*r[i][0] + SIGMA * sumR;
					if(k == ITERATION_NUMBER - 1)
					{
						iterativeRankingMap.put(i, r[i][k]);
					}
				}
			}
			initialRankingMap = sortByComparator(initialRankingMap);
			iterativeRankingMap = sortByComparator(iterativeRankingMap);
			int intialRanking[] = createRankingArray(initialRankingMap, N);
			int iterativeRank [] = createRankingArray(iterativeRankingMap, N);
			Map<Integer, Map<Integer, Integer>> networkBasedIntitalRanks = CreateNetworkBasedRank.createNetworkBasedRankMap(networkDivisionFinal, initialRankingMap);
			Map<Integer, Map<Integer, Integer>> networkBasedIterativeRanks = CreateNetworkBasedRank.createNetworkBasedRankMap(networkDivisionFinal, iterativeRankingMap);
			System.out.println("Entering delta calculation");
			//Creating Delta From R[0] and R[No of interations]
			Map<Integer, Double> deltaValueMap = new HashMap<Integer, Double>();// Contains Node Index as key and Delta as Map
			Map<Integer, Double> deltaRankMap = new HashMap<Integer, Double>();
			Double delta = 0.0;
			Double Rank = 0.0;
			double R0 = 0;
			double RN = 0;
			for(int i=0;i<N;i++)
			{
				R0 = intialRanking[i];
				RN = iterativeRank[i];
				Rank = (RN - R0);
				delta = Rank /R0;
				deltaRankMap.put(i,Rank);
				deltaValueMap.put(i,delta);
			}
			System.out.println("Exiting delta calculation");
			//Printing the result into result.txt file
			PrintWriter writer = new PrintWriter(diseaseName+".txt", "UTF-8");
			writer.println("Node\tDegree\tR[0]\tR[0] Rank\tR[0] Rank In Network\tR["+ITERATION_NUMBER+"]\tRank R["+ITERATION_NUMBER+"]\tRank R["+ITERATION_NUMBER+"] in Network\tSeed Flag\tNetwork_Id\tDelta Rank((R["+ITERATION_NUMBER+"]-R0))\tDelta((R["+ITERATION_NUMBER+"]-R0)/R0)");
			for(int i=0;i<N;i++)
			{
				writer.print(indexProteinMap.get(i));
				writer.print("\t");
				writer.printf("%.2f",outDegree[i]);
				writer.print("\t");
				writer.printf("%.2f",r[i][0]);
				writer.print("\t");
				writer.print(intialRanking[i]);
				writer.print("\t");
				writer.print(networkBasedIntitalRanks.get(networkDivisionFinal.get(i)).get(i));
				writer.print("\t");
				writer.printf("%.5f",r[i][ITERATION_NUMBER - 1]);
				writer.print("\t");
				writer.print(iterativeRank[i]);
				writer.print("\t");
				writer.print(networkBasedIterativeRanks.get(networkDivisionFinal.get(i)).get(i));
				writer.print("\t");
				writer.print(genesList.contains(indexProteinMap.get(i))?"Y":"N");
				writer.print("\t");
				writer.print(networkDivisionFinal.get(i));
				writer.print("\t");
				writer.print(deltaRankMap.get(i));
				writer.print("\t");
				writer.print(deltaValueMap.get(i));
				writer.println();
			}
			writer.close();
			System.out.println("You can see your result in the "+diseaseName+".txt file in your current folder.");
		} 
		catch (FileNotFoundException e) {
			System.out.println("The input file location is not correct or Access is restricted");
		}
		catch (IOException e) {
			System.out.println("There is some problem in reading/writing input/output file");
		}
		catch (Exception e) {
			System.out.println("Data is not in correct format in input file");
			e.printStackTrace();
		}
		
	}
	public static Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<Integer, Double>> list = 
			new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
                                           Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Iterator<Map.Entry<Integer, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static int[] createRankingArray(Map<Integer, Double> sortMap, int size)
	{
		int[] a = new int[size];
		int count = 1;
		double previous=0;
		for (Map.Entry<Integer, Double> entry : sortMap.entrySet()) {
			a[entry.getKey()] = count;
			if(entry.getValue() != previous)
			{
				count++;
			}
		}
		return a;
	}
}
