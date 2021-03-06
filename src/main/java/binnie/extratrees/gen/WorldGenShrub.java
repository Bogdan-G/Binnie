// 
// Decompiled by Procyon v0.5.30
// 

package binnie.extratrees.gen;

import forestry.api.arboriculture.ITree;
import forestry.api.world.ITreeGenData;
import forestry.arboriculture.blocks.BlockForestryLeaves;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.worldgen.BlockType;
import binnie.extratrees.worldgen.BlockTypeLeaf;

public class WorldGenShrub
{
	public static class Shrub extends WorldGenTree
	{
		public Shrub(final ITreeGenData tree) {
			super(tree);
		}

		@Override
		public void generate() {
			float leafSpawn = this.height;
			float width = this.height * this.randBetween(0.7f, 0.9f);
			if (width < 1.5f) {
				width = 1.5f;
			}
			final float f = 0.0f;
			final float h = leafSpawn;
			leafSpawn = h - 1.0f;
			this.generateCylinder(new Vector(f, h, 0.0f), 0.4f * width, 1, this.leaf, false);
			final float f2 = 0.0f;
			final float h2 = leafSpawn;
			leafSpawn = h2 - 1.0f;
			this.generateCylinder(new Vector(f2, h2, 0.0f), 0.8f * width, 1, this.leaf, false);
			while (leafSpawn >= 0.0f) {
				final float f3 = 0.0f;
				final float h3 = leafSpawn;
				leafSpawn = h3 - 1.0f;
				this.generateCylinder(new Vector(f3, h3, 0.0f), width, 1, this.leaf, false);
			}
		}

		@Override
		public void preGenerate() {
			this.minHeight = 1;
			this.height = this.determineHeight(1, 1);
			this.girth = this.determineGirth(this.treeGen.getGirth(this.world, this.startX, this.startY, this.startZ));
		}

		@Override
		public BlockType getLeaf() {
			return new BlockTypeLeaf(false);
		}
	}
}
