package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S2DPacketOpenWindowAnalyser extends PlayPacketAnalyser {
	
	public S2DPacketOpenWindowAnalyser(ClassContainer container, HookMap hookMap) {
		super("S2DPacketOpenWindow", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWindowID", "I", "I"),
				new FieldMappingData("getInventoryType", "I", "I"),
				new FieldMappingData("getWindowTitle", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getSlotCount", "I", "I"),
				new FieldMappingData("notTranslateTitle", "Z", "Z"),
				new FieldMappingData("getEntityID", "I", "I") };
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
