package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S2FPacketSetSlotAnalyser extends PlayPacketAnalyser {
	
	public S2FPacketSetSlotAnalyser() {
		super("S2FPacketSetSlot");
		hooks = new FieldHook[] {
				new FieldHook("getWindowID", "I", "I"),
				new FieldHook("getSlot", "I", "I"),
				new FieldHook("getItem", "L" + INTERFACES + "item/IItemStack;") };
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