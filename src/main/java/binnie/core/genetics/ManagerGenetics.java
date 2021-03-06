// 
// Decompiled by Procyon v0.5.30
// 

package binnie.core.genetics;

import binnie.botany.genetics.AlleleColor;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IAlleleFloat;
import forestry.api.genetics.IGenome;
import java.util.Comparator;
import java.util.TreeSet;
import binnie.Binnie;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import forestry.api.genetics.IIndividual;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import forestry.api.genetics.EnumTolerance;
import binnie.botany.api.IFlowerRoot;
import forestry.api.lepidopterology.IButterflyRoot;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.arboriculture.ITreeRoot;
import net.minecraft.world.World;
import forestry.api.genetics.IAlleleSpecies;
import binnie.core.BinnieCore;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IChromosomeType;
import java.util.List;
import forestry.api.genetics.ISpeciesRoot;
import java.util.Map;
import binnie.core.ManagerBase;

public class ManagerGenetics extends ManagerBase
{
	public BreedingSystem beeBreedingSystem;
	public BreedingSystem treeBreedingSystem;
	public BreedingSystem mothBreedingSystem;
	public BreedingSystem flowerBreedingSystem;
	private final Map<ISpeciesRoot, BreedingSystem> BREEDING_SYSTEMS;
	private List<IChromosomeType> invalidChromosomeTypes;
	private Map<ISpeciesRoot, Map<IChromosomeType, List<IAllele>>> chromosomeArray;

	public ManagerGenetics() {
		this.BREEDING_SYSTEMS = new LinkedHashMap<ISpeciesRoot, BreedingSystem>();
		this.invalidChromosomeTypes = new ArrayList<IChromosomeType>();
		this.chromosomeArray = new LinkedHashMap<ISpeciesRoot, Map<IChromosomeType, List<IAllele>>>();
	}

	@Override
	public void init() {
		if (BinnieCore.isApicultureActive()) {
			this.beeBreedingSystem = new BeeBreedingSystem();
		}
		if (BinnieCore.isArboricultureActive()) {
			this.treeBreedingSystem = new TreeBreedingSystem();
		}
		if (BinnieCore.isLepidopteryActive()) {
			this.mothBreedingSystem = new MothBreedingSystem();
		}
		if (BinnieCore.isBotanyActive()) {
			this.flowerBreedingSystem = new FlowerBreedingSystem();
		}
	}

	@Override
	public void postInit() {
		this.refreshData();
	}

	public boolean isSpeciesDiscovered(final IAlleleSpecies species, final World world, final boolean nei) {
		return true;
	}

