// 
// Decompiled by Procyon v0.5.30
// 

package binnie.core;

import cpw.mods.fml.relauncher.Side;
import binnie.core.network.packet.MessageBinnie;
import cpw.mods.fml.common.network.NetworkRegistry;
import binnie.core.mod.parser.FieldParser;
import binnie.Binnie;
import binnie.core.network.BinniePacketHandler;
import binnie.core.proxy.IProxyCore;
import binnie.core.gui.IBinnieGUID;
import binnie.core.network.IPacketID;
import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import binnie.core.network.IPacketProvider;

public abstract class AbstractMod implements IPacketProvider, IInitializable
{
	private SimpleNetworkWrapper wrapper;
	private LinkedHashSet<Field> fields;
	protected List<IInitializable> modules;

	public AbstractMod() {
		this.fields = new LinkedHashSet<Field>();
		this.modules = new ArrayList<IInitializable>();
		BinnieCore.registerMod(this);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public abstract boolean isActive();

	@Override
	public abstract String getChannel();

	@Override
	public IPacketID[] getPacketIDs() {
		return new IPacketID[0];
	}

	public IBinnieGUID[] getGUIDs() {
		return new IBinnieGUID[0];
	}

	public Class[] getConfigs() {
		return new Class[0];
	}

	public abstract IProxyCore getProxy();

	public abstract String getModID();

	public SimpleNetworkWrapper getNetworkWrapper() {
		return this.wrapper;
	}

	protected abstract Class<? extends BinniePacketHandler> getPacketHandler();

	@Override
	public void preInit() {
		if (!this.isActive()) {
			return;
		}
		if (this.getConfigs() != null) {
			for (final Class cls : this.getConfigs()) {
				Binnie.Configuration.registerConfiguration(cls, this);
			}
		}
		this.getProxy().preInit();
		for (final IInitializable module : this.modules) {
			module.preInit();
		}
		for (final Field field : this.getClass().getFields()) {
			this.fields.add(field);
		}
		for (final Class cls : this.getClass().getClasses()) {
			for (final Field field2 : this.getClass().getFields()) {
				this.fields.add(field2);
			}
		}
		for (final IInitializable module : this.modules) {
			for (final Field field3 : module.getClass().getFields()) {
				this.fields.add(field3);
			}
		}
		for (final Field field4 : this.fields) {
			try {
				FieldParser.preInitParse(field4, this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void init() {
		if (!this.isActive()) {
			return;
		}
		this.getProxy().init();
		(this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(this.getChannel())).registerMessage((Class) this.getPacketHandler(), (Class) MessageBinnie.class, 1, Side.CLIENT);
		this.wrapper.registerMessage((Class) this.getPacketHandler(), (Class) MessageBinnie.class, 1, Side.SERVER);
		for (final IInitializable module : this.modules) {
			module.init();
		}
		for (final Field field : this.fields) {
			try {
				FieldParser.initParse(field, this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void postInit() {
		if (!this.isActive()) {
			return;
		}
		this.getProxy().postInit();
		for (final IInitializable module : this.modules) {
			module.postInit();
		}
		for (final Field field : this.fields) {
			try {
				FieldParser.postInitParse(field, this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected final void addModule(final IInitializable init) {
		this.modules.add(init);
		MinecraftForge.EVENT_BUS.register(init);
	}
}
