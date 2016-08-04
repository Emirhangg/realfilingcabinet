package com.bafomdad.realfilingcabinet.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bafomdad.realfilingcabinet.RealFilingCabinet;
import com.bafomdad.realfilingcabinet.items.ItemFolder;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public class FolderTapeRecipe extends ShapelessRecipes implements IRecipe {
	
	public static List inputs = new ArrayList();
	
	static
	{
		inputs.add(new ItemStack(RealFilingCabinet.itemFolder, 1, 0));
		inputs.add(new ItemStack(RealFilingCabinet.itemWhiteoutTape));
	}

	public FolderTapeRecipe() {
		
		super(new ItemStack(RealFilingCabinet.itemEmptyFolder), inputs);
	}
	
	@Override
	public boolean matches(InventoryCrafting ic, World world) {
		
		ArrayList list = new ArrayList(this.inputs);
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				
				ItemStack stack = ic.getStackInRowAndColumn(j, i);
				if (stack != null)
				{
					boolean flag = false;
					Iterator iter = list.iterator();
					
					while (iter.hasNext())
					{
						ItemStack stack1 = (ItemStack)iter.next();
						
						if (stack.getItem() == stack1.getItem())
						{
							flag = true;
							list.remove(stack1);
							break;
						}
					}
					if (!flag)
						return false;
				}
			}
		}
		return list.isEmpty();
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting ic) {
		
		int folder = -1;
		int tape = -1;
		
		for (int i = 0; i < ic.getSizeInventory(); i++) {
			
			ItemStack stack = ic.getStackInSlot(i);
			
			if (stack != null)
			{
				if (stack.getItem() == RealFilingCabinet.itemFolder && stack.getItemDamage() == 0)
					folder = i;
				if (stack.getItem() == RealFilingCabinet.itemWhiteoutTape)
					tape = i;
			}
		}
		if (folder >= 0 && tape >= 0)
		{
			ItemStack stack2 = ic.getStackInSlot(folder);
			if (ItemFolder.getStack(stack2) != null && ItemFolder.getFileSize(stack2) == 0)
				return new ItemStack(RealFilingCabinet.itemEmptyFolder, 1, 0);
		}
		return null;
	}
}
