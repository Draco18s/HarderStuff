package com.draco18s.wildlife.entity;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.wildlife.WildlifeBase;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityTanner extends TileEntity implements ISidedInventory {
	private int[] tanningTime = new int[2];
	private ItemStack[] inventory = new ItemStack[2];

	public TileEntityTanner() { }
	
	@Override
	public void updateEntity() {
		if(inventory[0] != null) {
			cureLeather(0);
		}
		if(inventory[1] != null) {
			cureLeather(1);
		}
	}

	private void cureLeather(int i) {
		if(inventory[i].getItem() == WildlifeBase.itemRawLeather) {
			tanningTime[i]++;
			if(tanningTime[i] > 1200) {
				inventory[i] = new ItemStack(Items.leather);
                if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
                //worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
			}
		}
		/*else {
			tanningTime[i] = 0;
		}*/
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot >= 0 && slot < 2) {
			return inventory[slot];
		}
		return null;
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
		if(slot >= 0 && slot < 2) {
			inventory[slot] = stack;
	
	        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
	        	stack.stackSize = getInventoryStackLimit();
	        }
	        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	        tanningTime[slot] = 0;
	        markDirty();
		}
	}

	@Override
	public String getInventoryName() {
		return "Tanner";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
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
		return stack == null || stack.getItem() == WildlifeBase.itemRawLeather;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tc) {
        super.readFromNBT(tc);
        NBTTagList nbttaglist = tc.getTagList("Items", 10);
        inventory = new ItemStack[getSizeInventory()];
        tanningTime = new int[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < inventory.length)
            {
                inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                tanningTime[i] = nbttagcompound1.getInteger("time");
            }
        }
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
                nbttagcompound1.setInteger("time", tanningTime[i]);
            }
        }

        tc.setTag("Items", nbttaglist);
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
	public int getSizeInventory() {
		return 2;
	}

	public String getModelTexture() {
		return "wildlife:textures/entity/tanner.png";
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int m = getBlockMetadata()*2;
		if(side == (2+m) || side == (3+m)) return new int[]{(side - 2 - m)};
		if(side == 0) return new int[]{0,1};
		return new int[]{};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return isItemValidForSlot(slot, stack) && this.getStackInSlot(slot) == null;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		int m = getBlockMetadata()*2;
		boolean validSide = false;
		if(side == 0) validSide = true;
		if(slot-2-m == side) validSide = true;
		return validSide && this.getStackInSlot(slot) != null && this.getStackInSlot(slot).getItem() == Items.leather;
	}
}
