package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C14PacketTabCompleteAnalyser extends PlayPacketAnalyser {
	
	public C14PacketTabCompleteAnalyser(ClassContainer container, HookMap hookMap) {
		super("C14PacketTabComplete", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getText", "Ljava/lang/String;", "Ljava/lang/String;") };
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
