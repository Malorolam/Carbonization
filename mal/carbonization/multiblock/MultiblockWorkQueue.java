package mal.carbonization.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import mal.carbonization.CarbonizationRecipes;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Make an array of the jobs and process them
 * 
 */
public class MultiblockWorkQueue {

	private List<MultiblockWorkQueueItem> queue = new ArrayList<MultiblockWorkQueueItem>();
	private HashMap<String, Integer> oreSlagInQueue = new HashMap<String, Integer>();
	public int maxJobs;
	public boolean initilized = false;
	
	public int currentFuelUsed;
	
	public MultiblockWorkQueue()
	{
		maxJobs = 1;
	}
	public MultiblockWorkQueue(int maxJobs)
	{
		this.maxJobs = maxJobs;
		initilized = true;
	}
	
	//add a new job
	/**
	 * As long as the number of jobs in the queue is less than the maximum allowed, the 
	 * queue will add in the job, otherwise alerts us that there is no room
	 */
	public int addJob(ItemStack input, int fuelTick, int cookTime, int maxCookTime, String oreSlagType, int oreSlagAmount)
	{
		if(queue.size() < maxJobs)
		{
			queue.add(new MultiblockWorkQueueItem(input, fuelTick, cookTime, maxCookTime, oreSlagType, oreSlagAmount));
		}
		return queue.size();
	}
	
	public int addJob(MultiblockWorkQueueItem item)
	{
		if(queue.size() < maxJobs)
		{
			queue.add(item);
		}
		return queue.size();
	}

	//number of jobs in the queue
	public int numJobs()
	{
		if(queue != null)
			return queue.size();
		else
			return 0;
	}
	
	//checks to see if there is space to put another job
	public boolean hasSpace()
	{
		if(numJobs() < maxJobs)
			return true;
		return false;
	}
	
	public int getGrossMaxCookTime()
	{
		int val = 0;
		for(MultiblockWorkQueueItem item : queue)
		{
			val += item.maxCookTime;
		}
		//System.out.println("Gross Max Cook Time: " + val);
		return val;
	}
	
	public int getGrossCurrentCookTime()
	{
		int val = 0;
		for(MultiblockWorkQueueItem item : queue)
		{
			val += item.cookTime;
		}
		//System.out.println("Gross Cook Time: " + val);
		return val;
	}
	
	/*
	 * generate the data in the queue as an int list
	 */
	public int[] generateIntList(int[] list, int slotsFilled)
	{
		int indiineeded = numJobs()*6+1;
		if(list.length < slotsFilled+indiineeded)//uh oh, need to make the list bigger :3
		{
			int[] list2 = new int[slotsFilled+indiineeded];
			for(int i = 0; i<list.length; i++)
				list2[i]=list[i];
			list = list2;
		}
		
		int pos = slotsFilled;
		list[pos++] = this.maxJobs;
		if(numJobs() > 0)
			for(MultiblockWorkQueueItem item : queue)
			{
				list[pos++] = item.input.itemID;
				list[pos++] = item.input.getItemDamage();
				list[pos++] = item.input.stackSize;
				list[pos++] = item.cookTime;
				list[pos++] = item.maxCookTime;
				list[pos++] = item.slagYield;
			}
		
		return list;
	}
	
	/*
	 * reconstruct a queue from an int list
	 */
	public int reconstructQueue(int[] list, int startIndex)
	{
		queue = new ArrayList();
		int pos = startIndex;
		this.maxJobs = list[pos++];
		
		for(int i = 0; i < this.maxJobs; i++)
		{
			if(pos+5 < list.length && list[pos+2]!=0)
			{
				ItemStack input = new ItemStack(list[pos], list[pos+2], list[pos+1]);
				//ItemStack output = CarbonizationRecipes.smelting().getMultiblockSmeltingResult(input);
    			int fuelTick = CarbonizationRecipes.smelting().getMultiblockFuelTime(input);
    			int cookTime = list[pos+3];
    			int maxCookTime = list[pos+4];
    			int oreSlagAmount = list[pos+5];
    			String oreSlagType = CarbonizationRecipes.smelting().getMultiblockOreSlagType(input);
				this.addJob(input, fuelTick, cookTime, maxCookTime, oreSlagType, oreSlagAmount);
			}
			pos+= 6;
		}
		
		return queue.size();
	}
	
	public HashMap getOreMap()
	{
		return this.oreSlagInQueue;
	}
	
	public void setOreMap(HashMap<String, Integer> oreSlagInQueue)
	{
		this.oreSlagInQueue = oreSlagInQueue;
	}
	
	/*
	 * generate the data for the queue and add it to the nbt tag
	 */
	public NBTTagCompound generateNBT()
	{
		//System.out.println("Generating Queue NBT");
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setInteger("maxJobs", maxJobs);
		NBTTagList list = new NBTTagList();
		if(queue != null)
		{
			//System.out.println("Building Queue Data");
			for(MultiblockWorkQueueItem item : queue)//cycle through each item in the queue
			{
				//System.out.println("Added item: " + item.input.getDisplayName() + " (" + item.cookTime + "/" + item.maxCookTime + ")");
				NBTTagCompound job = new NBTTagCompound();
				item.generateNBT(job);
				list.appendTag(job);
			}
			nbt.setTag("Jobs", list);
		}
		
		NBTTagList slaglist = new NBTTagList();
		for(String s: this.oreSlagInQueue.keySet())
		{
			NBTTagCompound slag = new NBTTagCompound();
			slag.setString("SlagType", s);
			slag.setInteger(s, oreSlagInQueue.get(s));
			slaglist.appendTag(slag);
		}
		nbt.setTag("Slag", slaglist);

		return nbt;
	}
	
