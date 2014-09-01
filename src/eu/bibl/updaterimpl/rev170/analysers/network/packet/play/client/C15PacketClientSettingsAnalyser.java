package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C15PacketClientSettingsAnalyser extends PlayPacketAnalyser {
	
	public C15PacketClientSettingsAnalyser(ClassContainer container, HookMap hookMap) {
		super("C15PacketClientSettings", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getLocale", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getViewDistance", "I", "I"),
				new FieldMappingData("getChatVisiblity", "L" + MinecraftAnalyser.INTERFACES + "chat/IChatVisibility;"),
				new FieldMappingData("chatColoursEnabled", "Z", "Z"),
				new FieldMappingData("getDifficulity", "L" + MinecraftAnalyser.INTERFACES + "world/IDifficulty;"),
				new FieldMappingData("showCapes", "Z", "Z") };
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
