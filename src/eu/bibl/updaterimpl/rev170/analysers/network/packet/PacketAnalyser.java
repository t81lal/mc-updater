package eu.bibl.updaterimpl.rev170.analysers.network.packet;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.util.Access;
import eu.bibl.updater.analysis.Analyser;

public class PacketAnalyser extends Analyser {
	
	public PacketAnalyser() {
		super("Packet");
		methodHooks = new MethodHook[] {
				new MethodHook("readPacket", "(L" + INTERFACES + "network/packet/IPacketBuffer;)V"),
				new MethodHook("writePacket", "(L" + INTERFACES + "network/packet/IPacketBuffer;)V") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "Key was smaller than nothing!  Weird key!");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/packet/IPacket"));
		
		int methods = 0;
		for(MethodNode m : methods(cn, "(L" + map.getClassByRefactoredName("PacketBuffer").getObfuscatedName() + ";)V")) {
			if (!Access.isAbstract(m.access))
				continue;
			addHook(methodHooks[methods++].buildObfMn(m));
		}
	}
}