	public void retreiveNBT(NBTTagCompound nbt)
	{
		//System.out.println("Recovering Queue NBT");
		this.maxJobs = nbt.getInteger("maxJobs");
		NBTTagList list = nbt.getTagList("Jobs");
		for(int i = 0; i<list.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);
			MultiblockWorkQueueItem item = new MultiblockWorkQueueItem();
			item.retreiveNBT(tag);
			this.addJob(item);
			//System.out.println("Rebuilt item: " + item.input.getDisplayName() + " (" + item.cookTime + "/" + item.maxCookTime + ")");
		}
		
		NBTTagList slaglist = nbt.getTagList("Slag");
		if(slaglist.tagCount() > 0)
			for(int j = 0; j<list.tagCount(); j++)
			{
				NBTTagCompound tag = (NBTTagCompound)slaglist.tagAt(j);
				String s = tag.getString("SlagType");
				int value = tag.getInteger(s);
				this.addSlagToMap(s, value);
			}
		
		initilized = true;
	}
	
	//tick each job and remove finished ones
	//finished jobs are returned as a list of the input items so they can be removed elsewhere
	//the current fuel is passed in to ensure that the job can be ticked
	public List<ItemStack> tickJobs(int currentFuel)
	{
		if(queue == null || maxJobs == 0)
			return null;
		
		currentFuelUsed = 0;
		List<MultiblockWorkQueueItem> doneItems = new ArrayList();
		if(!queue.isEmpty())
		{
			for(MultiblockWorkQueueItem item:queue)
			{
				if(currentFuel >= item.fuelTick)
				{
					currentFuel -= item.fuelTick;//doesn't pass out like nbt stuff, since it's a basic type, but ensures we still process stuff
					currentFuelUsed += item.fuelTick;
					boolean b = item.tickItem();
					if(b)
					{
						this.addSlagToMap(item.oreSlagType, item.slagYield);
						
						doneItems.add(item);
					}
				}
			}
			
			for(MultiblockWorkQueueItem item:doneItems)
			{
				removeJob(item);
			}
			return tickSlag();
		}
		return null;
	}
	
	/*
	 * Attempt number 2 at outputting the correct items in the correct quanities
	 */
	private List<ItemStack> tickSlag()
	{
		List<ItemStack> ret = new ArrayList();
		HashMap<String, Integer> outputCounts = new HashMap<String, Integer>();
		
		//find all the slag types with enough stuff to make something and put the output values in a map
		for(String s: oreSlagInQueue.keySet())
		{
			System.out.println("Slag type: " + s + " with value of: " + oreSlagInQueue.get(s));
			if(oreSlagInQueue.get(s) >= carbonization.ORESLAGRATIO)
			{
				int value = CarbonizationRecipes.smelting().getOreSlagOutput(s).stackSize;
				if(outputCounts.containsKey(s))
				{
					int ov = outputCounts.get(s);
					outputCounts.put(s, value+ov);
				}
				else
					outputCounts.put(s, value);
				
				if(oreSlagInQueue.get(s) > carbonization.ORESLAGRATIO)
					oreSlagInQueue.put(s, oreSlagInQueue.get(s)-carbonization.ORESLAGRATIO);
				else
					oreSlagInQueue.remove(s);
				
				System.out.println("Adding item: " + value + "x" + CarbonizationRecipes.smelting().getOreSlagOutput(s).getDisplayName());
			}
		}
		
		//take the map and add the correct itemstacks in the correct stack counts
		for(String s: outputCounts.keySet())
		{
			int id = CarbonizationRecipes.smelting().getOreSlagOutput(s).itemID;
			int size = outputCounts.get(s);
			int damage = CarbonizationRecipes.smelting().getOreSlagOutput(s).getItemDamage();
			
			ret.add(new ItemStack(id, size, damage));
		}
		return ret;
	}
	/*private List<ItemStack> tickSlag(List<String> slagResults)
	{
		if(slagResults == null || slagResults.isEmpty())
			return null;
		
		List<ItemStack> ret = new ArrayList();
		
		for(String s : slagResults)
		{
			System.out.println("Slag name " + s);
			if(oreSlagInQueue.containsKey(s))
			{
				System.out.println("Current Slag of type " + s + ": " + oreSlagInQueue.get(s));
				if(oreSlagInQueue.get(s) >= carbonization.ORESLAGRATIO)
				{
					if(CarbonizationRecipes.smelting().getOreSlagOutput(s) != null)
					{
						ret.add(CarbonizationRecipes.smelting().getOreSlagOutput(s));//a little stupid, but we'll check for stacking later
						int i = oreSlagInQueue.get(s);
						oreSlagInQueue.put(s, i-carbonization.ORESLAGRATIO);
					}
					else//something messed up, so remove the key
					{
						oreSlagInQueue.remove(s);
					}
				}
				if(oreSlagInQueue.get(s) <= 0)
					oreSlagInQueue.remove(s);
			}
		}
		
		if(ret.isEmpty())
			ret = null;
		
		return ret;
	}*/
	
	private void addSlagToMap(String slag, int value)
	{
		if(oreSlagInQueue.containsKey(slag))
		{
			int ov = oreSlagInQueue.get(slag);
			oreSlagInQueue.put(slag, value+ov);
		}
		else
			oreSlagInQueue.put(slag, value);
	}
	
	//remove the successfully added finished job from the queue
	public boolean removeJob(MultiblockWorkQueueItem job)
	{
		if(job == null)
			return false;
		else
			return queue.remove(job);
	}
}
