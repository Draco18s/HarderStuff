package com.draco18s.ores.entities;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IMechanicalPowerUser;

public class TileEntityOreProcessor extends TileEntity implements ISidedInventory, IMechanicalPowerUser {
	private float processTime;
	private float powerLevel;
	private float timeMod;

	private ItemStack[] inventory = new ItemStack[3];
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) return;
		//System.out.println("Time left: " + processTime + "," + powerLevel);
		if(processTime > 0) {
            processTime -= powerLevel/timeMod;
            if(inventory[0] == null && inventory[1] == null) {
            	processTime = 0;
            }
            else if (processTime <= 0) {
            	processItem();
            }
            if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			markDirty();
        }
        else if(canProcess(0) || canProcess(1)) {
            processTime = 100;
            timeMod = 1;
        	ItemStack nextResult = null;
            if(canProcess(0)) {
				nextResult = HardLibAPI.recipeManager.getProcessorResult(inventory[0], true);
			}
			else if(canProcess(1)) {
				nextResult = HardLibAPI.recipeManager.getProcessorResult(inventory[1], true);
			}
			if(nextResult != null && nextResult.getItem() instanceof ItemBlock) {
				try {
					timeMod = Block.getBlockFromItem(nextResult.getItem()).getBlockHardness(null, 0, 0, 0);
					if(nextResult.getItem().getHarvestLevel(nextResult, "shovel") >= 0) {
						timeMod *= 2;
					}
				}
				catch(NullPointerException e) {
					
				}
			}
        }
	}

	private boolean canProcess(int a) {
		if (inventory[a] == null) {
			return false;
        }
        else {
            ItemStack itemstack = HardLibAPI.recipeManager.getProcessorResult(inventory[a], true);
            if (itemstack == null) return false;
            if (inventory[2] == null) return true;
            if (!inventory[2].isItemEqual(itemstack)) return false;
            int result = inventory[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= inventory[2].getMaxStackSize();
        }
	}

	private void processItem() {
		if (canProcess(0)) {
    		ItemStack itemstack = HardLibAPI.recipeManager.getProcessorResult(inventory[0], true);
            
            if (inventory[2] == null) {
                inventory[2] = itemstack.copy();
            }
            else if (inventory[2].getItem() == itemstack.getItem()) {
                inventory[2].stackSize += itemstack.stackSize;
            }

            inventory[0].stackSize -= HardLibAPI.recipeManager.getProcessorAmount(inventory[0]);
            //sifted = true;
            if (inventory[0].stackSize <= 0) {
                inventory[0] = null;
            }
        }
        else if(canProcess(1)) {
        	ItemStack itemstack = HardLibAPI.recipeManager.getProcessorResult(inventory[1], true);
            
            if (inventory[2] == null) {
                inventory[2] = itemstack.copy();
            }
            else if (inventory[2].getItem() == itemstack.getItem()) {
                inventory[2].stackSize += itemstack.stackSize;
            }

            inventory[1].stackSize -= HardLibAPI.recipeManager.getProcessorAmount(inventory[1]);
            if (inventory[1].stackSize <= 0) {
                inventory[1] = null;
            }
        }
	}

	@Override
	public void setPowerLevel(float powerInput) {
		if(worldObj.isRemote) return;
		powerLevel = powerInput;
		//System.out.println(""+powerLevel);
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	@Override
	public boolean hasPower() {
		return powerLevel > 0;
	}
	
	public int minimumTorque() {
		return 500;
	}
	
	@Override
	public float powerScale(int p) {
		float f = p / 60f;
		return (((float)Math.sqrt(f)-0.1f)*0.84f) / timeMod;//TODO: check WAILA
	}

	@Override
	public String getActionString() {
		return "Process";
	}
	
	public float getTime() {
		return processTime;
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
		return "gui.processor";
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
		//System.out.println(stack.getDisplayName() + ":" + HardLibAPI.recipeManager.getProcessorResult(stack, false));
		return HardLibAPI.recipeManager.canInsert(this, stack);
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
        processTime = tc.getFloat("processTime");
        powerLevel = tc.getFloat("powerLevel");
        timeMod = tc.getFloat("timeMod");
        //System.out.println("powerLevel read from nbt (" + worldObj.isRemote + "): " + powerLevel);
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
        tc.setFloat("processTime", processTime);
        tc.setFloat("powerLevel", powerLevel);
        tc.setFloat("timeMod", timeMod);
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
		if(side == 0) {
			return new int[]{2};
		}
		return new int[]{1,0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		if(side != 1 && slot == 2) {
			return true;
		}
		return false;
	}

	//public String getModelTexture() {
	//	return "ores:textures/entities/sifter.png";
	//}
}
