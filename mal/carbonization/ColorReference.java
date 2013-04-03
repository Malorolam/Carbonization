package mal.carbonization;

//A reference class for color codes
public enum ColorReference {

	BLACK("\u00A70"), DARKBLUE("\u00A71"), DARKGREEN("\u00A72"), DARKCYAN("\u00A73"), DARKRED("\u00A74"), PURPLE("\u00A75"), 
	ORANGE("\u00A76"), LIGHTGREY("\u00A77"), DARKGREY("\u00A78"), LIGHTGREEN("\u00A7a"), LIGHTCYAN("\u00A7b"), 
	LIGHTRED("\u00A7c"), PINK("\u00A7d"), YELLOW("\u00A7e"), WHITE("\u00A7f");
	
	private String colorCode;
	
	ColorReference(String code)
	{
		colorCode = code;
	}
	
	public String getCode()
	{
		return colorCode;
	}

}
/*******************************************************************************
* Copyright (c) 2013 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
* 
* 
*********************************************************************************/