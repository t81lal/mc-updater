package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S0CPacketSpawnPlayerAnalyser extends PlayPacketAnalyser {
	
	public S0CPacketSpawnPlayerAnalyser() {
		super("S0CPacketSpawnPlayer");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getGameProfile", "Lcom/mojang/authlib/GameProfile;", "Lcom/mojang/authlib/GameProfile;"),
				new FieldHook("getDataLength", "I", "I"),
				new FieldHook("getX", "I", "I"),
				new FieldHook("getY", "I", "I"),
				new FieldHook("getZ", "I", "I"),
				new FieldHook("getRotationPitch", "B", "B"),
				new FieldHook("getRotationYaw", "B", "B"),
				new FieldHook("gegetCurrentItem", "I", "I"),
				new FieldHook("getMetadata", "Ljava/util/List;", "Ljava/util/List;"), };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length - 1; i++) {
			addHook(hooks[i].buildObfFin(fins[i]));
		}
		addHook(hooks[9].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
	}
}