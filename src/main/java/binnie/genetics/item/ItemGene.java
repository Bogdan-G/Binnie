// 
// Decompiled by Procyon v0.5.30
// 

package binnie.genetics.item;

import net.minecraft.creativetab.CreativeTabs;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import binnie.genetics.genetics.IGeneItem;
import net.minecraft.item.ItemStack;
import binnie.genetics.CreativeTabGenetics;
import binnie.genetics.Genetics;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;

public abstract class ItemGene extends Item
{
	IIcon[] icons;

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		return this.icons[pass];
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister register) {
		this.icons[0] = Genetics.proxy.getIcon(register, "machines/serum.glass");
		this.icons[1] = Genetics.proxy.getIcon(register, "machines/serum.cap");
		this.icons[2] = Genetics.proxy.getIcon(register, "machines/serum.edges");
		this.icons[3] = Genetics.proxy.getIcon(register, "machines/serum.dna");
	}

	@Override
	public int getRenderPasses(final int metadata) {
		return 4;
	}

	public ItemGene(final String unlocName) {
		this.icons = new IIcon[4];
		this.setMaxStackSize(1);
		this.setMaxDamage(16);
		this.setUnlocalizedName(unlocName);
		this.setCreativeTab(CreativeTabGenetics.instance);
	}

	@Override
	public int getColorFromItemStack(final ItemStack itemstack, final int j) {
		final IGeneItem gene = this.getGeneItem(itemstack);
		return gene.getColour(j);
	}

	public int getCharges(final ItemStack stack) {
		return (stack == null) ? 0 : (stack.getItem().getMaxDamage() - stack.getItemDamage());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemstack, final EntityPlayer entityPlayer, final List list, final boolean par4) {
		super.addInformation(itemstack, entityPlayer, list, par4);
		final int damage = this.getMaxDamage() - itemstack.getItemDamage();
		if (damage == 0) {
			list.add("Empty");
		}
		else if (damage == 1) {
			list.add("1 Charge");
		}
		else {
			list.add(damage + " Charges");
		}
		final IGeneItem gene = this.getGeneItem(itemstack);
		gene.getInfo(list);
	}

	@Override
	public abstract String getItemStackDisplayName(final ItemStack p0);

	@Override
	public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List itemList) {
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	public abstract IGeneItem getGeneItem(final ItemStack p0);
}
