package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S17PacketEntityLookMoveAnalyser extends PlayPacketAnalyser {
	
	public S17PacketEntityLookMoveAnalyser() {
		super("S17PacketEntityLookMove");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		boolean b = map.getClassByRefactoredName("S14PacketEntity").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		return map.getClassByRefactoredName(name).getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run1() {
		classHook.getInterfaceHook().setSuperClass(INTERFACES + "network/packet/play/server/IS14PacketEntity");
	}
}