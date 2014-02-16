package mal.core;

public class FormatReference {

	//compress a number into scientific notation
	public static String compressInteger(int num)
	{
		String s = "";
		if(num < 0)
			s += "-";
		
		//get first value
		int value = num;
		int evalue = 0;
		while(value>9)
		{
			value = (int)Math.floor(value/10);
			evalue++;
		}
		s += value+".";
		
		//decimal places
		value = num-value*evalue*10;
		while(value>99)
			value = (int)Math.floor(value/10);
		s+=value+"E"+evalue;
		
		return s;
	}
	
	public static String compressLong(long num)
	{
		String s = "";
		if(num < 0)
			s += "-";
		if(num < 10000)
		{
			s += num;
			return s;
		}
		
		//get first value
		long value = num;
		int evalue = 0;
		while(value>9)
		{
			value = (long)Math.floor(value/10);
			evalue++;
		}

		//decimal places
		float dvalue = (float) (num/Math.pow(10,evalue));
		s+=String.format("%.2f", dvalue)+"E"+evalue;
		
		return s;
	}
}
/*******************************************************************************
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/