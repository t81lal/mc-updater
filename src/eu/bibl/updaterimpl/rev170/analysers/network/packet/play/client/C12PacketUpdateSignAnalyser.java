package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C12PacketUpdateSignAnalyser extends PlayPacketAnalyser {
	
	public C12PacketUpdateSignAnalyser(ClassContainer container, HookMap hookMap) {
		super("C12PacketUpdateSign", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getText", "[Ljava/lang/String;", "[Ljava/lang/String;") };
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
