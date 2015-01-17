package com.draco18s.ores.entities;

import java.util.List;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.item.ItemOreDustSmall;
import com.draco18s.ores.recipes.RecipeManager;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntitySifter extends TileEntity implements ISidedInventory {
	private int siftTime;
	private ItemStack[] inventory = new ItemStack[3];
	private AxisAlignedBB suckZone = null;
	
	public TileEntitySifter() {
	}
	
	@Override
	public void updateEntity() {
		suckItems();
		if (siftTime > 0) {
            --siftTime;
            if(inventory[0] == null && inventory[1] == null) {
            	siftTime = 0;
            }
            else if (siftTime == 0) {
            	siftItem();
            }
            if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			markDirty();
        }
        else if (canSift(0) || canSift(1)) {
            siftTime = 40;
        }
        else if(inventory[0] != null && inventory[1] != null) {
        	if(inventory[0].getItem() == inventory[0].getItem() && inventory[0].getItemDamage() == inventory[1].getItemDamage()) {
        		inventory[0].stackSize += inventory[1].stackSize;
        		inventory[1] = null;
        	}
        }
		//System.out.println("Can sift? " + canSift());
	}
	
	private void siftItem() {
		//boolean sifted = false;
        if (canSift(0)) {
    		ItemStack itemstack = RecipeManager.getSiftResult(inventory[0], true);
            
            if (inventory[2] == null) {
                inventory[2] = itemstack.copy();
            }
            else if (inventory[2].getItem() == itemstack.getItem()) {
                inventory[2].stackSize += itemstack.stackSize;
            }

            inventory[0].stackSize -= RecipeManager.getSiftAmount(inventory[0]);
            //sifted = true;
            if (inventory[0].stackSize <= 0) {
                inventory[0] = null;
            }
        }
        else if(canSift(1)) {
        	ItemStack itemstack = RecipeManager.getSiftResult(inventory[1], true);
            
            if (inventory[2] == null) {
                inventory[2] = itemstack.copy();
            }
            else if (inventory[2].getItem() == itemstack.getItem()) {
                inventory[2].stackSize += itemstack.stackSize;
            }

            inventory[1].stackSize -= RecipeManager.getSiftAmount(inventory[1]);
            if (inventory[1].stackSize <= 0) {
                inventory[1] = null;
            }
        }
    }
	
	private void suckItems() {
		List ents = worldObj.getEntitiesWithinAABB(EntityItem.class, getAABB(xCoord, yCoord+1, zCoord));
		if(ents.size() > 0) {
			ItemStack stack;
			EntityItem ent;
			for(int e = ents.size()-1; e >= 0; e--) {
				ent = (EntityItem) ents.get(e);
				stack = ent.getEntityItem();
				if(RecipeManager.getSiftResult(stack, false) != null) {
					if(mergeStacks(stack)) {
						 ent.setDead();
						 return;
					}
				}
			}
		}
	}

	protected AxisAlignedBB getAABB(int x, int y, int z) {
		if(suckZone == null) {
			suckZone = AxisAlignedBB.getBoundingBox((double)((float)x), (double)y, (double)((float)z), (double)((float)(x + 1)), (double)y + 0.25D, (double)((float)(z + 1)));
		}
        return suckZone;
    }
	
	private boolean mergeStacks(ItemStack stack) {
		for(int s = 0; s < 2; s++) {
			if(inventory[s] == null) {
				inventory[s] = stack.copy();
				return true;
			}
			else if(inventory[s].stackSize+stack.stackSize <= inventory[s].getMaxStackSize() && inventory[s].getItem() == stack.getItem() && inventory[s].getItemDamage() == stack.getItemDamage()) {
				inventory[s].stackSize += stack.stackSize;
				return true;
			}
		}
		return false;
	}

	private boolean canSift(int a) {
		if (inventory[a] == null) {
			return false;
        }
        else {
            ItemStack itemstack = RecipeManager.getSiftResult(inventory[a], true);
            if (itemstack == null) return false;
            if (inventory[2] == null) return true;
            if (!inventory[2].isItemEqual(itemstack)) return false;
            int result = inventory[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= inventory[2].getMaxStackSize();
        }
	}
	
	public int getTime() {
		return siftTime;
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		if (inventory[slot] != null) {
            ItemStack itemstack;

            if (inventory[slot].stackSize <= amt) {
                itemstack = inventory[slot];
                inventory[slot] = null;
                if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
                return itemstack;
            }
            else {
                itemstack = inventory[slot].splitStack(amt);

                if (inventory[slot].stackSize == 0) {
                    inventory[slot] = null;
                }
                if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
                return itemstack;
            }
        }
        else {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return inventory[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
        	stack.stackSize = getInventoryStackLimit();
        }
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
	}

	@Override
	public String getInventoryName() {
		return "Sifter";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(slot == 2) {
			return false;
		}
		return RecipeManager.canInsert(this, stack);
		//return (stack.getItem() instanceof ItemOreDustSmall);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tc) {
        super.readFromNBT(tc);
        NBTTagList nbttaglist = tc.getTagList("Items", 10);
        inventory = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < inventory.length)
            {
                inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        siftTime = tc.getInteger("siftTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tc) {
        super.writeToNBT(tc);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < inventory.length; ++i)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tc.setTag("Items", nbttaglist);
        tc.setInteger("siftTime", siftTime);
    }
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(side == 1) {
			return new int[]{1,0};
		}
		return new int[]{2};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		if(side != 1) {
			if(slot == 2) {
				return true;
			}
		}
		return false;
	}

	public String getModelTexture() {
		return "ores:textures/entities/sifter.png";
	}
}
