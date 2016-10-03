package com.bafomdad.realfilingcabinet.api.common;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFilingCabinet extends ITileEntityProvider {

	public void leftClick(TileEntity tile, EntityPlayer player);
	
	public void rightClick(TileEntity tile, EntityPlayer player);
	
	public void entityCollisionInteraction(World world, BlockPos pos, IBlockState state, Entity entity);
	
//	public IProperty[] getIgnoredProperties();
}
