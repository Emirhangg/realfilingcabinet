package com.bafomdad.realfilingcabinet.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.bafomdad.realfilingcabinet.api.UpgradeHelper;
import com.bafomdad.realfilingcabinet.blocks.tiles.TileEntityRFC;
import com.bafomdad.realfilingcabinet.helpers.StringLibs;
import com.bafomdad.realfilingcabinet.init.RFCItems;
import com.bafomdad.realfilingcabinet.items.ItemFolder;
import com.bafomdad.realfilingcabinet.network.RFCPacketHandler;
import com.bafomdad.realfilingcabinet.network.RFCTileMessage;

public class EnderUtils {

	public static void syncToFolder(TileEntityRFC tile, ItemStack stack, int index) {
		
		ItemStack folder = tile.getInventory().getTrueStackInSlot(index);
		long folderSize = ItemFolder.getFileSize(folder);
		
		if (folderSize != ItemFolder.getFileSize(stack))
			ItemFolder.setFileSize(stack, folderSize);
	}
	
	public static void syncToFolder(TileEntityRFC tile, int dim, int index, int extractedAmount) {
		
		ItemStack folder = tile.getInventory().getTrueStackInSlot(index);
		if (folder.getItem() != RFCItems.folder)
			return;
		
		RFCPacketHandler.INSTANCE.sendToServer(new RFCTileMessage(tile.getPos(), dim, extractedAmount, index));
	}
	
	public static int syncRecipeOutput(ItemStack folder, ItemStack output) {
		
		long size = ItemFolder.getFileSize(folder);
		long extract = Math.min(output.getMaxStackSize(), size);
		
		return output.stackSize = (int)extract;
	}
	
	public static void syncRecipes(TileEntityRFC tile, ItemStack folder, ItemStack stack) {
		
		updateTileOutput(tile, NBTUtils.getInt(folder, StringLibs.RFC_SLOTINDEX, 0));
	}
	
	private static void updateTileOutput(TileEntityRFC tile, int index) {
		
		ItemStack folder = tile.getInventory().getTrueStackInSlot(index);
		long folderSize = ItemFolder.getFileSize(folder);
		
		if (folderSize > 64)
			return;
		
		else 
		{
			if (folderSize == 0)
			{
				tile.getInventory().setInventorySlotContents(9, null);
				return;
			}
			ItemStack newStack = new ItemStack(((ItemStack)ItemFolder.getObject(folder)).getItem(), (int)folderSize, ((ItemStack)ItemFolder.getObject(folder)).getItemDamage());
			tile.getInventory().setInventorySlotContents(9, newStack);
		}
	}
	
	public static ItemStack createEnderFolder(TileEntityRFC tile, EntityPlayer player, ItemStack stack) {
		
		NBTTagCompound playerTag = player.getEntityData();
		if (!playerTag.hasKey(StringLibs.RFC_SLOTINDEX))
			playerTag.setInteger(StringLibs.RFC_SLOTINDEX, 0);
		
		ItemStack enderFolder = stack.copy();
		enderFolder.setItemDamage(1);
		NBTUtils.setInt(enderFolder, StringLibs.RFC_SLOTINDEX, playerTag.getInteger(StringLibs.RFC_SLOTINDEX));
		setTileLoc(tile, enderFolder);
		
		return enderFolder;
	}
	
	public static void setTileLoc(TileEntityRFC tile, ItemStack stack) {
		
		BlockPos pos = tile.getPos();
		int dim = tile.getWorld().provider.getDimension();
		
		NBTUtils.setCompound(stack, StringLibs.RFC_TILEPOS, new NBTTagCompound());
		NBTTagCompound posTag = NBTUtils.getCompound(stack, StringLibs.RFC_TILEPOS, true);
		
		posTag.setInteger("X", pos.getX());
		posTag.setInteger("Y", pos.getY());
		posTag.setInteger("Z", pos.getZ());
		
		NBTUtils.setInt(stack, StringLibs.RFC_DIM, dim);
	}
	
	public static TileEntityRFC getTileLoc(ItemStack stack) {
		
		NBTTagCompound posTag = NBTUtils.getCompound(stack, StringLibs.RFC_TILEPOS, true);
		
		if (posTag != null)
		{
			int x = posTag.getInteger("X");
			int y = posTag.getInteger("Y");
			int z = posTag.getInteger("Z");
			int dim = NBTUtils.getInt(stack, StringLibs.RFC_DIM, 0);
			
			return findLoadedTileEntityInWorld(new BlockPos(x, y, z), dim);
		}
		return null;
	}
	
	public static TileEntityRFC findLoadedTileEntityInWorld(BlockPos pos, int dim) {
		
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		for (WorldServer world : server.worldServers){
			for (Object obj : world.loadedTileEntityList) {
				if (obj instanceof TileEntityRFC)
				{
					if (world.provider.getDimension() == dim && UpgradeHelper.getUpgrade((TileEntityRFC)obj, StringLibs.TAG_ENDER) != null)
						return (TileEntityRFC)world.getTileEntity(pos);
				}
			}
		}
		return null;
	}
	
	public static void extractEnderFolder(TileEntityRFC tile, EntityPlayer player) {
		
		NBTTagCompound playerTag = player.getEntityData();
		if (playerTag.hasKey(StringLibs.RFC_SLOTINDEX))
		{
			int index = playerTag.getInteger(StringLibs.RFC_SLOTINDEX);
			ItemStack folder = tile.getInventory().getTrueStackInSlot(index);
			
			if (folder == null) {
				if (findNextFolder(tile, index) == -1)
				{
					folder = tile.getInventory().getTrueStackInSlot(0);
					playerTag.setInteger(StringLibs.RFC_SLOTINDEX, 0);
					index = 0;
				}
				index = findNextFolder(tile, index);
				folder = tile.getInventory().getTrueStackInSlot(index);
				playerTag.setInteger(StringLibs.RFC_SLOTINDEX, index);
			}
			ItemStack newFolder = createEnderFolder(tile, player, folder);
			player.setHeldItem(EnumHand.MAIN_HAND, newFolder);
			playerTag.setInteger(StringLibs.RFC_SLOTINDEX, index += 1);
		}
		else
		{
			for (int i = 0; i < tile.getInventory().getFolderInventory(); i++) {
				ItemStack folder = tile.getInventory().getTrueStackInSlot(i);
				if (folder != null)
				{
					ItemStack newFolder = createEnderFolder(tile, player, folder);
					player.setHeldItem(EnumHand.MAIN_HAND, newFolder);
					break;
				}
			}
		}
	}
	
	private static int findNextFolder(TileEntityRFC tile, int slot) {
		
		int index = -1;
		for (int i = slot; i < tile.getInventory().getFolderInventory(); i++) {
			ItemStack stack = tile.getInventory().getStackInSlot(i);
			if (stack != null) {
				index = i;
				break;
			}
		}
		return index;
	}
}