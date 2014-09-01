package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S33PacketUpdateSignAnalyser extends PlayPacketAnalyser {
	
	public S33PacketUpdateSignAnalyser(ClassContainer container, HookMap hookMap) {
		super("S33PacketUpdateSign", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getLines", "[Ljava/lang/String;", "[Ljava/lang/String;") };
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
