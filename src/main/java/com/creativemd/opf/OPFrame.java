package com.creativemd.opf;

import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.creativecore.common.utils.sorting.BlockSelector.BlockSelectorBlock;
import com.creativemd.littletiles.client.gui.handler.LittleGuiHandler;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.registry.LittleTileRegistry;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.ingredient.rules.BlockIngredientRule.BlockIngredientRuleFixedBlock;
import com.creativemd.littletiles.common.util.ingredient.rules.IngredientRules;
import com.creativemd.opf.block.BlockLittlePicFrame;
import com.creativemd.opf.block.BlockPicFrame;
import com.creativemd.opf.block.TileEntityPicFrame;
import com.creativemd.opf.client.OPFrameClient;
import com.creativemd.opf.gui.SubContainerPic;
import com.creativemd.opf.gui.SubGuiPic;
import com.creativemd.opf.little.LittleOpFrame;
import com.creativemd.opf.little.LittleOpPreview;
import com.creativemd.opf.little.LittlePlacedOpFrame;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = OPFrame.modid, version = OPFrame.version, name = "OnlinePictureFrame", acceptedMinecraftVersions = "", dependencies = "required-before:creativecore", guiFactory = "com.creativemd.opf.OpFrameSettings")
@Mod.EventBusSubscriber
public class OPFrame {
	
	public static final String modid = "opframe";
	public static final String version = "1.4.0";
	
	public static Block frame = new BlockPicFrame().setUnlocalizedName("opFrame").setRegistryName("opFrame");
	public static Block littleFrame;
	
	public static OPFrameConfig CONFIG;
	
	@SideOnly(Side.CLIENT)
	public static void initClient() {
		OPFrameClient.initClient();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(OPFrame.class);
		
		if (Loader.isModLoaded("littletiles"))
			loadLittleTiles();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(frame);
		if (littleFrame != null)
			event.getRegistry().register(littleFrame);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new ItemBlock(frame).setRegistryName(frame.getRegistryName()));
		if (littleFrame != null)
			event.getRegistry().register(new ItemBlock(littleFrame).setRegistryName(littleFrame.getRegistryName()));
		
		if (FMLCommonHandler.instance().getSide().isClient())
			initClient();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		CreativeConfigRegistry.ROOT.registerValue(modid, CONFIG = new OPFrameConfig());
		
		GameRegistry.registerTileEntity(TileEntityPicFrame.class, "OPFrameTileEntity");
	}
	
	@Method(modid = "littletiles")
	public void loadLittleTiles() {
		littleFrame = new BlockLittlePicFrame().setUnlocalizedName("littleOpFrame").setRegistryName("littleOpFrame");
		
		GuiHandler.registerGuiHandler("littleOpFrame", new LittleGuiHandler() {
			
			@Override
			public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleTile tile) {
				if (tile instanceof LittleOpFrame)
					return new SubContainerPic((TileEntityPicFrame) ((LittleOpFrame) tile).getTileEntity(), player, tile);
				return null;
			}
			
			@Override
			@SideOnly(Side.CLIENT)
			public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleTile tile) {
				if (tile instanceof LittleOpFrame)
					return new SubGuiPic((TileEntityPicFrame) ((LittleOpFrame) tile).getTileEntity(), true, LittleGridContext.get().size);
				return null;
			}
			
		});
		
		LittleTileRegistry.registerTileType(LittleOpFrame.class, "OpFrame", (x) -> false, true);
		
		IngredientRules.registerBlockRule(new BlockSelectorBlock(littleFrame), new BlockIngredientRuleFixedBlock(littleFrame, 0));
		
		LittlePreview.registerPreviewType("opPreview", LittleOpPreview.class);
		LittlePreview.registerPreviewType("opPlacedPreview", LittlePlacedOpFrame.class);
		
	}
}
