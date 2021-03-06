// 
// Decompiled by Procyon v0.5.30
// 

package binnie.core.network.packet;

import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.ByteArrayInputStream;
import net.minecraft.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import java.io.IOException;

public class MessageBase
{
	private int id;

	public MessageBase(final int id) {
		this.id = id;
	}

	public MessageBase(final MessageBinnie message) {
		try {
			this.readData(message.data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MessageBinnie GetMessage() {
		return new MessageBinnie(this.id, this);
	}

	protected NBTTagCompound readNBTTagCompound(final ByteBuf data) throws IOException {
		final short length = data.readShort();
		if (length < 0) {
			return null;
		}
		final byte[] compressed = new byte[length];
		data.readBytes(compressed);
		return CompressedStreamTools.readCompressed(new ByteArrayInputStream(compressed));
	}

	protected void writeNBTTagCompound(final NBTTagCompound nbttagcompound, final ByteBuf data) throws IOException {
		if (nbttagcompound == null) {
			data.writeShort(-1);
		}
		else {
			final byte[] compressed = CompressedStreamTools.compress(nbttagcompound);
			data.writeShort((short) compressed.length);
			data.writeBytes(compressed);
		}
	}

	public void writeData(final ByteBuf data) throws IOException {
	}

	public void readData(final ByteBuf data) throws IOException {
	}
}
