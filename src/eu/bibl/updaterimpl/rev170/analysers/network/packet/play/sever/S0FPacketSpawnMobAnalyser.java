package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S0FPacketSpawnMobAnalyser extends PlayPacketAnalyser {
	
	public S0FPacketSpawnMobAnalyser(ClassContainer container, HookMap hookMap) {
		super("S0FPacketSpawnMob", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getEntityType", "I", "I"),
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getRotationPitch", "B", "B"),
				new FieldMappingData("getRotationHeadPitch", "B", "B"),
				new FieldMappingData("getRotationYaw", "B", "B"),
				new FieldMappingData("getVelocityX", "I", "I"),
				new FieldMappingData("getVelocityY", "I", "I"),
				new FieldMappingData("getVelocityZ", "I", "I"),
				new FieldMappingData("getDataWatcher", "L" + MinecraftAnalyser.INTERFACES + "entity/IDataWatcher;"),
				new FieldMappingData("getMetadata", "Ljava/util/List;", "Ljava/util/List;"), };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length - 2; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
		FieldNode dw = fields(cn, "L" + hookMap.getClassByRefactoredName("DataWatcher").getObfuscatedName() + ";").get(0);
		FieldNode meta = fields(cn, "Ljava/util/List;").get(0);
		addFieldHook(fieldHooks[11].buildObfFn(dw));
		addFieldHook(fieldHooks[12].buildObfFn(meta));
	}
}
