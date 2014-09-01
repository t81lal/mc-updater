package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S2CPacketSpawnGlobalEntityAnalyser extends PlayPacketAnalyser {
	
	public S2CPacketSpawnGlobalEntityAnalyser(ClassContainer container, HookMap hookMap) {
		super("S2CPacketSpawnGlobalEntity", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getEntityType", "I", "I"),
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I") };
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
