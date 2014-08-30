package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C10PacketCreativeInventoryActionAnalyser extends PlayPacketAnalyser {
	
	public C10PacketCreativeInventoryActionAnalyser() {
		super("C10PacketCreativeInventoryAction");
		hooks = new FieldHook[] {
				new FieldHook("getSlot", "I", "I"),
				new FieldHook("getClickedItem", "L" + INTERFACES + "item/IItemStack;") };
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