package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C03PacketPlayerAnalyser extends PlayPacketAnalyser {
	
	public C03PacketPlayerAnalyser() {
		super("C03PacketPlayer");
		hooks = new FieldHook[] {
				new FieldHook("isOnGround", "Z", "Z"),
				new FieldHook("getX", "D", "D"),
				new FieldHook("getFeetY", "D", "D"),
				new FieldHook("getHeadY", "D", "D"),
				new FieldHook("getZ", "D", "D"),
				new FieldHook("getRotationPitch", "F", "F"),
				new FieldHook("getRotationYaw", "F", "F") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addHook(hooks[0].buildObfFin(fins[0]));
	}
}