package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S10PacketSpawnPaintingAnalyser extends PlayPacketAnalyser {
	
	public S10PacketSpawnPaintingAnalyser() {
		super("S10PacketSpawnPainting");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getTitle", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getX", "I", "I"),
				new FieldHook("getY", "I", "I"),
				new FieldHook("getZ", "I", "I"),
				new FieldHook("getDirection", "I", "I") };
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