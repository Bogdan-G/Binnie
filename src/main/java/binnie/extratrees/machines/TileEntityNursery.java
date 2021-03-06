// 
// Decompiled by Procyon v0.5.30
// 

package binnie.extratrees.machines;

import java.util.HashSet;
import java.util.Set;

import forestry.api.core.IErrorState;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.biome.BiomeGenBase;

import forestry.api.genetics.IIndividual;
import forestry.api.lepidopterology.IButterfly;

import net.minecraft.item.ItemStack;

import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;

import net.minecraft.world.World;

import forestry.api.lepidopterology.IButterflyNursery;
import binnie.core.machines.TileEntityMachine;

public class TileEntityNursery extends TileEntityMachine implements IButterflyNursery
{
	public TileEntityNursery(final Nursery.PackageNursery pack) {
		super(pack);
	}

	IButterflyNursery getNursery() {
		return this.getMachine().getInterface(IButterflyNursery.class);
	}

	boolean hasNursery() {
		return this.getNursery() != null;
	}

	public World getWorld() {
		return this.worldObj;
	}

	public int getXCoord() {
		return this.xCoord;
	}

	public int getYCoord() {
		return this.yCoord;
	}

	public int getZCoord() {
		return this.zCoord;
	}

	public EnumTemperature getTemperature() {
		return null;
	}

	public EnumHumidity getHumidity() {
		return null;
	}

	public boolean addProduct(final ItemStack product, final boolean all) {
		return false;
	}

	@Override
	public IButterfly getCaterpillar() {
		return this.hasNursery() ? this.getNursery().getCaterpillar() : null;
	}

	@Override
	public IIndividual getNanny() {
		return null;
	}

	@Override
	public void setCaterpillar(final IButterfly butterfly) {
		if (this.hasNursery()) {
			this.getNursery().setCaterpillar(butterfly);
		}
	}

	@Override
	public boolean canNurse(final IButterfly butterfly) {
		return this.getCaterpillar() == null;
	}

	public BiomeGenBase getBiome() {
		return this.getWorld().getBiomeGenForCoords(this.xCoord, this.zCoord);
	}

	public void setErrorState(final IErrorState state) {
	}

	public IErrorState getErrorState() {
		return null;
	}

	public boolean setErrorCondition(final boolean condition, final IErrorState errorState) {
		return false;
	}

	public Set<IErrorState> getErrorStates() {
		return new HashSet<IErrorState>();
	}

	public int getBiomeId() {
		return 0;
	}

	public void setErrorState(final int state) {
	}

	public int getErrorOrdinal() {
		return 0;
	}

	@Override
	public ChunkCoordinates getCoordinates() {
		return new ChunkCoordinates(xCoord, yCoord, zCoord);
	}
}
