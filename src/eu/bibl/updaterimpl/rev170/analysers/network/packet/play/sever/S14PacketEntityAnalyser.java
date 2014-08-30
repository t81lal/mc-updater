package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S14PacketEntityAnalyser extends PlayPacketAnalyser{

	public S14PacketEntityAnalyser() {
		super("S14PacketEntity");
		hooks = new FieldHook[]{
			new FieldHook("getEntityID", "I", "I"),
			new FieldHook("getChangeX", "B", "B"),
			new FieldHook("getChangeY", "B", "B"),
			new FieldHook("getChangeZ", "B", "B"),
			new FieldHook("getRotationPitch", "B", "B"),
			new FieldHook("getRotationYaw", "B", "B")
		};
	}

	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		addHook(hooks[0].buildObfFin((FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD)));
	}
}