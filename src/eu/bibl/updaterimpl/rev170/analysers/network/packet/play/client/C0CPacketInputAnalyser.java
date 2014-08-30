package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C0CPacketInputAnalyser extends PlayPacketAnalyser {
	
	public C0CPacketInputAnalyser() {
		super("C0CPacketInput");
		hooks = new FieldHook[] {
				new FieldHook("getSidewaysMotion", "F", "F"),
				new FieldHook("getForwardMotion", "F", "F"),
				new FieldHook("isJump", "Z", "Z"),
				new FieldHook("isUnmount", "Z", "Z") };
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