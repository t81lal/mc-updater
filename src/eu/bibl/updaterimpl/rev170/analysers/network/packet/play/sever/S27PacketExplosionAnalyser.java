package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S27PacketExplosionAnalyser extends PlayPacketAnalyser {
	
	public S27PacketExplosionAnalyser(ClassContainer container, HookMap hookMap) {
		super("S27PacketExplosion", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "D", "D"),
				new FieldMappingData("getY", "D", "D"),
				new FieldMappingData("getZ", "D", "D"),
				new FieldMappingData("getRadius", "F", "F"),
				new FieldMappingData("getRecords", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldMappingData("getMotionX", "F", "F"),
				new FieldMappingData("getMotionY", "F", "F"),
				new FieldMappingData("getMotionZ", "F", "F") };
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
