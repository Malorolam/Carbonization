package mal.carbonization.multiblock;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * keeps track of the information for a single job
 * 
 */
public class MultiblockWorkQueueItem {

	public ItemStack input = new ItemStack(0,0,0);//input item
	public int fuelTick;//amount of fuel per tick it takes to complete the job
	public int cookTime;//amount of tick it takes to complete the job
	public int maxCookTime;//maximum time to complete a job
	public int slagYield;//the amount of specific slag (in millibuckets) the item yields
	public String oreSlagType;//the kind of slag produced
	public boolean processing = true;
	
	public MultiblockWorkQueueItem(ItemStack input, int fuelTick, int cookTime, int maxCookTime, String oreSlagType, int oreSlagYield)
	{
		this.input = input;
		this.fuelTick = fuelTick;
		this.cookTime = cookTime;
		this.maxCookTime = maxCookTime;
		this.oreSlagType = oreSlagType;
		this.slagYield = oreSlagYield;
	}
	
	public MultiblockWorkQueueItem()
	{
		
	}
	
	public void generateNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("fuelTick", fuelTick);
		nbt.setInteger("cookTime", cookTime);
		nbt.setInteger("maxCookTime", maxCookTime);
		nbt.setString("type", oreSlagType);
		nbt.setInteger("slagYield", slagYield);
		input.writeToNBT(nbt);
		System.out.println("Saved Item: "+ input.toString() + " (" + cookTime + "/" + maxCookTime + ")");
	}
	
	public void retreiveNBT(NBTTagCompound nbt)
	{
		this.fuelTick = nbt.getInteger("fuelTick");
		this.cookTime = nbt.getInteger("cookTime");
		this.maxCookTime = nbt.getInteger("maxCookTime");
		this.oreSlagType = nbt.getString("type");
		this.slagYield = nbt.getInteger("slagYield");
		input.readFromNBT(nbt);
		System.out.println("Recovered Item: "+ input.toString() + " (" + cookTime + "/" + maxCookTime + ")");
	}
	
	public boolean tickItem()
	{
		//System.out.println("Cook Time for item: " + input + ": " + cookTime);
		if(cookTime > 0)
		{
			cookTime--;
			return false;
		}
		else
		{
			processing = false;
			return true;
		}
	}
}
