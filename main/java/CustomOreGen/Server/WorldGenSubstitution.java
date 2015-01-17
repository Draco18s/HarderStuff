package CustomOreGen.Server;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import CustomOreGen.Server.DistributionSettingMap.DistributionSetting;
import CustomOreGen.Util.BiomeDescriptor;
import CustomOreGen.Util.BlockDescriptor;
import CustomOreGen.Util.BlockDescriptor.BlockInfo;
import CustomOreGen.Util.GeometryStream;
import CustomOreGen.Util.TileEntityHelper;

public class WorldGenSubstitution extends WorldGenerator implements IOreDistribution
{
    @DistributionSetting(
            name = "Name",
            inherited = false,
            info = "Descriptive distribution name."
    )
    public String name;
    @DistributionSetting(
            name = "DisplayName",
            inherited = false,
            info = "Distribution name for display in user interfaces."
    )
    public String displayName;
    @DistributionSetting(
            name = "Seed",
            inherited = false,
            info = "Distribution random number seed."
    )
    public long seed;
    @DistributionSetting(
            name = "OreBlock",
            info = "Ore block(s) - total weight must not be more than 100%"
    )
    public final BlockDescriptor oreBlock;
    @DistributionSetting(
            name = "ReplaceableBlock",
            info = "List of replaceable blocks"
    )
    public final BlockDescriptor replaceableBlocks;
    @DistributionSetting(
            name = "TargetBiome",
            info = "List of valid target biomes"
    )
    public final BiomeDescriptor biomes;
    @DistributionSetting(
            name = "additionalRange",
            info = "Distance outside of current chunk to scan in every pass, in meters"
    )
    public int additionalRange;
    @DistributionSetting(
            name = "minHeight",
            info = "Minimum substitution height"
    )
    public int minHeight;
    @DistributionSetting(
            name = "maxHeight",
            info = "Maximum substitution height"
    )
    public int maxHeight;
    @DistributionSetting(
            name = "minSurfRelHeight",
            info = "Minimum surface-relative substitution height"
    )
    public int minSurfRelHeight;
    @DistributionSetting(
            name = "maxSurfRelHeight",
            info = "Maximum surface-relative substitution height"
    )
    public int maxSurfRelHeight;
    @DistributionSetting(
            name = "populatedChunks",
            info = "Chunks populated during current game session."
    )
    public int populatedChunks;
    @DistributionSetting(
            name = "placedBlocks",
            info = "Blocks placed during current game session."
    )
    public long placedBlocks;
    protected boolean _valid;
    protected final boolean _canGenerate;
    protected static final DistributionSettingMap settingMap = new DistributionSettingMap(WorldGenSubstitution.class);

    public WorldGenSubstitution(int distributionID, boolean canGenerate)
    {
        this.oreBlock = new BlockDescriptor(Blocks.stone);
        this.replaceableBlocks = new BlockDescriptor();
        this.biomes = new BiomeDescriptor(".*");
        this.additionalRange = 0;
        this.minHeight = Integer.MIN_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
        this.minSurfRelHeight = Integer.MIN_VALUE;
        this.maxSurfRelHeight = Integer.MAX_VALUE;
        this.populatedChunks = 0;
        this.placedBlocks = 0L;
        this._valid = false;
        this.name = "Substitute_" + distributionID;
        this.seed = (new Random((long)distributionID)).nextLong();
        this._canGenerate = canGenerate;
    }

    public void inheritFrom(IOreDistribution inherits) throws IllegalArgumentException
    {
        if (inherits != null && inherits instanceof WorldGenSubstitution)
        {
            settingMap.inheritAll((WorldGenSubstitution)inherits, this);
            this._valid = false;
        }
        else
        {
            throw new IllegalArgumentException("Invalid source distribution \'" + inherits + "\'");
        }
    }

    public Map getDistributionSettings()
    {
        return settingMap.getDescriptions();
    }

    public Object getDistributionSetting(String settingName)
    {
        return settingMap.get(this, settingName);
    }

    public void setDistributionSetting(String settingName, Object value) throws IllegalArgumentException, IllegalAccessException
    {
        settingMap.set(this, settingName, value);
    }

    public void generate(World world, int chunkX, int chunkZ) {}

    public void populate(World world, int chunkX, int chunkZ)
    {
        if (this._canGenerate && this._valid && this.oreBlock != null)
        {
            Random random = new Random(world.getSeed());
            long xSeed = random.nextLong() >> 3;
            long zSeed = random.nextLong() >> 3;
            random.setSeed(xSeed * (long)chunkX + zSeed * (long)chunkZ ^ world.getSeed() ^ this.seed);
            this.generate(world, random, chunkX * 16, 0, chunkZ * 16);
        }
    }

    public void cull() {}

    public void clear()
    {
        this.populatedChunks = 0;
        this.placedBlocks = 0L;
    }

    public GeometryStream getDebuggingGeometry(World world, int chunkX, int chunkZ)
    {
        return null;
    }

