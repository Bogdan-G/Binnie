// 
// Decompiled by Procyon v0.5.30
// 

package binnie.genetics.core;

import binnie.craftgui.genetics.machine.WindowSplicer;
import binnie.craftgui.genetics.machine.WindowAcclimatiser;
import binnie.craftgui.genetics.machine.WindowIncubator;
import binnie.craftgui.genetics.machine.WindowAnalyser;
import binnie.craftgui.genetics.machine.WindowInoculator;
import binnie.craftgui.genetics.machine.WindowPolymeriser;
import binnie.craftgui.genetics.machine.WindowSequencer;
import binnie.craftgui.genetics.machine.WindowIsolator;
import binnie.craftgui.genetics.machine.WindowGenepool;
import net.minecraft.tileentity.TileEntity;
import binnie.craftgui.genetics.machine.WindowGeneBankNEI;
import binnie.craftgui.genetics.machine.WindowGeneBank;
import net.minecraft.world.World;
import java.lang.reflect.Constructor;
import binnie.genetics.gui.WindowAnalyst;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayer;
import binnie.craftgui.minecraft.Window;
import binnie.core.gui.IBinnieGUID;

public enum GeneticsGUI implements IBinnieGUID
{
	Genepool(WindowGenepool.class),
	Isolator(WindowIsolator.class),
	Sequencer(WindowSequencer.class),
	Replicator(WindowPolymeriser.class),
	Inoculator(WindowInoculator.class),
	GeneBank(WindowGeneBank.class),
	Analyser(WindowAnalyser.class),
	Incubator(WindowIncubator.class),
	Database(WindowGeneBank.class),
	DatabaseNEI(WindowGeneBankNEI.class),
	Acclimatiser(WindowAcclimatiser.class),
	Splicer(WindowSplicer.class),
	Analyst,
	Registry,
	MasterRegistry;

	Class<? extends Window> windowClass;

	private GeneticsGUI()
	{
	}

	private GeneticsGUI(final Class window) {
		this.windowClass = window;
	}

	public Window getWindow(final EntityPlayer player, final IInventory object, final Side side) throws Exception {
		switch (this) {
		case Analyst: {
			return new WindowAnalyst(player, null, side, false, false);
		}
		case MasterRegistry: {
			return new WindowAnalyst(player, null, side, true, true);
		}
		case Registry: {
			return new WindowAnalyst(player, null, side, true, false);
		}
		default: {
			final Constructor constr = this.windowClass.getConstructor(EntityPlayer.class, IInventory.class, Side.class);
			return (Window) constr.newInstance(player, object, side);
		}
		}
	}

	@Override
	public Window getWindow(final EntityPlayer player, final World world, final int x, final int y, final int z, final Side side) {
		Window window = null;
		final TileEntity tileEntity = world.getTileEntity(x, y, z);
		IInventory object = null;
		if (tileEntity instanceof IInventory) {
			object = (IInventory) tileEntity;
		}
		try {
			if (this == GeneticsGUI.Database || this == GeneticsGUI.GeneBank) {
				window = WindowGeneBank.create(player, object, side);
			}
			else if (this == GeneticsGUI.DatabaseNEI) {
				window = WindowGeneBankNEI.create(player, object, side);
			}
			else {
				window = this.getWindow(player, object, side);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return window;
	}
}
