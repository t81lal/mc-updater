package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S06PacketUpdateHealthAnalyser extends PlayPacketAnalyser {
	
	public S06PacketUpdateHealthAnalyser() {
		super("S06PacketUpdateHealth");
		hooks = new FieldHook[] {
				new FieldHook("getHealth", "F", "F"),
				new FieldHook("getFood", "I", "I"),
				new FieldHook("getFoodSaturation", "F", "F") };
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