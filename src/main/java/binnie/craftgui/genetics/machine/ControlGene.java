// 
// Decompiled by Procyon v0.5.30
// 

package binnie.craftgui.genetics.machine;

import binnie.genetics.core.GeneticsTexture;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.CraftGUI;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import binnie.craftgui.events.EventMouse;
import binnie.craftgui.core.Attribute;
import net.minecraft.item.ItemStack;
import binnie.genetics.genetics.Engineering;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.minecraft.Window;
import binnie.Binnie;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.ITooltip;
import binnie.genetics.api.IGene;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.controls.core.Control;

public class ControlGene extends Control implements IControlValue<IGene>, ITooltip
{
	IGene gene;

	@Override
	public void getTooltip(final Tooltip tooltip) {
		final String cName = Binnie.Genetics.getSystem(this.gene.getSpeciesRoot()).getChromosomeName(this.gene.getChromosome());
		tooltip.add(cName + ": " + this.gene.getName());
		if (this.isMouseOver() && this.canFill(Window.get(this).getHeldItemStack())) {
			tooltip.add("Left click to assign gene");
			final IGene existingGene = Engineering.getGene(Window.get(this).getHeldItemStack(), this.gene.getChromosome().ordinal());
			if (existingGene == null) {
				return;
			}
			final String dName = Binnie.Genetics.getSystem(this.gene.getSpeciesRoot()).getChromosomeName(this.gene.getChromosome());
			tooltip.add("Will replace " + dName + ": " + existingGene.getName());
		}
	}

	private boolean canFill(final ItemStack stack) {
		return stack != null && stack.stackSize == 1 && Engineering.isGeneAcceptor(stack) && Engineering.canAcceptGene(stack, this.getValue());
	}

	protected ControlGene(final IWidget parent, final float x, final float y) {
		super(parent, x, y, 16.0f, 16.0f);
		this.addAttribute(Attribute.MouseOver);
		this.addSelfEventHandler(new EventMouse.Down.Handler() {
			@Override
			public void onEvent(final EventMouse.Down event) {
				if (ControlGene.this.canFill(Window.get(ControlGene.this.getWidget()).getHeldItemStack())) {
					final NBTTagCompound action = new NBTTagCompound();
					final NBTTagCompound geneNBT = new NBTTagCompound();
					ControlGene.this.getValue().writeToNBT(geneNBT);
					action.setTag("gene", geneNBT);
					Window.get(ControlGene.this.getWidget()).sendClientAction("gene-select", action);
				}
			}
		});
	}

	@Override
	public IGene getValue() {
		return this.gene;
	}

	@Override
	public void setValue(final IGene value) {
		this.gene = value;
	}

	@Override
	public void onRenderForeground() {
	}

	@Override
	public void onRenderBackground() {
		if (this.isMouseOver() && this.canFill(Window.get(this).getHeldItemStack())) {
			CraftGUI.Render.solid(this.getArea(), -1);
			CraftGUI.Render.solid(this.getArea().inset(1), -12303292);
		}
		CraftGUI.Render.colour(-1);
		CraftGUI.Render.iconItem(IPoint.ZERO, GeneticsTexture.dnaIcon.getIcon());
	}
}
