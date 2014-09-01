package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S0CPacketSpawnPlayerAnalyser extends PlayPacketAnalyser {
	
	public S0CPacketSpawnPlayerAnalyser(ClassContainer container, HookMap hookMap) {
		super("S0CPacketSpawnPlayer", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getGameProfile", "Lcom/mojang/authlib/GameProfile;", "Lcom/mojang/authlib/GameProfile;"),
				new FieldMappingData("getDataLength", "I", "I"),
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getRotationPitch", "B", "B"),
				new FieldMappingData("getRotationYaw", "B", "B"),
				new FieldMappingData("gegetCurrentItem", "I", "I"),
				new FieldMappingData("getMetadata", "Ljava/util/List;", "Ljava/util/List;"), };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length - 1; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
		addFieldHook(fieldHooks[9].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
	}
}
