package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S0EPacketSpawnObjectAnalyser extends PlayPacketAnalyser {
	
	public S0EPacketSpawnObjectAnalyser() {
		super("S0EPacketSpawnObject");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getObjectType", "I", "I"),
				new FieldHook("getX", "I", "I"),
				new FieldHook("getY", "I", "I"),
				new FieldHook("getZ", "I", "I"),
				new FieldHook("getRotationPitch", "I", "I"),
				new FieldHook("getRotationYaw", "I", "I"),
				new FieldHook("getDataLength", "I", "I"),
				new FieldHook("getData1", "I", "I"),
				new FieldHook("getData2", "I", "I"),
				new FieldHook("getData3", "I", "I") };
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