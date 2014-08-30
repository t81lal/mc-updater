package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C0BPacketEntityActionAnalyser extends PlayPacketAnalyser {
	
	public C0BPacketEntityActionAnalyser() {
		super("C0BPacketEntityAction");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getActionID", "I", "I"),
				new FieldHook("getHorseJumpBoost", "I", "I") };
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