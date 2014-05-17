package mal.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import mal.carbonization.carbonization;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/*
 * Locate and parse the version info so we can send it if there is a new version
 */
public class VersionInfo {
	public static final String ModName = "Carbonization";
	public static final String Version = "1.0.1";
	public static final String VersionURL = "https://dl.dropboxusercontent.com/s/gswvn9mq4afqfj2/carbonizationVersion.info?dl=1";
	
	boolean majorUpdate;
	boolean newVersion;
	boolean newMCVersion;
	boolean versionCheckCompleted;
	
	String latestVersion;
	String modName;
	String modVersion;
	String latestMCVersion = "1.6.4";
	String versionDescription = "";
	String modURL;
	Logger modLogger = FMLLog.getLogger();
	
	public VersionInfo(String name, String version, String url)
	{
		modName = name;
		modVersion = (latestVersion = version);
		modURL = url;
	}
	
	public VersionInfo(String name, String version, String url, Logger logger)
	{
		this(name, version, url);
		this.modLogger = logger;
	}
	
	public static int[] parseVersion(String rawData)
	{
		ArrayList vtoken = new ArrayList();
		String[] tokens = rawData.trim().split("[\\. ]");
		
		for(int i = 0; i < tokens.length; i++)
		{
			tokens[i] = tokens[i].trim();
			if(tokens[i].matches("[0-9]+"))
				vtoken.add(Integer.valueOf(tokens[i]));
			else if(tokens[i].matches("[0-9]+[a-z]"))
			{
				String num = tokens[i].substring(0, tokens[i].length()-1);
				vtoken.add(Integer.valueOf(num));
				vtoken.add(Integer.valueOf(Character.getNumericValue(tokens[i].charAt(tokens[i].length()-1))));
			}
		}
		
		int[] value = new int[vtoken.size()];
		for(int i = 0; i < value.length; i++)
		{
			value[i] = ((Integer)vtoken.get(i)).intValue();
		}
		return value;
	}
	
	public static boolean beforeTargetVersion(String version, String target)
	{
		boolean flag = false;
		int[] versionToken = parseVersion(version);
		int[] targetToken = parseVersion(target);
		
		for(int i = 0; (i<versionToken.length && i<targetToken.length); i++)//array index out of bounds are bad, mmKay?
		{
			if(versionToken[i] < targetToken[i])
			{
				flag = true;
				break;
			}
			else if(versionToken[i] > targetToken[i])
			{
				flag = false;
				break;
			}
			else if(i==versionToken.length && versionToken.length < targetToken.length)
			{
				flag = true;
			}
		}
		
		return flag;
	}
	
	public static boolean afterTargetVersion(String target, String version)
	{
		boolean flag = false;
		int[] versionToken = parseVersion(version);
		int[] targetToken = parseVersion(target);
		
		for(int i = 0; (i<versionToken.length && i<targetToken.length); i++)
		{
			if(versionToken[i] > targetToken[i])
			{
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public static void doVersionCheck()
	{
		VersionInfo vInfo = new VersionInfo(ModName, Version, "https://dl.dropboxusercontent.com/s/gswvn9mq4afqfj2/carbonizationVersion.info?dl=1");
		TickHandler.registerModVersionInfo(vInfo);
		TickHandler.init();
		TickRegistry.registerScheduledTickHandler(TickHandler.instance, Side.CLIENT);
		
		vInfo.checkForNewVersion();
		
	}
	
	public void checkForNewVersion()
	{
		Thread versionThread = new VersionCheckThread();
		versionThread.start();
	}
	
	public String getCurrentVersion()
	{
		return modVersion;
	}
	
	public String getLatestVersion()
	{
		return latestVersion;
	}
	
	public String getVersionDescription()
	{
		return versionDescription;
	}
	
	public boolean isMajorUpdate()
	{
		return majorUpdate;
	}
	
	public boolean isNewVersionAvailable()
	{
		return newVersion;
	}
	
	public boolean isMCOutdated()
	{
		return newMCVersion;
	}
	
	public boolean isVersionCheckComplete()
	{
		return versionCheckCompleted;
	}
	
	private class VersionCheckThread extends Thread
	{
		private VersionCheckThread()
		{
		}

		public void run()
		{
			try
			{
				String location = "https://dl.dropboxusercontent.com/s/gswvn9mq4afqfj2/carbonizationVersion.info?dl=1";

				HttpURLConnection connection = null;

				while ((location != null) && (!location.isEmpty()))
				{
					URL url = new URL(location);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.60 Safari/537.17");

					connection.connect();
					location = connection.getHeaderField("Location");
				}

				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				VersionInfo.this.latestVersion = reader.readLine();
				VersionInfo.this.majorUpdate = Boolean.parseBoolean(reader.readLine());
				VersionInfo.this.latestMCVersion = reader.readLine();
				VersionInfo.this.versionDescription = reader.readLine();
				reader.close();

				if (VersionInfo.beforeTargetVersion(VersionInfo.this.modVersion, VersionInfo.this.latestVersion))
				{
					VersionInfo.this.modLogger.log(Level.INFO, "An updated version of " + VersionInfo.this.modName + " is available: " + VersionInfo.this.latestVersion + ".");
					VersionInfo.this.newVersion = true;
					if (VersionInfo.this.majorUpdate)
					{
						VersionInfo.this.modLogger.log(Level.INFO, "This update has been marked as major.");
					}
					if (VersionInfo.beforeTargetVersion("1.6.4", VersionInfo.this.latestMCVersion))
					{
						VersionInfo.this.newMCVersion = true;
						VersionInfo.this.modLogger.log(Level.INFO, "This update is for Minecraft " + VersionInfo.this.latestMCVersion + ".");
					}
				}
				else if(carbonization.VERBOSEMODE)
				{
					VersionInfo.this.modLogger.log(Level.INFO, "The current version of " + VersionInfo.this.modName + " is: " + VersionInfo.this.getCurrentVersion() + "The latest version is: " + VersionInfo.this.latestVersion + ".");
					VersionInfo.this.newVersion = true;
					if (VersionInfo.this.majorUpdate)
					{
						VersionInfo.this.modLogger.log(Level.INFO, "This update has been marked as major.");
					}
					if (VersionInfo.beforeTargetVersion("1.6.4", VersionInfo.this.latestMCVersion))
					{
						VersionInfo.this.newMCVersion = true;
						VersionInfo.this.modLogger.log(Level.INFO, "This update is for Minecraft " + VersionInfo.this.latestMCVersion + ".");
					}
				}

			}
			catch (Exception e)
			{
				VersionInfo.this.modLogger.log(Level.WARNING, "Version Check Failed: " + e.getMessage());
			}
			VersionInfo.this.versionCheckCompleted = true;
		}
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