package mal.core;

import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements IScheduledTickHandler{

	public static TickHandler instance = new TickHandler();
	
	private static ArrayList<VersionInfo> modVersionInfo = new ArrayList();
	private static boolean init;
	private static boolean sent;
	private static int modIndex = 0;
	
	public static boolean init()
	{
		if(init)
		{
			return false;
		}
		init = true;
		return true;
	}
	
	public static boolean isInit()
	{
		return init;
	}
	
	public static boolean registerModVersionInfo(VersionInfo info)
	{
		if(modVersionInfo.contains(info))
		{
			return false;
		}
		modVersionInfo.add(info);
		return true;
	}
	
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if(sent)
			return;
		
		if(modIndex < modVersionInfo.size())
		{
			VersionInfo aInfo = (VersionInfo)modVersionInfo.get(modIndex);
			
			if(aInfo.isNewVersionAvailable())
			{
				EntityPlayer p = (EntityPlayer) tickData[0];
				p.sendChatToPlayer(ChatMessageComponent.createFromText(aInfo.modName + " has a new version available: " + aInfo.getLatestVersion()));
				p.sendChatToPlayer(ChatMessageComponent.createFromText(aInfo.getVersionDescription()));
			}
			modIndex+=1;
		}
		else
		{
			sent = true;
		}
	}
	
	public EnumSet<TickType> ticks()
	{
		if(sent)
		{
			return EnumSet.noneOf(TickType.class);
		}
		return EnumSet.of(TickType.PLAYER);
	}
	
	public String getLabel()
	{
		return "carbonization.version";
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int nextTickSpacing() {
		if(!sent)
			return 200;
		return 72000;
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