// 
// Decompiled by Procyon v0.5.30
// 

package binnie.extratrees.carpentry;

import binnie.botany.genetics.EnumFlowerColor;
import java.util.LinkedHashMap;
import binnie.core.block.TileEntityMetadata;
import binnie.botany.Botany;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import java.util.Map;
import binnie.extratrees.api.IDesignMaterial;

public class GlassType implements IDesignMaterial
{
	static Map<Integer, GlassType> types;
	String name;
	int colour;
	int id;

	private GlassType(final int id, final String name, final int colour) {
		this.id = id;
		this.name = name;
		this.colour = colour;
	}

	@Override
	public ItemStack getStack() {
		if (this.id < 128) {
			return new ItemStack(Blocks.stained_glass, 1, this.id);
		}
		return TileEntityMetadata.getItemStack(Botany.stained, this.id - 128);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getColour() {
		return this.colour;
	}

	public static int getIndex(final IDesignMaterial id) {
		for (final Map.Entry<Integer, GlassType> entry : GlassType.types.entrySet()) {
			if (entry.getValue() == id) {
				return entry.getKey();
			}
		}
		return 0;
	}

	public static IDesignMaterial get(final int id) {
		return GlassType.types.get(id);
	}

	public static IDesignMaterial get(final ItemStack stack) {
		if (stack == null) {
			return null;
		}
		for (final Map.Entry<Integer, GlassType> entry : GlassType.types.entrySet()) {
			if (stack.isItemEqual(entry.getValue().getStack())) {
				return entry.getValue();
			}
		}
		return null;
	}

	static {
		GlassType.types = new LinkedHashMap<Integer, GlassType>();
		for (final StandardColor c : StandardColor.values()) {
			GlassType.types.put(c.ordinal(), new GlassType(c.ordinal(), c.name, c.colour));
		}
		for (final EnumFlowerColor c2 : EnumFlowerColor.values()) {
			GlassType.types.put(128 + c2.ordinal(), new GlassType(128 + c2.ordinal(), c2.getName(), c2.getColor(false)));
		}
	}

	private enum StandardColor
	{
		White("White", 16777215),
		Orange("Orange", 14188339),
		Magenta("Magenta", 11685080),
		LightBlue("Light Blue", 6724056),
		Yellow("Yellow", 15066419),
		Lime("Lime", 8375321),
		Pink("Pink", 15892389),
		Gray("Gray", 5000268),
		LightGray("Light Gray", 10066329),
		Cyan("Cyan", 5013401),
		Purple("Purple", 8339378),
		Blue("Blue", 3361970),
		Brown("Brown", 6704179),
		Green("Green", 6717235),
		Red("Red", 10040115),
		Black("Black", 1644825);

		String name;
		int colour;

		private StandardColor(final String name, final int colour) {
			this.name = name;
			this.colour = colour;
		}
	}
}
