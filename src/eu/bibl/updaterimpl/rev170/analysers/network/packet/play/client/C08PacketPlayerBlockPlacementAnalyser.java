package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C08PacketPlayerBlockPlacementAnalyser extends PlayPacketAnalyser {
	
	public C08PacketPlayerBlockPlacementAnalyser() {
		super("C08PacketPlayerBlockPlacement");
		hooks = new FieldHook[] {
				new FieldHook("getX", "I", "I"),
				new FieldHook("getY", "I", "I"),
				new FieldHook("getZ", "I", "I"),
				new FieldHook("getDirection", "I", "I"),
				new FieldHook("getHeldItem", "L" + INTERFACES + "item/IItemStack;"),
				new FieldHook("getCursorX", "F", "F"),
				new FieldHook("getCursorY", "F", "F"),
				new FieldHook("getCursorZ", "F", "F") };
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