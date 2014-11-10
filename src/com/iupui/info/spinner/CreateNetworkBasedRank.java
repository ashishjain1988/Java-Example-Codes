package com.iupui.info.spinner;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CreateNetworkBasedRank {
	
	public static Map<Integer, Map<Integer, Integer>> createNetworkBasedRankMap(Map<Integer, Integer> NetworkDivisionMap, Map<Integer, Double> nodeScoreMap)
	{
		Map<Integer, Map<Integer, Double>> networkBasedNodeScores = new HashMap<Integer, Map<Integer,Double>>();
		Map<Integer, Map<Integer, Integer>> networkBasedNodeRanks = new HashMap<Integer,Map<Integer,Integer>>();
		
		Map<Integer, Double> tempMap;
		int networkId;
		Double score;
		
		// Create Network Based Group
		for(Entry<Integer,Double> entry : nodeScoreMap.entrySet())
		{
			networkId = NetworkDivisionMap.get(entry.getKey());
			score = entry.getValue();
			if(networkBasedNodeScores.get(networkId) == null)
			{
				tempMap = new HashMap<Integer, Double>();
			}else
			{
				tempMap = networkBasedNodeScores.get(networkId);
			}
			tempMap.put(entry.getKey(), score);
			networkBasedNodeScores.put(networkId, tempMap);
		}
		
		Map<Integer, Integer> tempMap1;
		// Create Network Based Group Ranking
		for(Entry<Integer, Map<Integer, Double>> entry : networkBasedNodeScores.entrySet())
		{
			networkId = entry.getKey();
			tempMap = SpinnerMain.sortByComparator(entry.getValue());
			tempMap1 = new HashMap<Integer, Integer>();
			int count = 1;
			double previous=0;
			for (Map.Entry<Integer, Double> entry1 : tempMap.entrySet()) {
				tempMap1.put(entry1.getKey(), count);
				//if(entry1.getValue() != previous)
				//{
					count++;
					//previous = entry1.getValue();
				//}
			}
			networkBasedNodeRanks.put(networkId, tempMap1);
		}
		
		
		return networkBasedNodeRanks;
	}

}
