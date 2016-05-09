package com.draco18s.ores.recipes;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.RecipesUtil;
import com.draco18s.ores.OresBase;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class EtFuturumIntegration {
	private static Block alternateBlock = null;
	private static Block diorite;

	public static void registerAPIRecipes(){
		OresBase.instance.logger.log(Level.INFO,"Registering Gany's Et Futurum recipes");
		ItemStack toolmat;
		if(alternateBlock == null) {
			diorite = Block.getBlockFromName("etfuturum:stone");
			toolmat = new ItemStack(diorite, 1, 3);
		}
		else {
			diorite = alternateBlock;
			toolmat = new ItemStack(diorite, 1, 4);
		}
		RecipesUtil.RemoveRecipe(Items.stone_axe, 1, 0, "Hard Ores");
		RecipesUtil.RemoveRecipe(Items.stone_pickaxe, 1, 0, "Hard Ores");
		RecipesUtil.RemoveRecipe(Items.stone_shovel, 1, 0, "Hard Ores");
		RecipesUtil.RemoveRecipe(Items.stone_hoe, 1, 0, "Hard Ores");
    	
        GameRegistry.addRecipe(new ItemStack(Items.stone_pickaxe), new Object[] {"III", " s ", " s ", 's', Items.stick, 'I', toolmat});
        GameRegistry.addRecipe(new ItemStack(Items.stone_axe), new Object[] {"II ", "Is ", " s ", 's', Items.stick, 'I', toolmat});
        GameRegistry.addRecipe(new ItemStack(Items.stone_shovel), new Object[] {" I ", " s ", " s ", 's', Items.stick, 'I', toolmat});
        GameRegistry.addRecipe(new ItemStack(Items.stone_hoe), new Object[] {"II ", " s ", " s ", 's', Items.stick, 'I', toolmat});
	}

	public static void setAlternateBlock(Block block) {
		alternateBlock = block;
	}

	public static Block getDioriteBlock() {
		return diorite;
	}
}
