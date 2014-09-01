package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S2FPacketSetSlotAnalyser extends PlayPacketAnalyser {
	
	public S2FPacketSetSlotAnalyser(ClassContainer container, HookMap hookMap) {
		super("S2FPacketSetSlot", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWindowID", "I", "I"),
				new FieldMappingData("getSlot", "I", "I"),
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
