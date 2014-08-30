package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S2DPacketOpenWindowAnalyser extends PlayPacketAnalyser {
	
	public S2DPacketOpenWindowAnalyser() {
		super("S2DPacketOpenWindow");
		hooks = new FieldHook[] {
				new FieldHook("getWindowID", "I", "I"),
				new FieldHook("getInventoryType", "I", "I"),
				new FieldHook("getWindowTitle", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getSlotCount", "I", "I"),
				new FieldHook("notTranslateTitle", "Z", "Z"),
				new FieldHook("getEntityID", "I", "I") };
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