    public boolean validate() throws IllegalStateException
    {
        this._valid = true;
        float oreBlockMatchWeight = this.oreBlock.getTotalMatchWeight();

        if (oreBlockMatchWeight <= 0.0F)
        {
            if (this._canGenerate)
            {
                this._valid = false;
                throw new IllegalStateException("Ore block descriptor for " + this + " is empty or does not match any registered blocks.");
            }
        }
        else if (oreBlockMatchWeight > 1.0F)
        {
            this._valid = false;
            throw new IllegalStateException("Ore block descriptor for " + this + " is overspecified with a total match weight of " + oreBlockMatchWeight * 100.0F + "%.");
        }

        float replBlockMatchWeight = this.replaceableBlocks.getTotalMatchWeight();

        if (replBlockMatchWeight <= 0.0F)
        {
            ;
        }

        float biomeMatchWeight = this.biomes.getTotalMatchWeight();

        if (biomeMatchWeight <= 0.0F)
        {
            ;
        }

        if (this.additionalRange < 0)
        {
            this._valid = false;
            throw new IllegalStateException("Invalid additional scan range \'" + this.additionalRange + "\' for " + this);
        }
        else if (this.minHeight > this.maxHeight)
        {
            this._valid = false;
            throw new IllegalStateException("Invalid height range [" + this.minHeight + "," + this.maxHeight + "] for " + this);
        }
        else if (this.minSurfRelHeight > this.maxSurfRelHeight)
        {
            this._valid = false;
            throw new IllegalStateException("Invalid height range [" + this.minSurfRelHeight + "," + this.maxSurfRelHeight + "] for " + this);
        }
        else
        {
            return this._valid && this._canGenerate;
        }
    }

    public boolean generate(World world, Random random, int depositX, int depositY, int depositZ)
    {
        if (this._canGenerate && this._valid && this.oreBlock != null)
        {
        	int depositCX = depositX / 16;
            int depositCZ = depositZ / 16;
            int cRange = (this.additionalRange + 15) / 16;
            int hRange = (this.additionalRange + 7) / 8;
            int minh = Math.max(0, this.minHeight);
            int maxh = Math.min(world.getHeight() - 1, this.maxHeight);

            for (int dCX = -cRange; dCX <= cRange; ++dCX)
            {
                for (int dCZ = -cRange; dCZ <= cRange; ++dCZ)
                {
                	int chunkZ = depositCZ + dCZ;
                    int chunkX = depositCX + dCX;

                    if (world.blockExists(chunkX * 16, 0, chunkZ * 16))
                    {
                        Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
                        int minX = dCX < 0 && -dCX * 2 > hRange ? 8 : 0;
                        int minZ = dCZ < 0 && -dCZ * 2 > hRange ? 8 : 0;
                        int maxX = dCX > 0 && dCX * 2 > hRange ? 8 : 16;
                        int maxZ = dCZ > 0 && dCZ * 2 > hRange ? 8 : 16;

                        for (int x = minX; x < maxX; ++x)
                        {
                            for (int z = minZ; z < maxZ; ++z)
                            {
                            	BiomeGenBase biome = chunk.getBiomeGenForWorldCoords(x, z, world.provider.worldChunkMgr);

                                if (biome == null || this.biomes.getWeight(biome) > 0.5F)
                                {
                                	int xzminh = minh;
                                	int xzmaxh = maxh;
                                	if (this.minSurfRelHeight != Integer.MIN_VALUE || this.maxSurfRelHeight != Integer.MAX_VALUE) {
                                		int surfh = findSurfaceHeight(chunk, x, z);
	                                	xzminh = Math.max(xzminh, this.minSurfRelHeight + surfh);
	                                	xzmaxh = Math.min(xzmaxh, this.maxSurfRelHeight + 
	                                			                  Math.min(surfh, Integer.MAX_VALUE - this.maxSurfRelHeight));
                                	}
                                    for (int y = xzminh; y <= xzmaxh; ++y)
                                    {
                                        Block currentBlock = chunk.getBlock(x, y, z);
                                        int fastCheck = this.replaceableBlocks.matchesBlock_fast(currentBlock);

                                        if (fastCheck != 0 && (fastCheck != -1 || this.replaceableBlocks.matchesBlock(currentBlock, chunk.getBlockMetadata(x, y, z), random)))
                                        {
                                        	int worldX = chunkX * 16 + x;
                                        	int worldZ = chunkZ * 16 + z;
                                            BlockInfo match = this.oreBlock.getMatchingBlock(random);
                                            int matchMeta = match.getMetadata();
                                            Block matchBlock = match.getBlock();
                                            if (match != null && matchBlock.canBlockStay(world, worldX, y, worldZ) && 
                                            	world.setBlock(worldX, y, worldZ, matchBlock, matchMeta, 2))
                                            {
                                                ++this.placedBlocks;
                                                TileEntityHelper.readFromPartialNBT(world, worldX, y, worldZ, match.getNBT());
                                                world.markBlockForUpdate(worldX, y, worldZ);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ++this.populatedChunks;
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isSurfaceBlock(Block block) {
    	Material material = block.getMaterial();
    	return 
    	  material == Material.clay || 
		  material == Material.grass || 
		  material == Material.ground || 
		  material == Material.ice ||
		  material == Material.rock ||
		  material == Material.sand;
    }
    
    private int findSurfaceHeight(Chunk chunk, int x, int z) {
    	int surfh = chunk.getHeightValue(x, z);
		while (surfh > 0 && !isSurfaceBlock(chunk.getBlock(x, surfh, z))) 
		{
			surfh--;
		}
		return surfh;
	}

	public String toString()
    {
        return this.name;
    }

	@Override
	public double getOresPerChunk() {
		return this.maxHeight - this.minHeight;
	}
}
