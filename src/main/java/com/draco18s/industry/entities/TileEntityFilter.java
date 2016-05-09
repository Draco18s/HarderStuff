package com.draco18s.industry.entities;

import scala.collection.concurrent.Debug;

import com.draco18s.industry.entities.TileEntityFilter.EnumAcceptType;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;

public class TileEntityFilter extends TileEntityHopper {
    private ItemStack[] filters = new ItemStack[6];
    private IInventory[] fakeEntities = new IInventory[6];
    private EnumAcceptType acceptRule = EnumAcceptType.OR;
    
	public TileEntityFilter() {
		super();
		func_145886_a("Wooden Hopper");
	}
	
	@Override
    public ItemStack getStackInSlot(int slot) {
        if(slot < 5) {
        	return super.getStackInSlot(slot);
        }
        else {
        	return filters[slot-5];
        }
    }
    
    public ItemStack decrStackSize(int slot, int num) {
		if(slot < 5) {
			return super.decrStackSize(slot, num);
		}
		if (filters[slot-5] != null) {
            ItemStack itemstack;
            if (filters[slot-5].stackSize <= num) {
                itemstack = filters[slot-5];
                filters[slot-5] = null;
                fakeEntities[slot-5] = null;
                return itemstack;
            }
            else {
                itemstack = filters[slot-5].splitStack(num);
                if (filters[slot-5].stackSize == 0) {
                    filters[slot-5] = null;
                    fakeEntities[slot-5] = null;
                }
                return itemstack;
            }
        }
        else {
            return null;
        }
    }
	
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(stack == null) { return true; }
		if(slot < 5) {
			if(doIHaveFilters()) {
				switch(acceptRule) {
					case OR:
						return acceptOr(slot, stack);
					case AND:
						return acceptAnd(slot, stack);
					case NONE:
						return !acceptOr(slot, stack);
					case SOME:
						return acceptOr(slot, stack) && !acceptAnd(slot, stack);
				}
			}
		}
		else if(stack.getItem() instanceof ItemBlock) {
			ItemBlock ib = (ItemBlock)stack.getItem();
			if(ib.field_150939_a.hasTileEntity(stack.getItemDamage())) {
				TileEntity te = ib.field_150939_a.createTileEntity(worldObj,stack.getItemDamage());
				return te instanceof IInventory;
			}
		}
		return false;
    }
	
	private boolean acceptOr(int slot, ItemStack stack) {
		boolean ret = false;
		for(int i = fakeEntities.length-1;i>=0;i--) {
			if(fakeEntities[i] != null) {
				if(fakeEntities[i] instanceof ISidedInventory) {
					ISidedInventory isi = ((ISidedInventory)fakeEntities[i]);
					int a = 0;
					switch(i) {
						case 0:
						case 1:
							a = 1;
							break;
						case 2:
						case 3:
							a = 2;
							break;
						case 4:
						case 5:
							a = 0;
							break;
					}
					int[] accessibleSlots = isi.getAccessibleSlotsFromSide(a);
					for(int s : accessibleSlots) {
						ret |= doesAccept(isi, s, a, stack);
						//System.out.println("Slot " + s + ret);
					}
					if(ret) return ret;
				}
				else {
					for(int s = fakeEntities[i].getSizeInventory()-1; s>=0; s--) {
						if(doesAccept(fakeEntities[i], s, stack)) {
							return true;
						}
					}
				}
			}
		}
		return ret;
	}
	
	private boolean acceptAnd(int slot, ItemStack stack) {
		boolean ret = true;
		for(int i = fakeEntities.length-1;i>=0;i--) {
			if(fakeEntities[i] != null) {
				boolean anySlot = false;
				if(fakeEntities[i] instanceof ISidedInventory) {
					ISidedInventory isi = ((ISidedInventory)fakeEntities[i]);
					int a = 0;
					switch(i) {
						case 0:
						case 1:
							a = 1;
							break;
						case 2:
						case 3:
							a = 2;
							break;
						case 4:
						case 5:
							a = 0;
							break;
					}
					int[] accessibleSlots = isi.getAccessibleSlotsFromSide(a);
					for(int s : accessibleSlots) {
						anySlot = anySlot || doesAccept(isi, s, a, stack);
					}
				}
				else {
					for(int s = fakeEntities[i].getSizeInventory()-1; !anySlot && s>=0; s--) {
						if(doesAccept(fakeEntities[i], s, stack)) {
							anySlot = true;
						}
					}
				}
				ret &= anySlot;
			}
		}
		return ret;
	}
	
	private boolean doesAccept(IInventory inven, int slot, ItemStack stack) {
		try {
			if(inven.isItemValidForSlot(slot, stack))
				return true;
		}
		catch (NullPointerException ex) { }
		return false;
	}
	
	private boolean doesAccept(ISidedInventory inven, int slot, int side, ItemStack stack) {
		try {
			/*Special handling for fuel discrimination*/
			if(inven instanceof TileEntityFurnace) {
				if(slot == 1) {
					return TileEntityFurnace.getItemBurnTime(stack) > 0;
				}
				else if(slot == 0) {
					return FurnaceRecipes.smelting().getSmeltingResult(stack) != null;
				}
			}
			else if(inven.canInsertItem(slot, stack, side)) {
				return true;
			}
		}
		catch (NullPointerException ex) { }
		return false;
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(slot < 5) {
			super.setInventorySlotContents(slot, stack);
		}
		else if(stack == null) {
			filters[slot - 5] = null;
			fakeEntities[slot - 5] = null;
		}
		else if(stack.getItem() instanceof ItemBlock) {
			ItemBlock ib = (ItemBlock)stack.getItem();
			if(ib.field_150939_a.hasTileEntity(stack.getItemDamage())) {
				TileEntity te = ib.field_150939_a.createTileEntity(worldObj,stack.getItemDamage());
				if(te instanceof IInventory) {
					filters[slot - 5] = stack;
					//filters[slot - 5].stackSize = 0;
					fakeEntities[slot - 5] = (IInventory)te;
				}
			}
		}
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
    }
	
	public boolean doIHaveFilters() {
		for(IInventory ii : fakeEntities) {
			if(ii != null) return true;
		}
		return false;
	}
	
	public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList nbttaglist = tag.getTagList("Filters", 10);
        filters = new ItemStack[6];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("FSlot") & 255;
            if (j >= 0 && j < filters.length) {
            	filters[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            	ItemBlock ib = (ItemBlock)filters[j].getItem();
    			if(ib.field_150939_a.hasTileEntity(filters[j].getItemDamage())) {
    				TileEntity te = ib.field_150939_a.createTileEntity(worldObj,filters[j].getItemDamage());
    				if(te instanceof IInventory) {
    					fakeEntities[j] = (IInventory)te;
    				}
    			}
            }
        }
        acceptRule = EnumAcceptType.values()[tag.getInteger("AcceptType")];
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < filters.length; ++i) {
            if (filters[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("FSlot", (byte)i);
                filters[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        tag.setTag("Filters", nbttaglist);
        tag.setInteger("AcceptType", acceptRule.ordinal());
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

	public EnumAcceptType getEnumType() {
		return acceptRule;
	}
    
    public void setEnumType(EnumAcceptType a) {
    	acceptRule = a;
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
    }
    
    public static enum EnumAcceptType {
    	OR,
    	AND,
    	NONE,
    	SOME;//some?
    }
}
