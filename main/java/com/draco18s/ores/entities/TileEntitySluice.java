package com.draco18s.ores.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.recipes.RecipeManager;
import com.draco18s.ores.util.OreDataHooks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntitySluice extends TileEntity implements IInventory {

	private ItemStack[] inventory = new ItemStack[getSizeInventory()];
	private int timer = 0;
	private Random rand = new Random();
	private int dirt = 0;
	private int dirtLost = 0;
	private int sand = 0;
	private int sandLost = 0;
	private AxisAlignedBB suckZone = null;
	private Item itemGravel;
	
	@Override
	public void updateEntity() {
		suckItems();
		doWater();
		if(timer > 0) {
			timer--;
			if(timer <= 0 && canFilter()) {
				doFilter();
			}
			if(timer % 15 == 0 && canFilter()) {
				if(dirt > sand) {
					if(dirt+sand > 5120) {
						dirt -= 64;
						dirtLost += 34;//33 red
					}
					else if(dirt+sand > 1280) {
						dirt -= 16;
						dirtLost += 10;//9, yellow
					}
					else if(dirt+sand > 640) {
						dirt -= 8;
						dirtLost += 6;//5, green
					}
					else if(dirt+sand > 320) {
						dirt -= 4;
						dirtLost += 4;//3, blue
					}
				}
				else {
					if(dirt+sand > 5120) {
						sand -= 64;
						sandLost += 34;//33 red
					}
					else if(dirt+sand > 1280) {
						sand -= 16;
						sandLost += 10;//9, yellow
					}
					else if(dirt+sand > 640) {
						sand -= 8;
						sandLost += 6;//5, green
					}
					else if(dirt+sand > 320) {
						sand -= 4;
						sandLost += 4;//3, blue
					}
				}
				markDirty();
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.blockType, 0);
			}
		}
		else if(canFilter()) {
			timer = 75;
		}
		if(inventory[0] != null) {
			if(inventory[0].getItem() == itemGravel) {
				dirt += 5 * inventory[0].stackSize;
				inventory[0] = null;
				markDirty();
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.blockType, 0);
			}
			else {
				sand += 5 * inventory[0].stackSize;
				inventory[0] = null;
				markDirty();
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.blockType, 0);
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
				if(stack.getItem() == Item.getItemFromBlock(Blocks.gravel) || stack.getItem() == Item.getItemFromBlock(Blocks.sand)) {
					 inventory[0] = stack.copy();
					 ent.setDead();
					 return;
				 }
			}
		}
	}

	protected AxisAlignedBB getAABB(int x, int y, int z) {
		if(suckZone  == null) {
			suckZone = AxisAlignedBB.getBoundingBox((double)((float)x), (double)y, (double)((float)z), (double)((float)(x + 1)), (double)y + 0.25D, (double)((float)(z + 1)));
		}
        return suckZone;
    }
	
	public boolean checkWater() {
		return false;
	}
	
	private boolean doWater() {
		int x = 0;
		int z = 0;
		int m = this.getBlockMetadata();
		switch(m) {
			case 2:
				z++;
				break;
			case 3:
				z--;
				break;
			case 4:
				x++;
				break;
			case 5:
				x--;
				break;
		}
		if(worldObj.getBlock(xCoord+x, yCoord+1, zCoord+z) != Blocks.water) {
			if(worldObj.getBlock(xCoord-x, yCoord-1, zCoord-z) == Blocks.water) {
				worldObj.setBlockToAir(xCoord-x, yCoord-1, zCoord-z);
			}
			return false;
		}
		else {
			if(worldObj.getBlock(xCoord-x, yCoord-1, zCoord-z) == Blocks.air) {
				worldObj.setBlock(xCoord-x, yCoord-1, zCoord-z, Blocks.flowing_water, 0, 3);
				worldObj.scheduleBlockUpdate(xCoord-x, yCoord-1, zCoord-z, Blocks.air, 0);
			}
			else if(worldObj.getBlock(xCoord-x, yCoord-1, zCoord-z) != Blocks.water) {
				return false;
			}
		}
		while(dirtLost >= 5 && !worldObj.isRemote) {
			EntityItem entityItem = new EntityItem(worldObj,
					xCoord-x+0.5f, yCoord-0.25f, zCoord-z+0.5f,
					new ItemStack(Blocks.gravel, 1, 0));

			entityItem.motionX = 0;
			entityItem.motionY = 0.2f;
			entityItem.motionZ = 0;
			
			worldObj.spawnEntityInWorld(entityItem);
			dirtLost -= 5;
		}
		while(sandLost >= 5 && !worldObj.isRemote) {
			EntityItem entityItem = new EntityItem(worldObj,
					xCoord-x+0.5f, yCoord-0.25f, zCoord-z+0.5f,
					new ItemStack(Blocks.sand, 1, 0));

			entityItem.motionX = 0;
			entityItem.motionY = 0.2f;
			entityItem.motionZ = 0;
			
			worldObj.spawnEntityInWorld(entityItem);
			sandLost -= 5;
		}
		return true;
	}
	
	public boolean canFilter() {
		if(doWater()) {
			if(dirt+sand > 0) {
				for(int s = 1; s < getSizeInventory(); s++) {
					if(inventory[s] == null || inventory[s].stackSize < inventory[s].getMaxStackSize()) {
						return true;
					}
				}
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	private void doFilter() {
		boolean usedGravel = false;
		if(dirt > sand) {
			usedGravel = true;
			dirt--;
		}
		else {
			sand--;
		}
		if(rand.nextInt(20) >= 7) {
			return;
		}
		int r;
		if(itemGravel == null) {
			itemGravel = Item.getItemFromBlock(Blocks.gravel);
		}
		Block b = RecipeManager.getRandomSluiceResult(this.rand, ((usedGravel)?itemGravel:null));
		if(b == Blocks.gravel && usedGravel) {
			mergeStacks(new ItemStack(Items.flint));
		}
		else if(b != null) {
			int best = 0, c =0, bestj=0, bestk=0;
			for(int j = -1; j <= 1; j++) {
				for(int k = -1; k <= 1; k++) {
					//System.out.println("Getting data for " + b.getUnlocalizedName());
					c = OreDataHooks.getOreData(worldObj, xCoord+j*16, yCoord, zCoord+k*16, b)+OreDataHooks.getOreData(worldObj, xCoord+j*16, yCoord-8, zCoord+k*16, b);
					if(c > best) {
						best = c;
						bestj = j;
						bestk = k;
					}
				}
			}
			if(best > 0) {
				OreDataHooks.subOreData(worldObj, xCoord+bestj*16, yCoord, zCoord+bestk*16, b, 1);
				//System.out.println("Found " + best + " " + b.getUnlocalizedName());
				ItemStack is = new ItemStack(b.getItemDropped(0, this.rand, 0), 1, b.damageDropped(0));
				if(is.getItem() != Items.redstone && is.getItemDamage() != 2) {
					if(usedGravel) {
						if(rand.nextInt(10) != 0) {
							is = RecipeManager.getMillResult(is).copy();
							is.stackSize = 1;
						}
					}
					else {
						is = RecipeManager.getMillResult(is).copy();
						is.stackSize = 2;
						if(rand.nextInt(10) != 0) {
							is.stackSize = 1;
						}
					}
				}
				mergeStacks(is);
			}
			else if(rand.nextInt(5) != 0) {
				if(usedGravel)
					++dirtLost;
				else
					++sandLost;
			}
		}
		markDirty();
	}

	private void mergeStacks(ItemStack stack) {
		for(int s = 1; s < getSizeInventory(); s++) {
			if(inventory[s] == null) {
				//System.out.println("Inserting into " + s);
				inventory[s] = stack.copy();
				return;
			}
			else if(inventory[s].stackSize+stack.stackSize <= inventory[s].getMaxStackSize() && inventory[s].getItem() == stack.getItem() && inventory[s].getItemDamage() == stack.getItemDamage()) {
				//System.out.println("Merging into " + s);
				inventory[s].stackSize += stack.stackSize;
				return;
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		/*if(inventory[slot] != null) {
			System.out.println(inventory[slot].getDisplayName());
		}*/
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
		return "Sluice";
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
		return false;
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
        timer = tc.getInteger("timer");
        dirt = tc.getInteger("dirt");
        dirtLost = tc.getInteger("dirtLost");
    }

    @Override
    public void writeToNBT(NBTTagCompound tc) {
        super.writeToNBT(tc);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
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
        tc.setInteger("timer", timer);
        tc.setInteger("dirt", dirt);
        tc.setInteger("dirtLost", dirtLost);
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

	public void setNoWater() {
		int x = 0;
		int z = 0;
		int m = this.getBlockMetadata();
		BlockFurnace k;
		switch(m) {
			case 2:
				z++;
				break;
			case 3:
				z--;
				break;
			case 4:
				x++;
				break;
			case 5:
				x--;
				break;
		}
		if(worldObj.getBlock(xCoord-x, yCoord-1, zCoord-z) == Blocks.water) {
			worldObj.setBlockToAir(xCoord-x, yCoord-1, zCoord-z);
		}
	}
	
	public int getBlockMetadata() {
        if (this.blockMetadata < 1 && worldObj != null) {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata;
    }

	public String getModelTexture() {
		return "ores:textures/entities/sluice.png";
	}

	public float getRotationY() {
		switch(this.getBlockMetadata()) {
			case 5:
				return -1*(float) (Math.PI/2f);
			case 3:
				return (float) Math.PI;
			case 4:
				return (float) (Math.PI/2f);
			case 2:
				return 0;
		}
		return (float) Math.PI;
	}

	public int getTime() {
		return timer;
	}

	public int getDirt() {
		return dirt+sand;
	}

	public int getComparatorValue() {
		int r = 0;
		for (int i = 1; i < getSizeInventory(); ++i)
        {
            if (inventory[i] != null)
            {
            	++r;            	
            }
        }
		return r;
	}
}
