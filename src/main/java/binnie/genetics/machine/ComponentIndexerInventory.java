// 
// Decompiled by Procyon v0.5.30
// 

package binnie.genetics.machine;

import java.util.Map;
import binnie.Binnie;
import java.util.HashMap;
import java.util.ArrayList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import binnie.core.machines.inventory.SetList;
import binnie.core.machines.Machine;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.inventory.IInventory;
import binnie.core.machines.inventory.ComponentInventory;

public abstract class ComponentIndexerInventory<T> extends ComponentInventory implements IInventory
{
	int indexerSize;
	public int guiRefreshCounter;
	List<ItemStack> indexerInventory;
	public List<Integer> sortedInventory;
	T sortingMode;
	boolean needsSorting;

	public ComponentIndexerInventory(final Machine machine) {
		super(machine);
		this.indexerSize = -1;
		this.guiRefreshCounter = 0;
		this.indexerInventory = new SetList<ItemStack>();
		this.sortedInventory = new SetList<Integer>();
		this.needsSorting = true;
	}

	@Override
	public int getSizeInventory() {
		if (this.indexerSize > 0) {
			return this.indexerSize + 1;
		}
		return this.indexerInventory.size() + 1;
	}

	@Override
	public ItemStack getStackInSlot(final int index) {
		if (index >= 0 && index < this.indexerInventory.size()) {
			return this.indexerInventory.get(index);
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(final int index, final int amount) {
		if (index >= 0) {
			final ItemStack returnStack = this.getStackInSlot(index).copy();
			this.setInventorySlotContents(index, null);
			return returnStack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int var1) {
		return null;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		++this.guiRefreshCounter;
	}

	@Override
	public void setInventorySlotContents(final int index, final ItemStack itemStack) {
		if (index >= 0 && index < this.indexerInventory.size()) {
			this.indexerInventory.set(index, itemStack);
		}
		else if (itemStack != null) {
			this.indexerInventory.add(itemStack);
		}
		this.needsSorting = true;
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public void setMode(final T mode) {
		this.sortingMode = mode;
		this.needsSorting = true;
	}

	public T getMode() {
		return this.sortingMode;
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		final NBTTagList indexerNBT = new NBTTagList();
		for (final ItemStack item : this.indexerInventory) {
			final NBTTagCompound itemNBT = new NBTTagCompound();
			item.writeToNBT(itemNBT);
			indexerNBT.appendTag(itemNBT);
		}
		nbttagcompound.setTag("indexer", indexerNBT);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		final NBTTagList indexerNBT = nbttagcompound.getTagList("indexer", 10);
		this.indexerInventory.clear();
		for (int i = 0; i < indexerNBT.tagCount(); ++i) {
			final NBTTagCompound itemNBT = indexerNBT.getCompoundTagAt(i);
			this.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(itemNBT));
		}
		this.needsSorting = true;
		this.markDirty();
	}

	public abstract void Sort();

	public static class ComponentApiaristIndexerInventory extends ComponentIndexerInventory<ComponentApiaristIndexerInventory.Mode> implements IInventory
	{
		public ComponentApiaristIndexerInventory(final Machine machine) {
			super(machine);
		}

		@Override
		public void Sort() {
			int i = 0;
			while (i < this.indexerInventory.size()) {
				if (this.indexerInventory.get(i) == null) {
					this.indexerInventory.remove(i);
				}
				else {
					++i;
				}
			}
			if (!this.needsSorting) {
				return;
			}
			this.needsSorting = false;
			++this.guiRefreshCounter;
			switch (this.sortingMode) {
			case Species:
			case Type: {
				class SpeciesList
				{
					public List<Integer> drones;
					public List<Integer> queens;
					public List<Integer> princesses;
					public List<ItemStack> bees;

					SpeciesList() {
						this.drones = new ArrayList<Integer>();
						this.queens = new ArrayList<Integer>();
						this.princesses = new ArrayList<Integer>();
						this.bees = new ArrayList<ItemStack>();
					}

					public void add(final ItemStack stack) {
						this.bees.add(stack);
					}
				}
				final Map<Integer, SpeciesList> speciesList = new HashMap<Integer, SpeciesList>();
				for (final ItemStack itemStack : this.indexerInventory) {
					final int species = itemStack.getItemDamage();
					if (!speciesList.containsKey(species)) {
						speciesList.put(species, new SpeciesList());
					}
					speciesList.get(species).add(itemStack);
				}
				for (final SpeciesList sortableList : speciesList.values()) {
					for (final ItemStack beeStack : sortableList.bees) {
						if (Binnie.Genetics.getBeeRoot().isDrone(beeStack)) {
							sortableList.drones.add(this.indexerInventory.indexOf(beeStack));
						}
						else if (Binnie.Genetics.getBeeRoot().isMated(beeStack)) {
							sortableList.queens.add(this.indexerInventory.indexOf(beeStack));
						}
						else {
							sortableList.princesses.add(this.indexerInventory.indexOf(beeStack));
						}
					}
				}
				this.sortedInventory = new SetList<Integer>();
				switch (this.sortingMode) {
				case Species: {
					for (int j = 0; j < 1024; ++j) {
						if (speciesList.containsKey(j)) {
							this.sortedInventory.addAll(speciesList.get(j).queens);
							this.sortedInventory.addAll(speciesList.get(j).princesses);
							this.sortedInventory.addAll(speciesList.get(j).drones);
						}
					}
					break;
				}
				case Type: {
					for (int j = 0; j < 1024; ++j) {
						if (speciesList.containsKey(j)) {
							this.sortedInventory.addAll(speciesList.get(j).queens);
						}
					}
					for (int j = 0; j < 1024; ++j) {
						if (speciesList.containsKey(j)) {
							this.sortedInventory.addAll(speciesList.get(j).princesses);
						}
					}
					for (int j = 0; j < 1024; ++j) {
						if (speciesList.containsKey(j)) {
							this.sortedInventory.addAll(speciesList.get(j).drones);
						}
					}
					break;
				}
				}
				break;
			}
			default: {
				this.sortedInventory.clear();
				for (i = 0; i < this.indexerInventory.size(); ++i) {
					this.sortedInventory.add(i);
				}
				break;
			}
			}
		}

		@Override
		public boolean hasCustomInventoryName() {
			return false;
		}

		@Override
		public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
			return true;
		}

		public static enum Mode
		{
			None,
			Species,
			Type;
		}
	}
}
