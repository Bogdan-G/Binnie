// 
// Decompiled by Procyon v0.5.30
// 

package binnie.core.triggers;

import buildcraft.api.statements.IStatementContainer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;
import buildcraft.api.statements.IStatementParameter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import binnie.Binnie;
import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.StatementManager;
import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.resource.BinnieIcon;
import buildcraft.api.statements.ITriggerExternal;

final class BinnieTrigger implements ITriggerExternal
{
	private static int incrementalID;
	protected static BinnieTrigger triggerNoBlankTemplate;
	protected static BinnieTrigger triggerNoTemplate;
	protected static BinnieTrigger triggerIsWorking;
	protected static BinnieTrigger triggerIsNotWorking;
	protected static BinnieTrigger triggerCanWork;
	protected static BinnieTrigger triggerCannotWork;
	protected static BinnieTrigger triggerPowerNone;
	protected static BinnieTrigger triggerPowerLow;
	protected static BinnieTrigger triggerPowerMedium;
	protected static BinnieTrigger triggerPowerHigh;
	protected static BinnieTrigger triggerPowerFull;
	protected static BinnieTrigger triggerSerumFull;
	protected static BinnieTrigger triggerSerumPure;
	protected static BinnieTrigger triggerSerumEmpty;
	protected static BinnieTrigger triggerAcclimatiserNone;
	protected static BinnieTrigger triggerAcclimatiserHot;
	protected static BinnieTrigger triggerAcclimatiserCold;
	protected static BinnieTrigger triggerAcclimatiserWet;
	protected static BinnieTrigger triggerAcclimatiserDry;
	private String desc;
	private String tag;
	private BinnieIcon icon;
	private int id;

	public BinnieTrigger(final String desc, final String tag, final String iconFile) {
		this(desc, tag, BinnieCore.instance, iconFile);
	}

	public BinnieTrigger(final String desc, final String tag, final AbstractMod mod, final String iconFile) {
		this.id = 0;
		this.id = BinnieTrigger.incrementalID++;
		this.tag = tag;
		StatementManager.registerStatement(this);
		TriggerProvider.triggers.add(this);
		this.icon = Binnie.Resource.getItemIcon(mod, iconFile);
		this.desc = desc;
	}

	@Override
	public String getDescription() {
		return this.desc;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IIconRegister register) {
		return this.icon.getIcon(register);
	}

	@Override
	public String getUniqueTag() {
		return this.tag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon() {
		return this.icon.getIcon();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister) {
		this.icon.registerIcon(iconRegister);
	}

	@Override
	public int maxParameters() {
		return 0;
	}

	@Override
	public int minParameters() {
		return 0;
	}

	@Override
	public IStatementParameter createParameter(final int index) {
		return null;
	}

	@Override
	public IStatement rotateLeft() {
		return null;
	}

	@Override
	public boolean isTriggerActive(final TileEntity target, final ForgeDirection side, final IStatementContainer source, final IStatementParameter[] parameters) {
		return false;
	}

	static {
		BinnieTrigger.incrementalID = 800;
	}
}
