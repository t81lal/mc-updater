package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S27PacketExplosionAnalyser extends PlayPacketAnalyser {
	
	public S27PacketExplosionAnalyser() {
		super("S27PacketExplosion");
		hooks = new FieldHook[] {
				new FieldHook("getX", "D", "D"),
				new FieldHook("getY", "D", "D"),
				new FieldHook("getZ", "D", "D"),
				new FieldHook("getRadius", "F", "F"),
				new FieldHook("getRecords", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldHook("getMotionX", "F", "F"),
				new FieldHook("getMotionY", "F", "F"),
				new FieldHook("getMotionZ", "F", "F") };
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