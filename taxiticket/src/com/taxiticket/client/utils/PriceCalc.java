package com.taxiticket.client.utils;

import java.util.Iterator;

import com.google.common.base.Splitter;

public class PriceCalc {

	private static double MWST = 0.19;
	private static double BASE = 300;
	private static double PRO_M = 0.14;
	public static double[] getPrice(double meters)
	{
		double[]result = new double[3];
		result[0] = BASE + PRO_M * meters;
		result[1] = result[0] * MWST;
		result[2] = result[0] + result[1];
		return result;
	}

	public static void main(String[] args)
	{
		String[] st1 ={	"St.Anna Apotheke, Bürgermeister-Aurnhammer-Straße 10, 86199 Augsburg, Deutschland",
						"Jobcenter Augsburg-Stadt, August-Wessels-Straße 31, 86156 Augsburg, Deutschland"
		};
		for (String s : st1)
		{
			int pos = s.lastIndexOf(", Deutschland");
			if ( pos > 0)
			{
				String ss = s.substring(0, pos);
				System.out.println(ss);
			}
				
//			Iterator<String> it = Splitter.on(",").split(s).iterator();
//			String result="";
//			while (it.hasNext())
//			{
//				String seg = it.next();
//				if (!seg.contains("Deutschland"))
//				{
//					result += seg;
//				}
//				if (it.hasNext())
//				{
//					result +=",";
//				}
//			}
		}
	}

}
