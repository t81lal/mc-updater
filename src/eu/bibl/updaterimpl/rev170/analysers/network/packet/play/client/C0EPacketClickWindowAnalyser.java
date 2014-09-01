package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C0EPacketClickWindowAnalyser extends PlayPacketAnalyser {
	
	public C0EPacketClickWindowAnalyser(ClassContainer container, HookMap hookMap) {
		super("C0EPacketClickWindow", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWindowID", "I", "I"),
				new FieldMappingData("getClickedSlot", "I", "I"),
				new FieldMappingData("getButton", "I", "I"),
				new FieldMappingData("getActionNumber", "S", "S"),
				new FieldMappingData("getMode", "I", "I"),
				new FieldMappingData("getItem", "L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;") };
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
