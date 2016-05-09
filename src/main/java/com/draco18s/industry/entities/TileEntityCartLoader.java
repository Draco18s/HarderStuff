package com.draco18s.industry.entities;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityCartLoader extends TileEntityHopper {
	private int delay = 0;
	private int powerLevel = 0;
	private HashMap<EntityMinecartContainer, Vec3> carts = new HashMap<EntityMinecartContainer, Vec3>();
	private HashMap<UUID, Vec3> nbtCarts = new HashMap<UUID, Vec3>();

	public TileEntityCartLoader() {
		super();
		func_145886_a("Cart Loader");
	}

	/*public int getInventoryStackLimit() {
		return 16;
	}*/

	@Override
	public void updateEntity() {
		if(this.getBlockMetadata() != 0) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
		}
		
		if(nbtCarts.size() > 0) {
			List list = worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(xCoord-1, yCoord-2, zCoord-1, xCoord + 2.0D, yCoord + 2.0D, zCoord + 2.0D), IEntitySelector.selectInventories);
			if (list != null && list.size() > 0) {
				for(Object o : list) {
					EntityMinecartContainer cart = (EntityMinecartContainer)o;
					UUID uuid = cart.getPersistentID();
					if(nbtCarts.containsKey(uuid)) {
						//System.out.println("Found our cart!");
						Vec3 v = nbtCarts.get(uuid);
						carts.put(cart, v);
						nbtCarts.remove(uuid);
					}
				}
			}
		}
		
		super.updateEntity();
		if(!worldObj.isRemote) {
			List list = worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(xCoord, yCoord-1, zCoord, xCoord + 1.0D, yCoord + 2.0D, zCoord + 1.0D), IEntitySelector.selectInventories);
			//List list = worldObj.getEntitiesWithinAABB(EntityMinecartContainer.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1.0D, yCoord + 1.0D, zCoord + 1.0D));
			IInventory iinventory = null;
			if (list != null && list.size() > 0) {
				//iinventory = (IInventory)list.get(worldObj.rand.nextInt(list.size()));
				for(Object obj : list) {
					if(obj instanceof EntityMinecartContainer) {
						EntityMinecartContainer cart = (EntityMinecartContainer)obj;
						boolean hasItems = false;
						if(cart.posY > yCoord) {
							//System.out.println("Cart above...");
							//if cart has items...
							int firstNonEmpty = -1;
							for(int j = 0; j < cart.getSizeInventory() && !hasItems; j++) {
								if(cart.getStackInSlot(j) != null) {
									hasItems = true;
									firstNonEmpty = j;
								}
							}
							if(hasItems) {
								hasItems = false;
								//if I have room...
								for(int j = getSizeInventory() - 1; j >= 0 && !hasItems; j--) {
									if(getStackInSlot(j) == null || (isItemValidForSlot(j, cart.getStackInSlot(firstNonEmpty)) && getStackInSlot(j).stackSize < getInventoryStackLimit())) {
										hasItems = true;
									}
								}
							}
						}
						else {
							//System.out.println("Cart below...");
							//if I have items...
							int firstNonEmpty = -1;
							for(int j = 0; j < getSizeInventory() && !hasItems; j++) {
								if(getStackInSlot(j) != null) {
									hasItems = true;
									firstNonEmpty = j;
								}
							}
							//if cart has room...
							if(hasItems) {
								hasItems = false;
								for(int j = cart.getSizeInventory() - 1; j >= 0 && !hasItems; j--) {
									if(cart.getStackInSlot(j) == null || (cart.isItemValidForSlot(j, getStackInSlot(firstNonEmpty)) && cart.getStackInSlot(j).stackSize < cart.getInventoryStackLimit())) {
										hasItems = true;
									}
								}
							}
						}
						double dx = (xCoord+0.5) - cart.posX;
						double dz = (zCoord+0.5) - cart.posZ;
						if(carts.containsKey(cart)) {
							Vec3 v = carts.get(cart);
							if(!hasItems) {
								cart.motionX = ((xCoord+0.5)-v.xCoord)*0.2;
								cart.motionZ = ((zCoord+0.5)-v.zCoord)*0.2;
								if(cart.motionX != 0) {
									cart.motionX = 0.5;
								}
								if(cart.motionZ != 0) {
									cart.motionZ = 0.5;
								}
								//cart.moveEntity((xCoord-v.xCoord)*0.2, 0, (zCoord-v.zCoord)*0.2);
								cart.moveMinecartOnRail(0, 0, 0, 0);
								carts.remove(cart);
								//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
							}
							/*else {
	        					cart.setPosition(v.xCoord, v.yCoord, v.zCoord);
	        				}*/
						}
						else if(hasItems && (dx*dx+dz*dz) < 0.7) {
							carts.put(cart, Vec3.createVectorHelper(cart.posX, cart.posY, cart.posZ));
							cart.motionX = 0;
							cart.motionZ = 0;
							//cart.moveEntity((xCoord-v.xCoord)*0.2, 0, (zCoord-v.zCoord)*0.2);
							cart.moveMinecartOnRail(0, 0, 0, 0);
							cart.setPosition(cart.posX, cart.posY, cart.posZ);
						}
						//else {
						//	System.out.println("h: " + hasItems + ", d:" + (dx*dx+dz*dz));
						//}
					}
				}
			}
			for(EntityMinecartContainer cart : carts.keySet()) {
				Vec3 v = carts.get(cart);
				//EntityMinecartContainer cart = (EntityMinecartContainer) worldObj.getEntityByID(c);
				boolean hasItems = false;
				if(cart.posY > yCoord) {
					cart.setPosition(xCoord+0.5, yCoord+1.5, zCoord+0.5);
					//if cart has items...
					int firstNonEmpty = -1;
					for(int j = 0; j < cart.getSizeInventory() && !hasItems; j++) {
						if(cart.getStackInSlot(j) != null) {
							hasItems = true;
							firstNonEmpty = j;
						}
					}
					if(hasItems) {
						hasItems = false;
						//if I have room...
						for(int j = getSizeInventory() - 1; j >= 0 && !hasItems; j--) {
							if(getStackInSlot(j) == null || (isItemValidForSlot(j, cart.getStackInSlot(firstNonEmpty)) && getStackInSlot(j).stackSize < getInventoryStackLimit())) {
								hasItems = true;
							}
						}
					}
				}
				else {
					cart.setPosition(xCoord+0.5, yCoord-0.5, zCoord+0.5);
					//if I have items...
					int firstNonEmpty = -1;
					for(int j = 0; j < getSizeInventory() && !hasItems; j++) {
						if(getStackInSlot(j) != null) {
							hasItems = true;
							firstNonEmpty = j;
						}
					}
					//if cart has room...
					if(hasItems) {
						hasItems = false;
						for(int j = cart.getSizeInventory() - 1; j >= 0 && !hasItems; j--) {
							if(cart.getStackInSlot(j) == null || (cart.isItemValidForSlot(j, getStackInSlot(firstNonEmpty)) && cart.getStackInSlot(j).stackSize < cart.getInventoryStackLimit())) {
								hasItems = true;
							}
						}
					}
				}
				if(!hasItems) {
					//System.out.println("cart: " + v.xCoord+","+v.zCoord);
					//System.out.println("te:   " + (xCoord+0.5)+","+(zCoord+0.5));
					cart.motionX = ((xCoord+0.5)-v.xCoord)*0.2;
					cart.motionZ = ((zCoord+0.5)-v.zCoord)*0.2;
					//System.out.println("move: " + cart.motionX + "," + cart.motionZ);
					if(cart.motionX != 0) {
						cart.motionX = 0.5 * Math.signum(cart.motionX);
					}
					if(cart.motionZ != 0) {
						cart.motionZ = 0.5 * Math.signum(cart.motionZ);
					}
					//cart.moveEntity((xCoord-v.xCoord)*0.2, 0, (zCoord-v.zCoord)*0.2);
					cart.moveMinecartOnRail(0, 0, 0, 0);
					carts.remove(cart);
					//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (/*list != null && list.size() > 0 && */tag.hasKey("totalCarts")) {
			for(int i = tag.getInteger("totalCarts") - 1; i >=0; i--) {
				UUID idToCheck = new UUID(tag.getLong("cart_mb"+i),tag.getLong("cart_lb"+i));
				Vec3 v = Vec3.createVectorHelper(tag.getDouble("cart_x"+i), tag.getDouble("cart_y"+i), tag.getDouble("cart_z"+i));
				nbtCarts.put(idToCheck, v);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		int i = 0;
		for(EntityMinecartContainer cart : carts.keySet()) {
			tag.setLong("cart_mb"+i, cart.getPersistentID().getMostSignificantBits());
			tag.setLong("cart_lb"+i, cart.getPersistentID().getLeastSignificantBits());
			Vec3 v = carts.get(cart);
			tag.setDouble("cart_x"+i, v.xCoord);
			tag.setDouble("cart_y"+i, v.yCoord);
			tag.setDouble("cart_z"+i, v.zCoord);
			i++;
		}
		tag.setInteger("totalCarts", i);
	}
}
