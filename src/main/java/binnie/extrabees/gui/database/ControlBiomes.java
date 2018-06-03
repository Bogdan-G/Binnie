// 
// Decompiled by Procyon v0.5.30
// 

package binnie.extrabees.gui.database;

import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import binnie.core.BinnieCore;
import binnie.Binnie;
import forestry.api.apiculture.IAlleleBeeSpecies;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import binnie.craftgui.core.CraftGUI;
import net.minecraft.world.biome.BiomeGenBase;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.Attribute;
import java.util.ArrayList;
import binnie.craftgui.core.IWidget;
import java.util.List;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.listbox.ControlListBox;

public class ControlBiomes extends Control implements ITooltip
{
	List<Integer> tolerated;

	public ControlBiomes(final IWidget parent, final int x, final int y, final int width, final int height) {
		super(parent, x, y - 35, width * 16, height * 16 + 64);
		this.tolerated = new ArrayList<Integer>();
		this.addAttribute(Attribute.MouseOver);
	}

	@Override
	public void getTooltip(final Tooltip list) {
		if (this.tolerated.isEmpty()) {
			return;
		}
		final int x = (int) (this.getRelativeMousePosition().x() / 8.0f);
		final int y = (int) (this.getRelativeMousePosition().y() / 8.0f);
		final int i = x + y * 16;
		if (i < this.tolerated.size()) {
			//cpw.mods.fml.common.FMLLog.info("i="+i+" tolerated="+this.tolerated.get(i)+" biomeName="+BiomeGenBase.getBiome(this.tolerated.get(i)).biomeName);
			list.add(BiomeGenBase.getBiome(this.tolerated.get(i)).biomeName);
		}
	}

	@Override
	public void onRenderForeground() {
		//for (int i = 0; i < this.tolerated.size(); ++i) {
		for (int i_0 = 0; i_0 < this.tolerated.size(); ++i_0) {
			final int i = this.tolerated.get(i_0);
			final int x = i_0 % 16 * 16 / 2;
			final int y = i_0 / 16 * 16 / 2;
			if (BiomeGenBase.getBiome(i) != null) {
				//cpw.mods.fml.common.FMLLog.info("i_0="+i_0+" i="+i+" color="+BiomeGenBase.getBiome(i).color+" biomeName="+BiomeGenBase.getBiome(i).biomeName);
				CraftGUI.Render.colour(BiomeGenBase.getBiome(i).color);
			}
			CraftGUI.Render.texture(CraftGUITexture.ButtonSmall, new IArea(x, y, 16.0f / 2.0f, 16.0f / 2.0f));
		}
	}

	public void setSpecies(final IAlleleBeeSpecies species) {
		this.tolerated.clear();
		if (species == null) {
			return;
		}
		final IBeeGenome genome = Binnie.Genetics.getBeeRoot().templateAsGenome(Binnie.Genetics.getBeeRoot().getTemplate(species.getUID()));
		final IBee bee = Binnie.Genetics.getBeeRoot().getBee(BinnieCore.proxy.getWorld(), genome);
		for (BiomeGenBase biome : bee.getSuitableBiomes()) {
			/*if (!this.tolerated.contains(biome.biomeID)) */this.tolerated.add(biome.biomeID);
		}
	}
}
