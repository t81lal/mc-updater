package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C0FPacketConfirmTransactionAnalyser extends PlayPacketAnalyser {
	
	public C0FPacketConfirmTransactionAnalyser() {
		super("C0FPacketConfirmTransaction");
		hooks = new FieldHook[] {
				new FieldHook("getWindowID", "I", "I"),
				new FieldHook("getActionNumber", "S", "S"),
				new FieldHook("isAccepted", "Z", "Z"), };
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