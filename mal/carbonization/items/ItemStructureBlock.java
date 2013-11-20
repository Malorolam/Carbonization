package mal.carbonization.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.carbonization.ColorReference;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityStructureBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemStructureBlock extends Item{

	/**
	 * Item ID structure
	 * xyzz
	 * x: purpose, 0-2
	 * y: secondary material, 0-4
	 * zz: base material, 00-19
	 */
	
	public Icon[] baseIcon = new Icon[19];
	public Icon[] secondaryIcon = new Icon[5];
	public Icon[] purposeIcon = new Icon[3];
	
	public ItemStructureBlock(int par1) {
		super(par1);
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
			line3 += "Basic Insulation.";
			flag = true;
			break;
		case 2:
			line3 += "High Density Insulation.";
			flag = true;
			break;
		case 3:
			line3 += "Coarse Threading.";
			flag = true;
			break;
		case 4:
			line3 += "Fine Threading.";
			flag = true;
			break;
		}
		
		switch(value[2])
		{
		case 0:
			line2 += "ice";
			break;
		case 1:
			line2 += "wood";
			break;
		case 2:
			line2 += "stone";
			break;
		case 3:
			line2 += "iron";
			break;
		case 4:
			line2 += "carbon flake";
			break;
		case 5:
			line2 += "carbon-plated iron";
			break;
		case 6:
			line2 += "refined iron";
			break;
		case 7:
			line2 += "carbon thread";
			break;
		case 8:
			line2 += "carbon rebar";
			break;
		case 9:
			line2 += "pig iron";
			break;
		case 10:
			line2 += "carbon fibre";
			break;
		case 11:
			line2 += "refined carbon fibre";
			break;
		case 12:
			line2 += "high carbon steel";
			break;
		case 13:
			line2 += "carbon nanoflake";
			break;
		case 14:
			line2 += "carbon-plated steel";
			break;
		case 15:
			line2 += "steel";
			break;
		case 16:
			line2 += "carbon nanotube";
			break;
		case 17:
			line2 += "refined carbon nanotube";
			break;
		case 18:
			line2 += "withered end";
			break;
		}
		
		list.add(setTooltipData(line1, ColorReference.LIGHTGREEN));
		list.add(setTooltipData(line2, ColorReference.LIGHTGREEN));
		if(flag)
			list.add(setTooltipData(line3, ColorReference.LIGHTGREEN));
		
		//Extra tooltip information that is precise material specific
		switch(is.getItemDamage())
		{
		case 0://ice structure ids
		case 100:
		case 200:
		case 300:
		case 400:
		case 1000:
		case 1100:
		case 1200:
		case 1300:
		case 1400:
		case 2000:
		case 2100:
		case 2200:
		case 2300:
		case 2400:
			list.add(setTooltipData("Dwarven engineering allows ", ColorReference.ORANGE));
			list.add(setTooltipData("for unmelting ice, even in ", ColorReference.ORANGE));
			list.add(setTooltipData("the hottest of temperatures.", ColorReference.ORANGE));
			break;
			
		case 1://wood structure ids
		case 101:
		case 201:
		case 301:
		case 401:
		case 1001:
		case 1101:
		case 1201:
		case 1301:
		case 1401:
		case 2001:
		case 2101:
		case 2201:
		case 2301:
		case 2401:
			list.add(setTooltipData("Dwarven engineering allows ", ColorReference.ORANGE));
			list.add(setTooltipData("for nonflammable wood, even", ColorReference.ORANGE));
			list.add(setTooltipData("in the hottest of temperatures.", ColorReference.ORANGE));
			break;
			
		case 2009://pig iron machine, any insulation
		case 2109:
		case 2209:
		case 2309:
		case 2409:
			list.add(setTooltipData("A machine that is appropriate", ColorReference.PINK));
			list.add(setTooltipData("for those in the Suidae family.", ColorReference.PINK));
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
			ConTier=0;
			break;
		case 1:
			InsTier=0.5;
			ConTier=0.5;
			break;
		case 2:
			InsTier=0;
			ConTier=1;
			break;
		case 3:
			InsTier=0.5;
			ConTier=1.5;
			break;
		case 4:
			InsTier=1.5;
			ConTier=0.5;
			break;
		case 5:
			InsTier=1;
			ConTier=1;
			break;
		case 6:
			InsTier=1.5;
			ConTier=2.5;
			break;
		case 7:
			InsTier=2.5;
			ConTier=1.5;
			break;
		case 8:
			InsTier=2;
			ConTier=2;
			break;
		case 9:
			InsTier=2.5;
			ConTier=3.5;
			break;
		case 10:
			InsTier=3.5;
			ConTier=2.5;
			break;
		case 11:
			InsTier=3;
			ConTier=3;
			break;
		case 12:
			InsTier=3.5;
			ConTier=4.5;
			break;
		case 13:
			InsTier=4.5;
			ConTier=3.5;
			break;
		case 14:
			InsTier=4;
			ConTier=4;
			break;
		case 15:
			InsTier=4.5;
			ConTier=5.5;
			break;
		case 16:
			InsTier=5.5;
			ConTier=4.5;
			break;
		case 17:
			InsTier=5;
			ConTier=5;
			break;
		case 18:
			InsTier=10;
			ConTier=10;
			break;
		}
		
		//Apply secondary material modifiers
		switch(secondaryMaterial)
		{
		case 0:
			break;
		case 1:
			InsTier += InsTier*0.6;
			ConTier -= ConTier*0.5;
			break;
		case 2:
			InsTier += InsTier;
			ConTier -= ConTier*0.5;
			break;
		case 3:
			ConTier += ConTier*0.6;
			InsTier -= InsTier*0.5;
			break;
		case 4:
			ConTier += ConTier;
			InsTier -= InsTier*0.5;
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
        int i1 = par3World.getBlockId(par4, par5, par6);

        if (i1 == Block.snow.blockID && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1)
        {
            par7 = 1;
        }
        else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
                && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6)))
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
        else if (par5 == 255 && Block.blocksList[carbonization.structure.blockID].blockMaterial.isSolid())
        {
            return false;
        }
        else if (par3World.canPlaceEntityOnSide(carbonization.structure.blockID, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
        {
            Block block = Block.blocksList[carbonization.structure.blockID];
            int j1 = this.getMetadata(par1ItemStack.getItemDamage());
            int k1 = Block.blocksList[carbonization.structure.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, j1);

            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, k1))
            {
                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
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
       if (!world.setBlock(x, y, z, carbonization.structure.blockID, 0, 3))
       {
           return false;
       }

       if (world.getBlockId(x, y, z) == carbonization.structure.blockID)
       {
           Block.blocksList[carbonization.structure.blockID].onBlockPlacedBy(world, x, y, z, player, stack);
           Block.blocksList[carbonization.structure.blockID].onPostBlockPlaced(world, x, y, z, metadata);
           int[] value = this.deconstructDamage(stack.getItemDamage());
           TileEntity te = world.getBlockTileEntity(x, y, z);
           
           if(te instanceof TileEntityStructureBlock)
           {
        	   ((TileEntityStructureBlock)te).initilize(value[2], value[1], value[0]);
           }
       }

       return true;
    }

	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for(int i = 0; i<3; i++)
			for(int j = 0; j<5; j++)
				for(int k = 0; k<19; k++)
				{
					par3List.add(new ItemStack(par1, 1, i*1000+j*100+k));
				}
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public Icon getIconFromDamageForRenderPass(int damage, int pass)
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
	
	public void registerIcons(IconRegister ir)
	{
		for(int i = 0; i<19; i++)
		{
			String baseMatName = "carbonization:";
		
			switch(i)
			{
			case 0:
				baseMatName += "ice";
				break;
			case 1:
				baseMatName += "wood";
				break;
			case 2:
				baseMatName += "stone";
				break;
			case 3:
				baseMatName += "iron";
				break;
			case 4:
				baseMatName += "carbonFlake";
				break;
			case 5:
				baseMatName += "cIron";
				break;
			case 6:
				baseMatName += "refIron";
				break;
			case 7:
				baseMatName += "carbonThread";
				break;
			case 8:
				baseMatName += "cRebar";
				break;
			case 9:
				baseMatName += "pigIron";
				break;
			case 10:
				baseMatName += "carbonFibre";
				break;
			case 11:
				baseMatName += "refCarbonFibre";
				break;
			case 12:
				baseMatName += "hcSteel";
				break;
			case 13:
				baseMatName += "carbonNanoflake";
				break;
			case 14:
				baseMatName += "cSteel";
				break;
			case 15:
				baseMatName += "steel";
				break;
			case 16:
				baseMatName += "carbonNanotube";
				break;
			case 17:
				baseMatName += "refCarbonNanotube";
				break;
			case 18:
				baseMatName += "witheredEnd";
				break;
			}
			baseMatName += "StructureTexture";
			
			baseIcon[i] = ir.registerIcon(baseMatName);
		}
		
		for(int i = 1; i < 5; i++)
		{
			String secondaryMatName = null;
			
			switch(i)
			{
			case 1:
				secondaryMatName = "carbonization:basicInsulationLayerTexture";
				break;
			case 2:
				secondaryMatName = "carbonization:highDensityInsulationLayerTexture";
				break;
			case 3:
				secondaryMatName = "carbonization:coarseThreadingLayerTexture";
				break;
			case 4:
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
			s += "basic";
			break;
		case 2:
			s += "highdensity";
			break;
		case 3:
			s += "coarse";
			break;
		case 4:
			s += "fine";
			break;
		}
		
		switch(value[2])
		{
		case 0:
			s += "ice";
			break;
		case 1:
			s += "wood";
			break;
		case 2:
			s += "stone";
			break;
		case 3:
			s += "iron";
			break;
		case 4:
			s += "cflake";
			break;
		case 5:
			s += "ciron";
			break;
		case 6:
			s += "refiron";
			break;
		case 7:
			s += "cthread";
			break;
		case 8:
			s += "crebar";
			break;
		case 9:
			s += "pigiron";
			break;
		case 10:
			s += "cfibre";
			break;
		case 11:
			s += "refcfibre";
			break;
		case 12:
			s += "hcsteel";
			break;
		case 13:
			s += "cnanoflake";
			break;
		case 14:
			s += "csteel";
			break;
		case 15:
			s += "steel";
			break;
		case 16:
			s += "cnanotube";
			break;
		case 17:
			s += "refcnanotube";
			break;
		case 18:
			s += "witheredend";
			break;
		}
		
		return s;
	}
	
	public String getItemNameIS(ItemStack is) 
	{
		int[] value = deconstructDamage(is.getItemDamage());
		String s = "";
		
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
			s += "basic";
			break;
		case 2:
			s += "highdensity";
			break;
		case 3:
			s += "coarse";
			break;
		case 4:
			s += "fine";
			break;
		}
		
		switch(value[2])
		{
		case 0:
			s += "ice";
			break;
		case 1:
			s += "wood";
			break;
		case 2:
			s += "stone";
			break;
		case 3:
			s += "iron";
			break;
		case 4:
			s += "cflake";
			break;
		case 5:
			s += "ciron";
			break;
		case 6:
			s += "refiron";
			break;
		case 7:
			s += "cthread";
			break;
		case 8:
			s += "crebar";
			break;
		case 9:
			s += "pigiron";
			break;
		case 10:
			s += "cfibre";
			break;
		case 11:
			s += "refcfibre";
			break;
		case 12:
			s += "hcsteel";
			break;
		case 13:
			s += "cnanoflake";
			break;
		case 14:
			s += "csteel";
			break;
		case 15:
			s += "steel";
			break;
		case 16:
			s += "cnanotube";
			break;
		case 17:
			s += "refcnanotube";
			break;
		case 18:
			s += "witheredend";
			break;
		}
		
		return s;
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