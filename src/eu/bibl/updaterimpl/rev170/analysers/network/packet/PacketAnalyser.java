package eu.bibl.updaterimpl.rev170.analysers.network.packet;

import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class PacketAnalyser extends Analyser {
	
	public PacketAnalyser(ClassContainer container, HookMap hookMap) {
		super("Packet", container, hookMap);
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData(new MappingData("readPacket"), new MappingData("(L" + MinecraftAnalyser.INTERFACES + "network/packet/IPacketBuffer;)V"), null, null, false),
				new CallbackMappingData(new MappingData("writePacket"), new MappingData("(L" + MinecraftAnalyser.INTERFACES + "network/packet/IPacketBuffer;)V"), null, null, false) };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "Key was smaller than nothing!  Weird key!");
	}
	
	@Override
	public InterfaceMappingData run() {
		int methods = 0;
		for (MethodNode m : InsnUtil.methods(cn, "(L" + hookMap.getClassByRefactoredName("PacketBuffer").getObfuscatedName() + ";)V")) {
			if (!((m.access & ACC_ABSTRACT) != 0))
				continue;
			addMethod(methodHooks[methods++].buildObfMethod(m));
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/IPacket");
	}
}
