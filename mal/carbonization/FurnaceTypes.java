package mal.carbonization;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum FurnaceTypes {
	
	IRONOFF("iron", "ironFurnace", "Iron Furnace",false),
	IRONON("iron", "ironFurnace", "Iron Furnace",true),
	INSULATEDOFF("insulated", "insulatedFurnace", "Insulated Iron Furnace",false),
	INSULATEDON("insulated", "insulatedFurnace", "Insulated Iron Furnace",true),
	STEELOFF("steel", "steelFurnace", "Insulated Steel Furnace",false),
	STEELON("steel", "steelFurnace", "Insulated Steel Furnace",true);
	
	private String[] textureArray = new String[6];
	private String commonName;
	private String fancyName;
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	FurnaceTypes(String material, String commonName, String fancyName, boolean isOn)
	{
		//Populate the texture array for the images on each side
		textureArray[0] = material+"FurnaceBottomTexture";
		textureArray[1] = material+"FurnaceTopTexture";
		textureArray[2] = material+"FurnaceSideTexture";
		textureArray[5] = material+"FurnaceSideTexture";
		textureArray[4] = material+"FurnaceSideTexture";
		if(!isOn)
			textureArray[3] = material+"FurnaceFrontTexture";
		else
			textureArray[3] = material+"FurnaceFrontOnTexture";
		
		//Used for internal functions
		this.commonName = commonName;
		
		//The name shown in game
		this.fancyName = fancyName;
	}
	
	public String getFancyName()
	{
		return fancyName;
	}
	
	public String getCommonName()
	{
		return commonName;
	}
	
	public String getTexture(int value)
	{
		if(value<textureArray.length)
			return textureArray[value];
		else
			return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void makeIcons(IIconRegister ir)
	{
		icons = new IIcon[textureArray.length];
		for(int i = 0; i<icons.length; i++)
		{
			icons[i] = ir.registerIcon("carbonization:"+textureArray[i]);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side)
	{
		return icons[side];
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