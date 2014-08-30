package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S08PacketPlayerPosLookAnalyser extends PlayPacketAnalyser {
	
	public S08PacketPlayerPosLookAnalyser() {
		super("S08PacketPlayerPosLook");
		hooks = new FieldHook[] {
				new FieldHook("getX", "D", "D"),
				new FieldHook("getY", "D", "D"),
				new FieldHook("getZ", "D", "D"),
				new FieldHook("getRotationYaw", "F", "F"),
				new FieldHook("getRotationPitch", "F", "F"),
				new FieldHook("isOnGround", "Z", "Z") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addHook(hooks[i].buildObfFin(fins[i]));
		}
	}
}