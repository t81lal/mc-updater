package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S0EPacketSpawnObjectAnalyser extends PlayPacketAnalyser {
	
	public S0EPacketSpawnObjectAnalyser(ClassContainer container, HookMap hookMap) {
		super("S0EPacketSpawnObject", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getObjectType", "I", "I"),
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getRotationPitch", "I", "I"),
				new FieldMappingData("getRotationYaw", "I", "I"),
				new FieldMappingData("getDataLength", "I", "I"),
				new FieldMappingData("getData1", "I", "I"),
				new FieldMappingData("getData2", "I", "I"),
				new FieldMappingData("getData3", "I", "I") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
	}
}
