package mal.carbonization.gui;

import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.URI;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
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
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class GuiMultiblockInit extends GuiScreen
{
    private TileEntityMultiblockInit test;
    
    private GuiTextField xbox;
    private GuiTextField ybox;
    private GuiTextField zbox;
    private EntityPlayer player;
    private GuiButton closeBtn;
    private GuiButton makeBtn;

    public GuiMultiblockInit(TileEntityMultiblockInit par2TileEntityTest, EntityPlayer player)
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
        this.buttonList.add(this.makeBtn = new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + b0, 114, 20, "Make it so."));
        this.buttonList.add(this.closeBtn = new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + b0, 114, 20, "Close"));
        
        this.xbox = new GuiTextField(this.fontRenderer, (this.width - 248) / 2 + 50, (this.height - 166) / 2 + 35, 60 , 20);
        this.xbox.setMaxStringLength(2);
        this.xbox.setFocused(true);
        this.xbox.setText(String.valueOf(this.test.xdiff));
        this.ybox = new GuiTextField(this.fontRenderer, (this.width - 248) / 2 + 50, (this.height - 166) / 2 + 65, 60, 20);
        this.ybox.setMaxStringLength(2);
        this.ybox.setFocused(false);
        this.ybox.setText(String.valueOf(this.test.ydiff));
        this.zbox = new GuiTextField(this.fontRenderer, (this.width - 248) / 2 + 50, (this.height - 166) / 2 + 95, 60, 20);
        this.zbox.setMaxStringLength(2);
        this.zbox.setFocused(false);
        this.zbox.setText(String.valueOf(this.test.zdiff));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 1:
                par1GuiButton.enabled = false;

                try
                {
                	int xdiff = 0;
                	try
                	{
                		xdiff = Integer.parseInt(xbox.getText());
                	}
                	catch (Throwable t)
                	{
                		
                	}
                	if(xdiff >9)
                		xdiff = 9;
                	if(xdiff <3)
                		xdiff = 3;
	            	
                	int ydiff = 0;
                	try
                	{
                		ydiff = Integer.parseInt(ybox.getText());
                	}
                	catch(Throwable t)
                	{
                		
                	}
                	if(ydiff >6)
                		ydiff = 6;
                	if(ydiff <3)
                		ydiff = 3;
	            	
                	int zdiff = 0;
                	try
                	{
                		zdiff = Integer.parseInt(zbox.getText());
                	}
                	catch(Throwable t)
                	{
                		
                	}
                	if(zdiff >9)
                		zdiff = 9;
                	if(zdiff <3)
                		zdiff = 3;

                    this.test.closeGui(player, xdiff, ydiff, zdiff, true);
                    this.mc.displayGuiScreen((GuiScreen)null);
                    this.mc.setIngameFocus();
                }
                catch (Throwable throwable)
                {
                    //throwable.printStackTrace();
                }

                break;
            case 2:
            	try
            	{
            		int xdiff = 0;
                	try
                	{
                		xdiff = Integer.parseInt(xbox.getText());
                	}
                	catch (Throwable t)
                	{
                		
                	}
	            	
                	int ydiff = 0;
                	try
                	{
                		ydiff = Integer.parseInt(ybox.getText());
                	}
                	catch(Throwable t)
                	{
                		
                	}
	            	
                	int zdiff = 0;
                	try
                	{
                		zdiff = Integer.parseInt(zbox.getText());
                	}
                	catch(Throwable t)
                	{
                		
                	}

                    this.test.closeGui(player, xdiff, ydiff, zdiff, false);
                    this.mc.displayGuiScreen((GuiScreen)null);
                    this.mc.setIngameFocus();
            	}
            	catch (Throwable t)
            	{
            		//t.printStackTrace();
            	}
            	break;
        }
    }
    
    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
    	if(par1==KeyEvent.VK_TAB)
    	{
    		if(xbox.isFocused())
    		{
    			xbox.setFocused(false);
    			ybox.setFocused(true);
    		}
    		else if(ybox.isFocused())
        	{
    			ybox.setFocused(false);
    			zbox.setFocused(true);
    		}
    		else if(zbox.isFocused())
        	{
    			zbox.setFocused(false);
    			xbox.setFocused(true);
    		}
    		else
    		{
    			xbox.setFocused(true);
    			ybox.setFocused(false);
    			zbox.setFocused(false);
    		}
    	}
    	
    	if(par1==KeyEvent.VK_ESCAPE)
    		this.actionPerformed(this.closeBtn);
    	
    	if(par1==KeyEvent.VK_ENTER)
    		this.actionPerformed(this.makeBtn);
    	
    	if(xbox.isFocused())
    		if(par1 <= KeyEvent.VK_9 && par1 >= KeyEvent.VK_0 || par1 == KeyEvent.VK_BACK_SPACE)
    			xbox.textboxKeyTyped(par1, par2);
    	if(ybox.isFocused())
    		if(par1 <= KeyEvent.VK_9 && par1 >= KeyEvent.VK_0 || par1 == KeyEvent.VK_BACK_SPACE)
    			ybox.textboxKeyTyped(par1, par2);
    	if(zbox.isFocused())
    		if(par1 <= KeyEvent.VK_9 && par1 >= KeyEvent.VK_0 || par1 == KeyEvent.VK_BACK_SPACE)
    			zbox.textboxKeyTyped(par1, par2);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.xbox.mouseClicked(par1, par2, par3);
        this.ybox.mouseClicked(par1, par2, par3);
        this.zbox.mouseClicked(par1, par2, par3);
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
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/multiblock.png"));
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
        this.fontRenderer.drawString("Multiblock Initilization:", k, l, 2039583);
        l += 12;
        GameSettings gamesettings = this.mc.gameSettings;
        String s = "Min. Size: 3x3x3;  Max. Size: 9x6x9";
        this.fontRenderer.drawString(s, k, l, 5197647);
        this.fontRenderer.drawString("X Size:", k, l+20, 2039583);
        this.fontRenderer.drawString("Y Size:", k, l+50, 2039583);
        this.fontRenderer.drawString("Z Size:", k, l+80, 2039583);
        this.xbox.drawTextBox();
        this.ybox.drawTextBox();
        this.zbox.drawTextBox();
        
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