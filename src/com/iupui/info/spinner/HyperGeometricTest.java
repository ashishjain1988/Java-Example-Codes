package com.iupui.info.spinner;

import java.util.ArrayList;
import java.util.List;

public class HyperGeometricTest {


	//private static String sign = "";
	public static void main(String[] args) {
		int N, K, n, k;
		N = 13112;//Total Protein In DB
		K = 60;// Seeds in all the Network DB
		n = 1628;//Total Protein in Sub Network
		k = 59;//Seed in Subnetwork
		HyperGeometricTest obj = new HyperGeometricTest();
		List<Object> obj1 = obj.hyperGeometricTest(N, K, n, k);
		System.out.println("P(N="+N+",K="+K+",n="+n+",k="+k+"): "+obj1.get(0)+" "+obj1.get(1));
	}

	/*private float chooseComb(double n,double k)
	{
		float s = 1;
		if (k > n/2)
		{
			k = n-k;
		}
		for (int i=0;i<k;i++)i in range(0, k){
			s = s * (n-i) / (k-i);
		}
		return s;

	}
*/
	private double LogChoosecomb(double n,double k)
	{
		double s = 1;
		if (k > n/2)
		{
			k = n-k;
		}
		for (int i=0;i<k;i++)/*i in range(0, k)*/{
			s = s + Math.log10(n-i) - Math.log10(k-i);
		}
		return s;

	}

	private List<Object> hyperGeometricTest(int N,int K,int n,int k)
	{
		List<Object> obj = new ArrayList<Object>();
		double p = LogChoosecomb (K,k) + LogChoosecomb (N-K, n-k) - LogChoosecomb (N, n);
		String sign="";
		//Float used for calculating  the over/under representation
		float fN,fK,fn,fk;
		fN = N;
		fk = k;
		fK = K;
		fn = n;
		if(fK/fN < fk/fn)
			sign = "Over-representation";
		else
			sign = "Under-representation";

		try {
			p = Math.pow(10, p);
		} catch (Exception e) {
			System.out.println("Error: Arithmetic Error!\n");
			System.exit(0);
		}
		obj.add(p);
		obj.add(sign);
		return obj;/*, sign*/

	}

}
