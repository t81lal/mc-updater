package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S10PacketSpawnPaintingAnalyser extends PlayPacketAnalyser {
	
	public S10PacketSpawnPaintingAnalyser(ClassContainer container, HookMap hookMap) {
		super("S10PacketSpawnPainting", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getTitle", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getDirection", "I", "I") };
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
