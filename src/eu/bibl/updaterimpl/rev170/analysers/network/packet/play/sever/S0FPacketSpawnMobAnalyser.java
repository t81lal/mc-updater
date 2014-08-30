package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S0FPacketSpawnMobAnalyser extends PlayPacketAnalyser {
	
	public S0FPacketSpawnMobAnalyser() {
		super("S0FPacketSpawnMob");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getEntityType", "I", "I"),
				new FieldHook("getX", "I", "I"),
				new FieldHook("getY", "I", "I"),
				new FieldHook("getZ", "I", "I"),
				new FieldHook("getRotationPitch", "B", "B"),
				new FieldHook("getRotationHeadPitch", "B", "B"),
				new FieldHook("getRotationYaw", "B", "B"),
				new FieldHook("getVelocityX", "I", "I"),
				new FieldHook("getVelocityY", "I", "I"),
				new FieldHook("getVelocityZ", "I", "I"),
				new FieldHook("getDataWatcher", "L" + INTERFACES + "entity/IDataWatcher;"),
				new FieldHook("getMetadata", "Ljava/util/List;", "Ljava/util/List;"), };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length - 2; i++) {
			addHook(hooks[i].buildObfFin(fins[i]));
		}
		FieldNode dw = fields(cn, "L" + map.getClassByRefactoredName("DataWatcher").getObfuscatedName() + ";").get(0);
		FieldNode meta = fields(cn, "Ljava/util/List;").get(0);
		addHook(hooks[11].buildObfFn(dw));
		addHook(hooks[12].buildObfFn(meta));
	}
}