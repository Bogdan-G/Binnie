// 
// Decompiled by Procyon v0.5.30
// 

package binnie.extratrees.core;

import net.minecraft.tileentity.TileEntity;
import binnie.craftgui.extratrees.dictionary.WindowSetSquare;
import binnie.craftgui.extratrees.dictionary.WindowLepidopteristDatabase;
import binnie.craftgui.extratrees.dictionary.WindowDistillery;
import binnie.craftgui.extratrees.dictionary.WindowBrewery;
import binnie.craftgui.extratrees.dictionary.WindowPress;
import binnie.craftgui.extratrees.kitchen.WindowBottleRack;
import binnie.craftgui.extratrees.dictionary.WindowLumbermill;
import binnie.craftgui.extratrees.dictionary.WindowWoodworker;
import binnie.craftgui.extratrees.dictionary.WindowArboristDatabase;
import net.minecraft.inventory.IInventory;
import binnie.craftgui.minecraft.Window;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import binnie.core.gui.IBinnieGUID;

public enum ExtraTreesGUID implements IBinnieGUID
{
	Database,
	Woodworker,
	Lumbermill,
	DatabaseNEI,
	Incubator,
	MothDatabase,
	MothDatabaseNEI,
	Press,
	Brewery,
	Distillery,
	KitchenBottleRack,
	Infuser,
	SetSquare;

	@Override
	public Window getWindow(final EntityPlayer player, final World world, final int x, final int y, final int z, final Side side) {
		Window window = null;
		final TileEntity tileEntity = world.getTileEntity(x, y, z);
		IInventory object = null;
		if (tileEntity instanceof IInventory) {
			object = (IInventory) tileEntity;
		}
		switch (this) {
		case Database:
		case DatabaseNEI: {
			window = WindowArboristDatabase.create(player, side, this != ExtraTreesGUID.Database);
			break;
		}
		case Woodworker: {
			window = WindowWoodworker.create(player, object, side);
			break;
		}
		case Lumbermill: {
			window = WindowLumbermill.create(player, object, side);
			break;
		}
		case KitchenBottleRack: {
			window = WindowBottleRack.create(player, object, side);
			break;
		}
		case Press: {
			window = WindowPress.create(player, object, side);
			break;
		}
		case Brewery: {
			window = WindowBrewery.create(player, object, side);
			break;
		}
		case Distillery: {
			window = WindowDistillery.create(player, object, side);
			break;
		}
		case MothDatabase:
		case MothDatabaseNEI: {
			window = WindowLepidopteristDatabase.create(player, side, this != ExtraTreesGUID.MothDatabase);
			break;
		}
		case SetSquare: {
			window = WindowSetSquare.create(player, world, x, y, z, side);
			break;
		}
		}
		return window;
	}
}
