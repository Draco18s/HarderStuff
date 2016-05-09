package com.draco18s.ores.entities;

import java.util.Random;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IMechanicalPowerUser;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.item.ItemOreDustSmall;
import com.draco18s.ores.item.ItemRawOre;
import com.draco18s.ores.recipes.RecipeManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMillstone extends TileEntity implements ISidedInventory, IMechanicalPowerUser {
	private float grindTime;
	private ItemStack[] inventory = new ItemStack[2];
	//private boolean hasPower = false;
	private float powerLevel;
	
	@Override
	public void updateEntity() {
		/*if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
			System.out.println("Doing update: " + grindTime + " " + powerLevel);
		}*/
		if(grindTime > 0) {
            grindTime -= powerLevel;
            if(inventory[0] == null) {
            	grindTime = 0;
            }
            else if (grindTime <= 0) {
            	grindItem();
            	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
			this.markDirty();
        }
        else if (canGrind()) {
            grindTime = 400;
            OresBase.proxy.startMillSound(this);
            //worldObj.playSoundEffect(xCoord, yCoord, zCoord, "ores:grain-mill-loop", 0.5F, worldObj.rand.nextFloat() * 0.25F + 0.6F);
        }
        else if(inventory[0] != null) {
            //grindTime = 400;
        	int x = 0;
        	int z = 0;
        	switch(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
	        	case 3:
	        		z++;
	        		break;
	        	case 5:
	        		x--;
	        		break;
	        	case 7:
	        		z--;
	        		break;
	        	case 9:
	        		x++;
	        		break;
        	}
        	TileEntityMillstone te = (TileEntityMillstone)worldObj.getTileEntity(xCoord+x, yCoord, zCoord+z);
        	if(te != null){
        		ItemStack items = te.getStackInSlot(0);
	        	if(items == null || items.stackSize < 64) {
	        		if(items == null) {
	            		te.setInventorySlotContents(0, inventory[0].copy());
	        			inventory[0] = null;
	        		}
	        		else if(items.getItem() == inventory[0].getItem() && items.getItemDamage() == inventory[0].getItemDamage()) {
	            		int room = 64 - items.stackSize;
	            		room = Math.min(room, inventory[0].stackSize);
	            		inventory[0].stackSize -= room;
	            		items.stackSize += room;
	            		if (inventory[0].stackSize <= 0) {
	                        inventory[0] = null;
	                    }
	            	}
	        	}
        	}
        }
	}
	
	public int minimumTorque() {
		return 650;
	}

	public float powerScale(int p) {
		float f = p / 40f;
		return ((float)Math.sqrt(f)-0.1f)*1.1f;
	}
	
	public float getGrindTime() {
		return grindTime;
	}

	public boolean canGrind() {
		if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != 1) {
			return false;
		}
		else {
			boolean ret = false;
			if (inventory[0] == null) {
				ret = false;
	        }
	        else {
	            ItemStack itemstack = HardLibAPI.recipeManager.getMillResult(inventory[0]);
	            if (itemstack == null) {
	            	ret = false;
	            }
	            else {
		            if (inventory[1] == null) {
		            	ret = true;
		            }
		            else if (inventory[1].getItem() != itemstack.getItem() || inventory[1].getItemDamage() != itemstack.getItemDamage()) {
		            	ret = false;
		            }
		            else {
	            		int result = inventory[1].stackSize + itemstack.stackSize;
		            	ret = result <= getInventoryStackLimit() && result <= inventory[1].getMaxStackSize();
		            }
	            }
	        }
			if(inventory[1] != null && (!ret || !worldObj.isRemote && (inventory[1].stackSize >= 8)) && worldObj.isAirBlock(xCoord, yCoord-1, zCoord)) {
				if(!worldObj.isRemote) {	
					Random rand = new Random();
					float rx = rand.nextFloat() * 0.6F + 0.2F;
					float ry = rand.nextFloat() * 0.2F + 0.6F - 1;
					float rz = rand.nextFloat() * 0.6F + 0.2F;
					EntityItem entityItem = new EntityItem(worldObj,
							xCoord + rx, yCoord + ry, zCoord + rz,
							new ItemStack(inventory[1].getItem(), inventory[1].stackSize, inventory[1].getItemDamage()));
					worldObj.spawnEntityInWorld(entityItem);
					entityItem.motionX = 0;
					entityItem.motionY = -0.2F;
					entityItem.motionZ = 0;
				}
				inventory[1] = null;
			}
			return ret;
		}
	}

	private void grindItem() {
		/*if(!worldObj.isRemote) {
			System.out.println("Did a grind");
		}*/
		//boolean sifted = false;
        if (canGrind()) {
    		ItemStack itemstack = HardLibAPI.recipeManager.getMillResult(inventory[0]);
            
            if (inventory[1] == null) {
                inventory[1] = itemstack.copy();
            }
            else if (inventory[1].getItem() == itemstack.getItem()) {
                inventory[1].stackSize += itemstack.stackSize;
            }

            --inventory[0].stackSize;
            //sifted = true;
            if (inventory[0].stackSize <= 0) {
                inventory[0] = null;
            }
        }
    }
	
	/*public void setPowerStatus(boolean p) {
		//System.out.println("Power status: " + p);
		hasPower = p;
	}*/

	@Override
	public void setPowerLevel(float p) {
		//System.out.println("Power status: " + p);
		powerLevel = p;
	}

	@Override
	public int getSizeInventory() {
		return 2;
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
                markDirty();
                return itemstack;
            }
            else {
                itemstack = inventory[slot].splitStack(amt);

                if (inventory[slot].stackSize == 0) {
                    inventory[slot] = null;
                }

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
		/*if(slot == 2) {
			return;
		}*/
		inventory[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
        	stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
	}

	@Override
	public String getInventoryName() {
		return "Millstone";
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
		/*if(slot == 2) {
			return false;
		}*/
		return HardLibAPI.recipeManager.canInsert(this, stack) && ((inventory[slot] == null) || (inventory[slot].getItem() == stack.getItem() && inventory[slot].getItemDamage() == stack.getItemDamage()));
		//return (stack.getItem() instanceof ItemRawOre && stack.getItemDamage() < 2)||(stack.getItem() == Items.wheat);
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
        grindTime = tc.getFloat("grindTime");
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
        tc.setFloat("grindTime", grindTime);
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
		if(grindTime > 0)
			OresBase.proxy.startMillSound(this);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(worldObj != null) {
			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			if(meta%2 == 0) {
				return new int[]{};
			}
			if(side == 1) {
				return new int[]{0};
			}
			if(side == 0) {
				return new int[]{1};
			}
			return new int[]{};
		}
		return new int[]{0,1};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(worldObj != null) {
			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			if(meta == 1 || meta%2 == 0) {
				return false;
			}
		}
		if(side == 1 && slot == 0)
			return this.isItemValidForSlot(slot, stack);
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		if(worldObj != null) {
			if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != 1) {
				return false;
			}
		}
		if(side == 0 && slot == 1) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasPower() {
		return powerLevel > 0;
	}

	@Override
	public String getActionString() {
		return "Grind";
	}
}
