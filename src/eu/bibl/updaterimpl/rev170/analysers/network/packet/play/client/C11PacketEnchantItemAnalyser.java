package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C11PacketEnchantItemAnalyser extends PlayPacketAnalyser {
	
	public C11PacketEnchantItemAnalyser(ClassContainer container, HookMap hookMap) {
		super("C11PacketEnchantItem", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWindowID", "I", "I"),
				new FieldMappingData("getEnchantmentPosition", "I", "I") };
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
