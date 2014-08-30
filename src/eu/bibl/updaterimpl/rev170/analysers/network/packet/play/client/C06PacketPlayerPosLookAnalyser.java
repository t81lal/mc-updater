package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C06PacketPlayerPosLookAnalyser extends PlayPacketAnalyser {
	
	public C06PacketPlayerPosLookAnalyser() {
		super("C06PacketPlayerPosLook");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		boolean b = map.getClassByRefactoredName("C03PacketPlayer").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		return map.getClassByRefactoredName(name).getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run1() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/packet/play/client/IC06PacketPlayerPosLook", INTERFACES + "network/play/client/IC03PacketPlayer"));
	}
}