	public ITreeRoot getTreeRoot() {
		return (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
	}

	public IBeeRoot getBeeRoot() {
		return (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
	}

	public IButterflyRoot getButterflyRoot() {
		return (IButterflyRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootButterflies");
	}

	public IFlowerRoot getFlowerRoot() {
		return (IFlowerRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootFlowers");
	}

	public BreedingSystem getSystem(final String string) {
		for (final BreedingSystem system : this.BREEDING_SYSTEMS.values()) {
			if (system.getIdent().equals(string)) {
				return system;
			}
		}
		return null;
	}

	public BreedingSystem getSystem(final ISpeciesRoot root) {
		// Fix NPE
		if (root == null) return null;
		return this.getSystem(root.getUID());
	}

	public ISpeciesRoot getSpeciesRoot(final IAlleleSpecies species) {
		for (final ISpeciesRoot root : AlleleManager.alleleRegistry.getSpeciesRoot().values()) {
			if (root.getKaryotype()[0].getAlleleClass().isInstance(species)) {
				return root;
			}
		}
		return null;
	}

	public IAllele getToleranceAllele(final EnumTolerance tol) {
		return AlleleManager.alleleRegistry.getAllele(Tolerance.values()[tol.ordinal()].getUID());
	}

	public int[] getTolerance(final EnumTolerance tol) {
		return Tolerance.values()[tol.ordinal()].getBounds();
	}

	public Collection<BreedingSystem> getActiveSystems() {
		return this.BREEDING_SYSTEMS.values();
	}

	public void registerBreedingSystem(final BreedingSystem system) {
		this.BREEDING_SYSTEMS.put(system.getSpeciesRoot(), system);
	}

	public BreedingSystem getConversionSystem(final ItemStack stack) {
		for (final BreedingSystem system : this.getActiveSystems()) {
			if (system.getConversion(stack) != null) {
				return system;
			}
		}
		return null;
	}

	public ItemStack getConversionStack(final ItemStack stack) {
		final BreedingSystem system = this.getConversionSystem(stack);
		return (system == null) ? null : system.getConversionStack(stack);
	}

	public IIndividual getConversion(final ItemStack stack) {
		final BreedingSystem system = this.getConversionSystem(stack);
		return (system == null) ? null : system.getConversion(stack);
	}

	@SubscribeEvent
	public void onWorldLoad(final WorldEvent.Load event) {
		this.refreshData();
	}

	private void refreshData() {
		this.loadAlleles();
		for (final BreedingSystem system : Binnie.Genetics.getActiveSystems()) {
			system.calculateArrays();
		}
	}

	private void loadAlleles() {
		this.invalidChromosomeTypes.clear();
		for (final ISpeciesRoot root : AlleleManager.alleleRegistry.getSpeciesRoot().values()) {
			final BreedingSystem system = this.getSystem(root);
			final Map<IChromosomeType, List<IAllele>> chromosomeMap = new LinkedHashMap<IChromosomeType, List<IAllele>>();
			for (final IChromosomeType chromosome : root.getKaryotype()) {
				final TreeSet<IAllele> alleles = new TreeSet<IAllele>(new ComparatorAllele());
				for (final IIndividual individual : root.getIndividualTemplates()) {
					final IGenome genome = individual.getGenome();
					try {
						final IAllele a1 = genome.getActiveAllele(chromosome);
						final IAllele a2 = genome.getInactiveAllele(chromosome);
						if (chromosome.getAlleleClass().isInstance(a1)) {
							alleles.add(a1);
						}
						if (!chromosome.getAlleleClass().isInstance(a2)) {
							continue;
						}
						alleles.add(a2);
					} catch (Exception ex) {
					}
				}
				system.addExtraAlleles(chromosome, alleles);
				if (alleles.size() == 0) {
					this.invalidChromosomeTypes.add(chromosome);
				}
				else {
					final List<IAllele> alleleList = new ArrayList<IAllele>();
					alleleList.addAll(alleles);
					chromosomeMap.put(chromosome, alleleList);
				}
			}
			this.chromosomeArray.put(root, chromosomeMap);
		}
	}

	public Map<IChromosomeType, List<IAllele>> getChromosomeMap(final ISpeciesRoot root) {
		return this.chromosomeArray.get(root);
	}

	public Collection<IChromosomeType> getActiveChromosomes(final ISpeciesRoot root) {
		return this.getChromosomeMap(root).keySet();
	}

	public boolean isInvalidChromosome(final IChromosomeType type) {
		return this.invalidChromosomeTypes.contains(type);
	}

	static class ComparatorAllele implements Comparator<IAllele>
	{
		@Override
		public int compare(final IAllele o1, final IAllele o2) {
			if (o1 == null || o2 == null) {
				throw new NullPointerException("Allele is null!");
			}
			if (o1 instanceof IAlleleFloat && o2 instanceof IAlleleFloat) {
				return Float.valueOf(((IAlleleFloat) o1).getValue()).compareTo(Float.valueOf(((IAlleleFloat) o2).getValue()));
			}
			if (o1 instanceof IAlleleInteger && o2 instanceof IAlleleInteger && !(o1 instanceof AlleleColor)) {
				return Integer.valueOf(((IAlleleInteger) o1).getValue()).compareTo(Integer.valueOf(((IAlleleInteger) o2).getValue()));
			}
			if (o1.getName() != null && o2.getName() != null) {
				return o1.getName().compareTo(o2.getName());
			}
			return o1.getUID().compareTo(o2.getUID());
		}
	}
}
