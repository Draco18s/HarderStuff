package com.draco18s.ores.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntitySluiceBottom;
import com.draco18s.ores.entities.TileEntitySluiceBottom;
import com.draco18s.ores.entities.TileEntityWindmill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSluiceBottom extends BlockContainer {
	private ItemStack[] inventory;
	private IIcon[] icons;
	public int renderID;

	public BlockSluiceBottom() {
		super(OresBase.sluiceBox);
		setBlockName("sluice");
		setHardness(2.0f);
		setHarvestLevel("axe", 1);
		setResistance(2.0f);
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockTextureName("ores:sluice-icon");
		setBlockBounds(0, 0, 0, 1, 0.25f, 1);
	}

	@Override
	public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
        return false;
    }
	
	@Override
	public int getRenderType() {
        return renderID;
    }
	
	@Override
    public int getRenderBlockPass() {
		return 1;
	}
	
	@Override
	public boolean canRenderInPass(int pass) {
		OresBase.proxy.renderPass = pass;
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySluiceBottom();
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        setMeta(world, x, y, z);
    }
	
	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
        return false;
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		super.onNeighborBlockChange(world, x, y, z, b);
		if(!canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
		}
		else {
			setMeta(world, x, y, z);
		}
	}
	
	/*@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[meta];
    }

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int p) {
        return this.icons[world.getBlockMetadata(x, y, z)];
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[16];
		for(int i=0; i < 16; i++) {
			icons[i] = iconRegister.registerIcon("ores:meta"+i);
		}
    }*/

    private void setMeta(World world, int x, int y, int z) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            Material m = block.getMaterial();
            Material m1 = block1.getMaterial();
            Material m2 = block2.getMaterial();
            Material m3 = block3.getMaterial();
            byte b0 = 5;

        	byte me = 0, f = 1;
        	int watermeta = 0;
            if (b0 == 5 && block == Blocks.water && block1 != Blocks.water) {
				b0 = 1;
            }

            if (b0 == 5 && (block1 == Blocks.water && block != Blocks.water)) {
				b0 = 0;
            }

            if (b0 == 5 && (block2 == Blocks.water && block3 != Blocks.water)) {
				b0 = 3;
            }

            if (b0 == 5 && (block3 == Blocks.water && block2 != Blocks.water)) {
				b0 = 2;
            }
            if (b0 == 5 && block == Blocks.flowing_water && block1 != Blocks.flowing_water) {
				b0 = 1;
            }

            if (b0 == 5 && (block1 == Blocks.flowing_water && block != Blocks.flowing_water)) {
				b0 = 0;
            }

            if (b0 == 5 && (block2 == Blocks.flowing_water && block3 != Blocks.flowing_water)) {
				b0 = 3;
            }

            if (b0 == 5 && (block3 == Blocks.flowing_water && block2 != Blocks.flowing_water)) {
				b0 = 2;
            }
            if(b0 == 5) {
            	//sSystem.out.println("Searching...");
            	boolean flag = true;
            	boolean flag1 = false;
            	int xx = 0, zz = 0;
            	for(me = 0; flag && me < 4; me+=(flag)?1:0) {
            		xx = 0;
            		zz = 0;
            		f = 0;
            		flag1 = false;
	            	do {
	            		++f;
	        			switch(me) {
	        				case 0:
	        					zz++;
	        					break;
	        				case 1:
	        					zz--;
	        					break;
	        				case 2:
	        					xx++;
	        					break;
	        				case 3:
	        					xx--;
	        					break;
	        			}
	        			//System.out.println(me + ", Offset: " + xx + "," + zz);
	        			//System.out.println(world.getBlock(x+xx, y, z+zz));
	        			if(world.getBlock(x+xx, y, z+zz) == Blocks.water || world.getBlock(x+xx, y, z+zz) == Blocks.flowing_water) {
	        				watermeta = world.getBlockMetadata(x+xx, y, z+zz);
	        				flag = false;
	        			}
	        			else if(world.getBlock(x+xx, y, z+zz) == this) {
	        				int q = world.getBlockMetadata(x+xx, y, z+zz);
	        				System.out.println("Found self:" + q);
	        				if(q > 4) {
	        					//xx = 15;
	        					flag1 = true;
	        				}
	        			}
	        			else {
	        				f = 15;
	        			}
	        		} while(f <= 3 && flag);
            	}
            	if(me < 4)
            		b0 = me;
            	if(flag1) {
            		b0 += 7;
            	}
            }
            //System.out.println("(A) Meta is: " + b0);
            
            if(me > 15) {
            	//this.dropBlockAsItem(world, x, y, z, 0, 0);
                //world.setBlockToAir(x, y, z);
            	//return;
            	b0 = 0;
            }
            if((b0 == 0 || b0 == 1) && (!block2.isBlockNormalCube() || !block3.isBlockNormalCube() || m2 == Material.ground || m3 == Material.ground || m2 == Material.grass || m3 == Material.grass || m2 == Material.sand || m3 == Material.sand)) {
            	b0 += 7;
            }
            if((b0 == 2 || b0 == 3) && (!block.isBlockNormalCube()  || !block1.isBlockNormalCube() || m  == Material.ground || m1 == Material.ground || m  == Material.grass || m1 == Material.grass || m2 == Material.sand || m3 == Material.sand)) {
            	b0 += 7;
            }
            //System.out.println("(B) Meta is: " + b0);
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            tileEntity.blockMetadata = b0;
            tileEntity.blockType = this;
            ((TileEntitySluiceBottom)tileEntity).updateFlow();
            world.setBlockMetadataWithNotify(x, y, z, b0, 3);
        }
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, block, par6);
	}

	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		TileEntitySluiceBottom ts = (TileEntitySluiceBottom) tileEntity;
		ts.setNoWater();
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			
			if(i == 1 && rand.nextFloat() > ts.getTime()/400f) {
				continue;
			}
			//System.out.println(item + " : " + ((item!=null)?item.stackSize:"0"));
			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world,
						x + rx, y + ry, z + rz,
						new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
		int q = ts.getDirt() / 5;
		while(q > 0) {
			int s = Math.min(q, 64);
			q -= 64;
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityItem = new EntityItem(world,
					x + rx, y + ry, z + rz,
					new ItemStack(Blocks.gravel, s));

			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntityInWorld(entityItem);
		}
	}
	
	/*@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (world.isRemote) {
			return true;
		}
		else {
			player.openGui(OresBase.instance, 2, world, x, y, z);
			return true;
		}
	}*/
	
	/*public boolean hasComparatorInputOverride() {
        return true;
    }
	
    public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_) {
    	TileEntitySluiceBottom ts = (TileEntitySluiceBottom)world.getTileEntity(x, y, z);
    	if(world.isBlockIndirectlyGettingPowered(x, y, z)) {
    		//output buffer
    		//return ts.getComparatorValue();
    		if(ts.checkWater()) {
    			return Container.calcRedstoneFromInventory(ts);
    		}
    		else {
    			return 15;
    		}
    	}
    	else {
    		//input buffer
    		//0: empty
			//1-4: Blue (16 blocks per)
			//5-8: green (also 16 blocks per)
			//9-12: yellow (32 blocks per)
			//13-14: orange
			//15: red.
    		int d = ts.getDirt();
    		if(d <= 640) {
    			return d/80;//blue, green
    		}
    		else if(d <= 1280) {
    			d -= 640;
    			return (d / 160)+8;//yellow
    		}
    		else if(d <= 5120) {
    			d -= 640;
    			return (d / 1920)+12;//orange
    		}
    		else {
    			return 15;//red
    		}
    	}
    }*/
    
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    	/*Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        if(block == this || block1 == this || block2 == this || block3 == this) {
        	return true;
        }
        else if(block == Blocks.water || block1 == Blocks.water || block2 == Blocks.water || block3 == Blocks.water) {
        	return true;
        }
        return false;*/
    	return world.getBlock(x, y-1, z).isSideSolid(world, x, y, z, ForgeDirection.UP);
    }
}
