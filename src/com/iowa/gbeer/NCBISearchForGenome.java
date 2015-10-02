package com.iowa.gbeer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NCBISearchForGenome {

	  public static String run(String searchField)
	    
	  {
		  try {
			
		
		String PUBMED_SERVER = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=genome&term="+searchField+"[orgn]";
	    URL url = new URL(PUBMED_SERVER);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    HttpURLConnection.setFollowRedirects(true);
	    conn.setDoInput(true);
	    conn.connect();

	    int status = conn.getResponseCode();
	    while (true)
	    {
	      int wait = 0;
	      String header = conn.getHeaderField("Retry-After");
	      if (header != null)
	        wait = Integer.valueOf(header);
	      if (wait == 0)
	        break;
	      conn.disconnect();
	      Thread.sleep(wait * 1000);
	      conn = (HttpURLConnection) new URL(PUBMED_SERVER).openConnection();
	      conn.setDoInput(true);
	      conn.connect();
	      status = conn.getResponseCode();
	    }
	    StringBuilder builder = new StringBuilder();
	    if (status == HttpURLConnection.HTTP_OK)
	    {
	      //LOG.info("Got a OK reply");
	      InputStream reader = conn.getInputStream();
	      URLConnection.guessContentTypeFromStream(reader);
	      int a = 0;
	      while ((a = reader.read()) != -1)
	      {
	        builder.append((char) a);
	      }
	    }
	    System.out.println(builder);
	    if(builder.toString().contains("<ErrorList>"))
	    {
	    	return "0";
	    }
	    String lineData = builder.toString().split("\n")[2].split("<Count>")[1].split("</Count>")[0];
	    conn.disconnect();
	    return lineData;
		  } catch (Exception e) {
			  return "NA";
			}
	    
	  }
	  public static void main(String[] args) {
		  System.out.println(NCBISearchForGenome.run("Lactococcus_lactis"));
	}}
