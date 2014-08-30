package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C11PacketEnchantItemAnalyser extends PlayPacketAnalyser {
	
	public C11PacketEnchantItemAnalyser() {
		super("C11PacketEnchantItem");
		hooks = new FieldHook[] {
				new FieldHook("getWindowID", "I", "I"),
				new FieldHook("getEnchantmentPosition", "I", "I") };
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