package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S39PacketPlayerAbilitiesAnalyser extends PlayPacketAnalyser {
	
	public S39PacketPlayerAbilitiesAnalyser() {
		super("S39PacketPlayerAbilities");
		hooks = new FieldHook[] {
				new FieldHook("isInvincible", "Z", "Z"),
				new FieldHook("canFly", "Z", "Z"),
				new FieldHook("isFlying", "Z", "Z"),
				new FieldHook("isCreativeMode", "Z", "Z"),
				new FieldHook("getFlyingSpeed", "F", "F"),
				new FieldHook("getWalkingSpeed", "F", "F") };
	}
	
	@Override
	public void run1() {
		MethodNode read = getReadMethod(cn);
		int i = 0;
		MethodInsnNode[] mins = getMethodNodes(read, INVOKEVIRTUAL);
		for(MethodInsnNode min : mins) {
			if (!min.owner.equals(cn.name))
				continue;
			MethodNode m = getMethod(min.name, min.desc);
			FieldInsnNode fin = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
			addHook(hooks[i++].buildObfFin(fin));
		}
	}
}