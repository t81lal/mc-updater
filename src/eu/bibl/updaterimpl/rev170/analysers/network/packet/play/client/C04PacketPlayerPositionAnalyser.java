package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C04PacketPlayerPositionAnalyser extends PlayPacketAnalyser {
	
	public C04PacketPlayerPositionAnalyser() {
		super("C04PacketPlayerPosition");
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
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/packet/play/client/IC04PacketPlayerPosition", INTERFACES + "network/play/client/IC03PacketPlayer"));
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		C03PacketPlayerAnalyser analyser = (C03PacketPlayerAnalyser) analysers.get("C03PacketPlayer");
		analyser.addHook(analyser.getHooks()[1].buildObfFin(fins[0]));
		analyser.addHook(analyser.getHooks()[2].buildObfFin(fins[1]));
		analyser.addHook(analyser.getHooks()[3].buildObfFin(fins[2]));
		analyser.addHook(analyser.getHooks()[4].buildObfFin(fins[3]));
	}
}