package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S1BPacketEntityAttachAnalyser extends PlayPacketAnalyser {
	
	public S1BPacketEntityAttachAnalyser() {
		super("S1BPacketEntityAttach");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getVehicleID", "I", "I"),
				new FieldHook("isLeash", "I", "I") };
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