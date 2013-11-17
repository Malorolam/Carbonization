package mal.carbonization.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.URI;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.tileentity.TileEntityTest;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class GuiTest extends GuiScreen
{
    private TileEntityTest test;
    
    private ItemStack[] input = new ItemStack[27];
    private ItemStack[] output = new ItemStack[27];
    private EntityPlayer player;

    public GuiTest(TileEntityTest par2TileEntityTest, EntityPlayer player)
    {
        this.test = par2TileEntityTest;
        this.player = player;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    	//System.out.println("init the gui");
        this.buttonList.clear();
        byte b0 = -16;
        
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        /*switch (par1GuiButton.id)
        {
            case 1:
                par1GuiButton.enabled = false;

                try
                {
	            	int xdiff = Integer.parseInt(xbox.getText());
	            	int ydiff = Integer.parseInt(ybox.getText());
	            	int zdiff = Integer.parseInt(zbox.getText());

                    this.test.closeGui(player, xdiff, ydiff, zdiff, true);
                    this.mc.displayGuiScreen((GuiScreen)null);
                    this.mc.setIngameFocus();
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }

                break;
            case 2:
            	try
            	{
	            	int xdiff = Integer.parseInt(xbox.getText());
	            	int ydiff = Integer.parseInt(ybox.getText());
	            	int zdiff = Integer.parseInt(zbox.getText());
	                
	                this.test.closeGui(player, xdiff, ydiff, zdiff, false);
	                this.mc.displayGuiScreen((GuiScreen)null);
	                this.mc.setIngameFocus();
	                //this.test.initilizeFunction(Integer.parseInt(xbox.getText()), Integer.parseInt(ybox.getText()), Integer.parseInt(zbox.getText()));
            	}
            	catch (Throwable t)
            	{
            		t.printStackTrace();
            	}
            	break;
        }*/
    }
    
    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
    	/*if(xbox.isFocused())
    		xbox.textboxKeyTyped(par1, par2);
    	if(ybox.isFocused())
    		ybox.textboxKeyTyped(par1, par2);
    	if(zbox.isFocused())
    		zbox.textboxKeyTyped(par1, par2);*/
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
  /*      super.mouseClicked(par1, par2, par3);
        this.xbox.mouseClicked(par1, par2, par3);
        this.ybox.mouseClicked(par1, par2, par3);
        this.zbox.mouseClicked(par1, par2, par3);*/
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    public void drawDefaultBackground()
    {
        super.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/multiblockFurnace.png"));
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 266);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        int k = (this.width - 248) / 2 + 10;
        int l = (this.height - 166) / 2 + 8;
        this.fontRenderer.drawString("The Official Carbonization Test Block:", k, l, 2039583);
        l += 12;
        GameSettings gamesettings = this.mc.gameSettings;
        String s = "Currently testing Multiblock Furnace Gui";
        this.fontRenderer.drawString(s, k, l, 5197647);
        
        super.drawScreen(par1, par2, par3);
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