package mal.carbonization.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.tileentity.TileEntityStructureBlock;
import mal.core.reference.ColorReference;
import mal.core.util.MalLogger;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemStructureBlock extends Item{

	/**
	 * Item ID structure
	 * xyzz
	 * x: purpose, 0-2
	 * y: secondary material, 0-2
	 * zz: base material, 00-09
	 */
	
	public IIcon[] baseIcon = new IIcon[10];
	public IIcon[] secondaryIcon = new IIcon[3];
	public IIcon[] purposeIcon = new IIcon[3];
	
	public ItemStructureBlock() {
		super();
		this.hasSubtypes = true;
		this.setCreativeTab(carbonization.tabStructure);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		int[] value = deconstructDamage(is.getItemDamage());
		String line1 = "A ";
		String line2 = "made from ";
		String line3 = "with ";
		
		double[] d = calculateTiers(value[2], value[1]);
		String line4 = "Insulation Tier: "+String.format("%.2f",d[0])+" Conduction";
		String line5 = "Tier: "+String.format("%.2f",d[1]) + " Average: " + String.format("%.2f", (d[0]+d[1])/2);
		boolean flag = false;
		
		switch(value[0])
		{
		case 0:
			line1 += "structure block";
			break;
		case 1:
			line1 += "furnace structure block";
			break;
		case 2:
			line1 += "machine structure block";
			break;
		}
		
		switch(value[1])
		{
		case 1:
			line3 += "High Density Insulation.";
			flag = true;
			break;
		case 2:
			line3 += "Fine Threading.";
			flag = true;
			break;
		}
		
		switch(value[2])
		{
		case 0:
			line2 += "stone";
			break;
		case 1:
			line2 += "ice";
			break;
		case 2:
			line2 += "iron";
			break;
		case 3:
			line2 += "brick";
			break;
		case 4:
			line2 += "steel";
			break;
		case 5:
			line2 += "carbon fibre";
			break;
		case 6:
			line2 += "titanium";
			break;
		case 7:
			line2 += "carbon nanotube";
			break;
		case 8:
			line2 += "withered end";
			break;
		case 9:
			line2 += "cobalt chrome";
			break;
		}
		
		list.add(setTooltipData(line1, ColorReference.LIGHTGREEN));
		list.add(setTooltipData(line2, ColorReference.LIGHTGREEN));
		if(flag)
			list.add(setTooltipData(line3, ColorReference.LIGHTGREEN));
		
		//Extra tooltip information that is precise material specific
		switch(is.getItemDamage())
		{
		case 1://ice structure ids
		case 101:
		case 201:
		case 1001:
		case 1101:
		case 1201:
		case 2001:
		case 2101:
		case 2201:
			list.add(setTooltipData("Dwarven engineering allows ", ColorReference.ORANGE));
			list.add(setTooltipData("for unmelting ice, even in ", ColorReference.ORANGE));
			list.add(setTooltipData("the hottest of temperatures.", ColorReference.ORANGE));
			break;
		}
		
		list.add(setTooltipData(line4, ColorReference.DARKGREEN));
		list.add(setTooltipData(line5, ColorReference.DARKGREEN));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
	
	/*
	 * Calculate the tier values based on the materials
	 */
	private static double[] calculateTiers(int baseMaterial, int secondaryMaterial)
	{
		double InsTier = 0;
		double ConTier = 0;
		
		//get the base values
		switch(baseMaterial)
		{
		case 0:
			InsTier=1;
			ConTier=2;
			break;
		case 1:
			InsTier=2;
			ConTier=1;
			break;
		case 2:
			InsTier=2;
			ConTier=4;
			break;
		case 3:
			InsTier=4;
			ConTier=2;
			break;
		case 4:
			InsTier=3;
			ConTier=6;
			break;
		case 5:
			InsTier=6;
			ConTier=3;
			break;
		case 6:
			InsTier=4;
			ConTier=8;
			break;
		case 7:
			InsTier=8;
			ConTier=4;
			break;
		case 8:
			InsTier=5;
			ConTier=10;
			break;
		case 9:
			InsTier=10;
			ConTier=5;
			break;
		}
		
		//Apply secondary material modifiers
		switch(secondaryMaterial)
		{
		case 0:
			break;
		case 1:
			InsTier += InsTier;
			ConTier -= ConTier*0.5;
			break;
		case 2:
			InsTier -= InsTier*0.5;
			ConTier += ConTier;
			break;
		}
		
		double[] d = new double[2];
		d[0] = InsTier;
		d[1] = ConTier;
		return d;
	}
	
	public static int[] deconstructDamage(int damage)
	{
		//System.out.println("Deconstructed item damage: " + damage);
		int[] value = new int[3];
		
		while(damage >= 1000)
		{
			value[0] += 1;
			damage -= 1000;
		}
		while(damage >= 100)
		{
			value[1] += 1;
			damage -= 100;
		}
		value[2] = damage;
		
		return value;
	}
	
	/*
	 * d[0]=insulation
	 * d[1]=conduction
	 */
	public static double[] getTier(int damage)
	{
		int[] v = deconstructDamage(damage);
		
		return calculateTiers(v[2], v[1]);
	}
	
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	Block block = par3World.getBlock(par4, par5, par6);

        if (block == Blocks.snow_layer && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1)
        {
            par7 = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(par3World, par4, par5, par6))
        {
            if (par7 == 0)
            {
                --par5;
            }

            if (par7 == 1)
            {
                ++par5;
            }

            if (par7 == 2)
            {
                --par6;
            }

            if (par7 == 3)
            {
                ++par6;
            }

            if (par7 == 4)
            {
                --par4;
            }

            if (par7 == 5)
            {
                ++par4;
            }
        }

        if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else if (par5 == 255 && carbonizationBlocks.structureBlock.getMaterial().isSolid())
        {
            return false;
        }
        else if (par3World.canPlaceEntityOnSide(carbonizationBlocks.structureBlock, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
        {
            int i1 = this.getMetadata(par1ItemStack.getItemDamage());
            int j1 = carbonizationBlocks.structureBlock.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, i1);

            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, j1))
            {
                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), carbonizationBlocks.structureBlock.stepSound.func_150496_b(), (carbonizationBlocks.structureBlock.stepSound.getVolume() + 1.0F) / 2.0F, carbonizationBlocks.structureBlock.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side The side the player (or machine) right-clicked on.
     */
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
       if (!world.setBlock(x, y, z, carbonizationBlocks.structureBlock, 0, 3))
       {
           return false;
       }

       if (world.getBlock(x, y, z) == carbonizationBlocks.structureBlock)
       {
           carbonizationBlocks.structureBlock.onBlockPlacedBy(world, x, y, z, player, stack);
           carbonizationBlocks.structureBlock.onPostBlockPlaced(world, x, y, z, metadata);
           int[] value = this.deconstructDamage(stack.getItemDamage());
           TileEntity te = world.getTileEntity(x, y, z);
           
           if(te instanceof TileEntityStructureBlock)
           {
        	   ((TileEntityStructureBlock)te).materialInitilize(value[2], value[1], value[0]);
           }
       }

       return true;
    }

	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List)
    {
		for(int i = 0; i<purposeIcon.length; i++)
			for(int j = 0; j<1/*secondaryIcon.length*/; j++)
				for(int k = 0; k<baseIcon.length; k++)
				{
					par3List.add(new ItemStack(item, 1, i*1000+j*100+k));
				}
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        int[] value = this.deconstructDamage(damage);
        
        if(pass == 0)//base material
        {
        	return baseIcon[value[2]];
        }
        else if (pass == 1)//secondary material
        {
        	return secondaryIcon[value[1]];
        }
        else//purpose
        {
        	return purposeIcon[value[0]];
        }
    }
	
	public void registerIcons(IIconRegister ir)
	{
		for(int i = 0; i<10; i++)
		{
			String baseMatName = "carbonization:";
		
			switch(i)
			{
			case 0:
				baseMatName += "stone";
				break;
			case 1:
				baseMatName += "ice";
				break;
			case 2:
				baseMatName += "iron";
				break;
			case 3:
				baseMatName += "brick";
				break;
			case 4:
				baseMatName += "steel";
				break;
			case 5:
				baseMatName += "carbonFibre";
				break;
			case 6:
				baseMatName += "titanium";
				break;
			case 7:
				baseMatName += "carbonNanotube";
				break;
			case 8:
				baseMatName += "witheredEnd";
				break;
			case 9:
				baseMatName += "cobaltChrome";
				break;
			}
			baseMatName += "StructureTexture";
			
			baseIcon[i] = ir.registerIcon(baseMatName);
		}
		
		for(int i = 1; i < 3; i++)
		{
			String secondaryMatName = null;
			
			switch(i)
			{
			case 1:
				secondaryMatName = "carbonization:highDensityInsulationLayerTexture";
				break;
			case 2:
				secondaryMatName = "carbonization:fineThreadingLayerTexture";
				break;
			}
			
			if(secondaryMatName != null)
				secondaryIcon[i] = ir.registerIcon(secondaryMatName);
		}
		
		for(int i = 1; i<3; i++)
		{
			String purposeName = null;
			switch(i)
			{
			case 1:
				purposeName = "carbonization:furnaceLayerTexture";
				break;
			case 2:
				purposeName = "carbonization:machineLayerTexture";
				break;
			}
	
			if(purposeName != null)
				purposeIcon[i] = ir.registerIcon(purposeName);
		}
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) 
	{
		int[] value = deconstructDamage(is.getItemDamage());
		String s = this.getUnlocalizedName();
		
		switch(value[0])
		{
		case 0:
			s += "structure";
			break;
		case 1:
			s+= "furnace";
			break;
		case 2:
			s+= "machine";
			break;
		}
		
		switch(value[1])
		{
		case 1:
			s += "highdensity";
			break;
		case 2:
			s += "fine";
			break;
		}
		
		switch(value[2])
		{
		case 0:
			s += "stone";
			break;
		case 1:
			s += "ice";
			break;
		case 2:
			s += "iron";
			break;
		case 3:
			s += "brick";
			break;
		case 4:
			s += "steel";
			break;
		case 5:
			s += "carbonfibre";
			break;
		case 6:
			s += "titanium";
			break;
		case 7:
			s += "carbonnanotube";
			break;
		case 8:
			s += "witheredend";
			break;
		case 9:
			s += "cobaltchrome";
			break;
		}
		
		return s;
	}
	
	public String getItemNameIS(ItemStack is) 
	{
		return getUnlocalizedName(is);
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