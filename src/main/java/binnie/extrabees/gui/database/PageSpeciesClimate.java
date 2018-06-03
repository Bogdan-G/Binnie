// 
// Decompiled by Procyon v0.5.30
// 

package binnie.extrabees.gui.database;

import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.IAlleleSpecies;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.mod.database.PageSpecies;

public class PageSpeciesClimate extends PageSpecies
{
	ControlClimateBar tempBar;
	ControlClimateBar humidBar;
	ControlBiomes biomes;

	public PageSpeciesClimate(final IWidget parent, final DatabaseTab tab) {
		super(parent, tab);
		new ControlTextCentered(this, 6.0f, "Climate");
		this.tempBar = new ControlClimateBar(this, 8, 24-8, 128, 12);
		this.humidBar = new ControlClimateBar(this, 8, 42-10, 128, 12, true);
		new ControlTextCentered(this, 45.0f, "Biomes");
		this.biomes = new ControlBiomes(this, 8, 90, 8, 4);
	}

	@Override
	public void onValueChanged(final IAlleleSpecies species) {
		this.tempBar.setSpecies((IAlleleBeeSpecies) species);
		this.humidBar.setSpecies((IAlleleBeeSpecies) species);
		this.biomes.setSpecies((IAlleleBeeSpecies) species);
	}
}
