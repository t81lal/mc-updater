package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S38PacketPlayerListItemAnalyser extends PlayPacketAnalyser {
	
	public S38PacketPlayerListItemAnalyser(ClassContainer container, HookMap hookMap) {
		super("S38PacketPlayerListItem", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getPlayerName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("isOnline", "Z", "Z"),
				new FieldMappingData("getPing", "I", "I") };
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
