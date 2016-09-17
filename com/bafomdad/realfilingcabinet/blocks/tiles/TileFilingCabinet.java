package com.bafomdad.realfilingcabinet.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileFilingCabinet extends TileEntity {
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		
		super.writeToNBT(tag);
		writeCustomNBT(tag);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		
		super.readFromNBT(tag);
		readCustomNBT(tag);
	}
	
	public void writeCustomNBT(NBTTagCompound tag) {}
	
	public void readCustomNBT(NBTTagCompound tag) {}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		
		NBTTagCompound tag = new NBTTagCompound();
		this.writeCustomNBT(tag);
		return new SPacketUpdateTileEntity(getPos(), 1, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		
		if (packet != null && packet.getNbtCompound() != null)
			readCustomNBT(packet.getNbtCompound());
		
		if (getWorld().isRemote) {
			markBlockForRenderUpdate();
		}
	}
	
	public void markBlockForUpdate() {
		
		IBlockState state = worldObj.getBlockState(pos);
		worldObj.notifyBlockUpdate(pos, state, state, 3);
	}
	
	public void markBlockForRenderUpdate() {
		
		worldObj.markBlockRangeForRenderUpdate(pos, pos);
	}
	
//	@Override
//    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
//    	
//    	return false;
//    }
}