package eu.bibl.updaterimpl.rev170.analysers.network.packet;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class PacketBufferAnalyser extends Analyser {
	
	public PacketBufferAnalyser() {
		super("PacketBuffer");
		hooks = new FieldHook[] { new FieldHook("getPacketBuffer", "Lio/netty/buffer/ByteBuf;", "Lio/netty/buffer/ByteBuf;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "VarInt too big");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/packet/IPacketBuffer"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "Lio/netty/buffer/ByteBuf;").get(0)));
	}
}