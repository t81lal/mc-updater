package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S16PacketEntityLookAnalyser extends PlayPacketAnalyser {
	
	public S16PacketEntityLookAnalyser() {
		super("S16PacketEntityLook");
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
		MethodNode m = getReadMethod(cn);
		S14PacketEntityAnalyser analyser = (S14PacketEntityAnalyser) analysers.get("S14PacketEntity");
		FieldInsnNode pitch = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
		FieldInsnNode yaw = (FieldInsnNode) getNext(pitch.getNext(), PUTFIELD);
		analyser.addHook(analyser.getHooks()[4].buildObfFin(pitch));
		analyser.addHook(analyser.getHooks()[5].buildObfFin(yaw));
	}
}