package com.draco18s.ores.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.recipes.RecipeManager;
import com.draco18s.ores.util.OreDataHooks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntitySluiceBottom extends TileEntity implements ISidedInventory {
	private ItemStack[] inventory = new ItemStack[getSizeInventory()];
	private int timer = 0;
	private Random rand = new Random();
	//private int dirt = 0;
	//private int dirtLost = 0;
	//private ArrayList<EntityItem> itemEnts = new ArrayList<EntityItem>();
	private Vec3 flowVec;
	
	private int downstremrequests = 0;
	public boolean renderWater;
	private int waterAmt;
	private boolean flowItemsTowards;
	private boolean calcflowItemsTowards;
	private AxisAlignedBB suckZone = null;
	private AxisAlignedBB flowZone = null;
	private Item itemGravel;
	
	@Override
	public void updateEntity() {
		downstremrequests = Math.min(downstremrequests, 3);
		renderWater = checkWater();
		suckItems();
		if(timer < 0) {
			timer++;
		}
		else if(timer > 0) {
			timer--;
			/*if(timer <= 0 && renderWater && inventory[1] != null) {
				doFilter();
				doFilter();
				doFilter();
				doFilter();
				doFilter();
				subtractDirt();
			}*/
			if(timer % 75 == 0) {
				doFilter();
			}
			if(timer == 0) {
				subtractDirt();
			}
			/*int b = (this.getBlockMetadata()>>2)&3 * 2;
			if(b != 0 && timer == b) {
				makeRequest();
			}*/
		}
		else if(renderWater && inventory[1] != null) {
			timer = 375;
		}
		else if(renderWater) {
			makeRequest();
			timer = -75;
		}
		if(inventory[0] != null) {
			if(inventory[1] == null) {
				inventory[1] = inventory[0].splitStack(1);
				if(inventory[0].stackSize == 0) {
					inventory[0] = null;
				}
			}
			if(inventory[0] != null && inventory[0].stackSize > 0 && !worldObj.isRemote) {
				float rx = 0.5F;
				float ry = rand.nextFloat() * 0.5F + 0.5F;
				float rz = 0.5F;

				EntityItem entityItem = new EntityItem(worldObj,
						xCoord + rx, yCoord + ry, zCoord + rz,
						new ItemStack(inventory[0].getItem(), inventory[0].stackSize, inventory[0].getItemDamage()));

				entityItem.motionX = flowVec.xCoord*0.014D;
				entityItem.motionY = 0;
				entityItem.motionZ = flowVec.zCoord*0.014D;
				entityItem.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(entityItem);
				downstremrequests = Math.max(downstremrequests-inventory[0].stackSize,0);
				inventory[0] = null;
			}
			//inventory[0] = null;
		}
	}

	private void makeRequest() {
		int m = this.getBlockMetadata()&3;
		int x=0,z=0;
		do {
			switch(m) {
				case 0:
					z += 1;
					break;
				case 1:
					z += -1;
					break;
				case 2:
					x += 1;
					break;
				case 3:
					x += -1;
					break;
			}
		} while(worldObj.getBlock(xCoord + x, yCoord, zCoord + z) == this.blockType);
		switch(m) {
			case 0:
				z += -1;
				break;
			case 1:
				z += 1;
				break;
			case 2:
				x += -1;
				break;
			case 3:
				x += 1;
				break;
		}
		TileEntity te = worldObj.getTileEntity(xCoord + x, yCoord, zCoord + z);
		if(te != null && te instanceof TileEntitySluiceBottom) {
			((TileEntitySluiceBottom)te).downstremrequests++;
		}
		//System.out.println("Upstream request made ("+x+","+z+")");
	}

	protected AxisAlignedBB getSuckZone(int x, int y, int z) {
		if(suckZone  == null) {
			suckZone = AxisAlignedBB.getBoundingBox((double)((float)x), (double)y, (double)((float)z), (double)((float)(x + 1)), (double)y + 0.25D, (double)((float)(z + 1)));
		}
        return suckZone;
    }
	
	protected AxisAlignedBB getFlowZone(int x, int y, int z) {
		if(flowZone  == null) {
			flowZone = AxisAlignedBB.getBoundingBox((double)((float)x), (double)y, (double)((float)z), (double)((float)(x + 1)), (double)y + 0.25D, (double)((float)(z + 1)));
		}
        return flowZone;
    }
	
	private void suckItems() {
		//if(worldObj.isRemote) return;
		List ents = worldObj.getEntitiesWithinAABB(EntityItem.class, getSuckZone(xCoord, yCoord, zCoord));
		//ents.clear();
		//System.out.println(ents.size() + " entities");
		EntityItem ent;
		if(ents.size() > 0) {
			ItemStack stack;
			for(int e = ents.size()-1; e >= 0; e--) {
				ent = (EntityItem) ents.get(e);
				if(inventory[0] == null && inventory[1] == null) {
					//if(!worldObj.isRemote) {
						//System.out.println("Sucking up items");
						stack = ent.getEntityItem();
						if(stack.getItem() == Item.getItemFromBlock(Blocks.gravel) || stack.getItem() == Item.getItemFromBlock(Blocks.sand)) {
							inventory[0] = stack.copy();
							inventory[0].stackSize = 1;
							stack.stackSize--;
							if(stack.stackSize == 0) {
								ent.setDead();
							}
							else if(waterAmt > 2) {
								ent.motionX += flowVec.xCoord*0.014D;
								ent.motionZ += flowVec.zCoord*0.014D;
							}
						}
						else if(ent.age > 1300) {
							ent.age = 100;
						}
					//}
				}
				else if(waterAmt >= 2 && (ent.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.gravel) || ent.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.sand))) {
					if(flowVec.lengthVector() > 0) {
						ent.delayBeforeCanPickup = 10;
					}
					ent.motionX += flowVec.xCoord*0.014D;
					ent.motionZ += flowVec.zCoord*0.014D;
				}

				else if(ent.age > 1300) {
					ent.age = 100;
				}
			}
		}
		if(flowItemsTowards && waterAmt >= 2) {
			int m = this.getBlockMetadata();
			int xx=0,zz=0;
			switch(m) {
				case 0:
					zz = 1;
					break;
				case 1:
					zz = -1;
					break;
				case 2:
					xx = 1;
					break;
				case 3:
					xx = -1;
					break;
				default:
					return;
			}
			ents = worldObj.getEntitiesWithinAABB(EntityItem.class, getFlowZone(xCoord+xx, yCoord, zCoord+zz));
			if(ents.size() > 0) {
				for(int e = ents.size()-1; e >= 0; e--) {
					ent = (EntityItem) ents.get(e);
					if((ent.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.gravel) || ent.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.sand))) {
						ent.motionX += flowVec.xCoord*0.014D;
						ent.motionZ += flowVec.zCoord*0.014D;
					}
				}
			}
		}
	}
	
	public boolean checkWater() {
		//if(worldObj == null) return false;
		int x = 0;
		int z = 0;
		int xx = 0;
		int zz = 0;
		int m = this.getBlockMetadata()&3;
		boolean flag;
		boolean ret = false;
		flowVec = Vec3.createVectorHelper(0, 0, 0);
		do {
			flag = false;
			switch(m) {
				case 0:
					z++;
					zz = 1;
					break;
				case 1:
					z--;
					zz = -1;
					break;
				case 2:
					x++;
					xx = 1;
					break;
				case 3:
					x--;
					xx = -1;
					break;
				default:
					flowItemsTowards = false;
					return false;
			}
			if(worldObj.getBlock(xCoord+x, yCoord, zCoord+z) == Blocks.water || worldObj.getBlock(xCoord+x, yCoord, zCoord+z) == Blocks.flowing_water) {
				int a = worldObj.getBlockMetadata(xCoord+x, yCoord, zCoord+z);
				int w = waterAmt;
				int D = Math.abs(x) + Math.abs(z);
				int H = worldObj.getBlockMetadata(xCoord+x, yCoord, zCoord+z);
				waterAmt = 10 - H - 2*D;
				flowVec = Vec3.createVectorHelper(-1*x, 0, -1*z).normalize();
				ret = true;
				if(calcflowItemsTowards) {
					double v = BlockLiquid.getFlowDirection(worldObj, xCoord+x, yCoord, zCoord+z, Material.water);
					double s = Math.round(Math.sin(v)*-10)/10d;//zVec
					double c = Math.round(Math.cos(v)*10)/10d;//xVec
					if(flowVec.xCoord == s && flowVec.zCoord == c) {
						flowItemsTowards = false;
					}
					else {
						flowItemsTowards = true;
					}
					calcflowItemsTowards = false;
				}
			}
			else if(worldObj.getBlock(xCoord+x, yCoord, zCoord+z) == this.blockType) {
				calcflowItemsTowards = false;
				flowItemsTowards = false;
				TileEntitySluiceBottom tt = (TileEntitySluiceBottom) worldObj.getTileEntity(xCoord+x, yCoord, zCoord+z);
				flag = true;
			}
		} while(Math.abs(x) + Math.abs(z) <= 3 && flag);
		if(!worldObj.isRemote) {
			if(ret && worldObj.getBlock(xCoord-xx, yCoord-1, zCoord-zz) == Blocks.air ) {
				worldObj.setBlock(xCoord-xx, yCoord-1, zCoord-zz, Blocks.flowing_water, 0, 3);
				worldObj.scheduleBlockUpdate(xCoord-xx, yCoord-1, zCoord-zz, Blocks.air, 0);
			}
			if(!ret && worldObj.getBlock(xCoord-xx, yCoord-1, zCoord-zz) == Blocks.water) {
				worldObj.setBlock(xCoord-xx, yCoord-1, zCoord-zz, Blocks.air, 0, 3);
				worldObj.scheduleBlockUpdate(xCoord-xx, yCoord-1, zCoord-zz, Blocks.flowing_water, 0);
			}
		}
		if(ret && waterAmt < 2) {
			ret = false;
		}
		return ret;
	}
	
	private void subtractDirt() {
		inventory[1] = null;
		/*int r = rand.nextInt(itemEnts.size());
		EntityItem e = itemEnts.get(r);
		e.getEntityItem().stackSize--;
		if(e.getEntityItem().stackSize <= 0) {
			itemEnts.remove(r);
		}*/
	}
	
	private void doFilter() {
		if(worldObj.isRemote || rand.nextInt(20) >= 7) return;
		Block b = RecipeManager.getRandomSluiceResult(this.rand, inventory[1].getItem());
		if(itemGravel == null) {
			itemGravel = Item.getItemFromBlock(Blocks.gravel);
		}
		if(b == Blocks.gravel && inventory[1].getItem() == itemGravel) {
			mergeStacks(new ItemStack(Items.flint));
		}
		else if(b != null) {
			int best = 0, c =0, bestj=0, bestk=0;
			for(int j = -1; j <= 1; j++) {
				for(int k = -1; k <= 1; k++) {
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
				System.out.println("Found " + best + " " + b.getUnlocalizedName());
				ItemStack is = new ItemStack(b.getItemDropped(0, this.rand, 0), 1, b.damageDropped(0));
				if(is.getItem() != Items.redstone && is.getItemDamage() != 2) {
					if(inventory[1].getItem() == itemGravel) {
						if(rand.nextInt(12) != 0) {
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
		}
		markDirty();
	}

	private void mergeStacks(ItemStack stack) {
		if(worldObj.isRemote) return;
		/*for(int s = 1; s < getSizeInventory(); s++) {
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
		}*/
		float rx = 0.5F;
		float ry = rand.nextFloat() * 0.25F + 0.25F;
		float rz = 0.5F;

		EntityItem entityItem = new EntityItem(worldObj,xCoord + rx, yCoord + ry, zCoord + rz,stack);

		float factor = 0.05F;
		entityItem.motionX = 0;
		entityItem.motionY = 0;
		entityItem.motionZ = 0;
		entityItem.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entityItem);
	}

	public int getDirt() {
		/*int dirt = 0;
		for (Iterator<EntityItem> it = itemEnts.iterator(); it.hasNext();) {
			EntityItem elem = it.next();
		    dirt += elem.getEntityItem().stackSize;
		}*/
		//ents
		return 0;
	}

	public void setNoWater() {
		int xx = 0;
		int zz = 0;
		int m = this.getBlockMetadata();
		switch(m) {
			case 0:
				zz = 1;
				break;
			case 1:
				zz = -1;
				break;
			case 2:
				xx = 1;
				break;
			case 3:
				xx = -1;
				break;
		}
		if(worldObj.getBlock(xCoord-xx, yCoord-1, zCoord-zz) == Blocks.water) {
			worldObj.setBlock(xCoord-xx, yCoord-1, zCoord-zz, Blocks.air, 0, 3);
			worldObj.scheduleBlockUpdate(xCoord-xx, yCoord-1, zCoord-zz, Blocks.flowing_water, 0);
		}
	}
	
	@Override
	public int getSizeInventory() {
		return 2;
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
		return stack.getItem() == Item.getItemFromBlock(Blocks.gravel) || stack.getItem() == Item.getItemFromBlock(Blocks.sand);
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
        flowItemsTowards = tc.getBoolean("flowItemsTowards");
        renderWater = tc.getBoolean("renderWater");
        waterAmt = tc.getInteger("waterAmt");
        flowVec = Vec3.createVectorHelper(tc.getDouble("flowVec_x"), 0,tc.getDouble("flowVec_z"));
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
        tc.setBoolean("flowItemsTowards", flowItemsTowards);
        tc.setBoolean("renderWater", renderWater);
        tc.setInteger("waterAmt", waterAmt);
        if(flowVec != null) {
	        tc.setDouble("flowVec_x", flowVec.xCoord);
	        tc.setDouble("flowVec_z", flowVec.zCoord);
        }
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
	public int getBlockMetadata() {
        if (this.blockMetadata < 1 && worldObj != null) {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }
        if(this.blockType == null) {
            this.blockType = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata;
    }

	public int getTime() {
		return timer;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(side == 1 && flowItemsTowards && (downstremrequests > 0 || inventory[1] == null))
			return new int[] {0};
		else
			return new int[] {};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return (side==1&&slot==0)&&(inventory[1] == null || downstremrequests > 0);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	public String getModelTexture() {
		return "ores:textures/entities/sluice.png";
	}

	public float getRotationY() {
		return 0;
	}

	public int getWaterAmount() {
		if(getBlockMetadata() > 4) return 0;
		return waterAmt;
	}

	public void updateFlow() {
		if(getBlockMetadata() > 4) {
			setNoWater();
		}
		calcflowItemsTowards = true;
	}
}
