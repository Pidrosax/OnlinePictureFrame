package com.creativemd.opf.gui;

import com.creativemd.creativecore.common.gui.premade.SubContainerTileEntity;
import com.creativemd.opf.OPFrame;
import com.creativemd.opf.block.TileEntityPicFrame;
import com.creativemd.opf.little.LittleOpFrame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Optional.Method;

public class SubContainerPic extends SubContainerTileEntity {
	
	public TileEntityPicFrame frame;
	public Object tile;
	
	public SubContainerPic(TileEntityPicFrame frame, EntityPlayer player, Object tile) {
		super(player, frame);
		this.frame = frame;
		this.tile = tile;
	}
	
	public SubContainerPic(TileEntityPicFrame frame, EntityPlayer player) {
		this(frame, player, null);
	}
	
	@Override
	public boolean shouldTick() {
		return false;
	}
	
	@Override
	public void createControls() {
		
	}
	
	@Override
	public void onPacketReceive(NBTTagCompound nbt) {
		int type = nbt.getInteger("type");
		if (type == 0) {
			String url = nbt.getString("url");
			if (OPFrame.CONFIG.canUse(player, url)) {
				frame.url = url;
				frame.sizeX = (float) Math.min(OPFrame.CONFIG.sizeLimitation, nbt.getFloat("x"));
				frame.sizeY = (float) Math.min(OPFrame.CONFIG.sizeLimitation, nbt.getFloat("y"));
				
				frame.renderDistance = Math.min(OPFrame.CONFIG.maxRenderDistance, nbt.getInteger("render"));
				frame.posX = nbt.getByte("posX");
				frame.posY = nbt.getByte("posY");
				frame.rotation = nbt.getByte("rotation");
				frame.visibleFrame = nbt.getBoolean("visibleFrame");
				frame.flippedX = nbt.getBoolean("flippedX");
				frame.flippedY = nbt.getBoolean("flippedY");
				
				frame.rotationX = nbt.getFloat("rotX");
				frame.rotationY = nbt.getFloat("rotY");
				
				frame.transparency = nbt.getFloat("transparency");
				frame.brightness = nbt.getFloat("brightness");
				
				if (nbt.hasKey("facing")) {
					setFacing(EnumFacing.getFront(nbt.getInteger("facing")));
				}
			}
			
			frame.updateBlock();
		}
	}
	
	@Method(modid = "littletiles")
	public void setFacing(EnumFacing facing) {
		if (tile != null) {
			((LittleOpFrame) tile).setMeta(facing.getIndex());
			((LittleOpFrame) tile).sendUpdatePacketToClient();
		}
	}
	
}
