package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S30PacketWindowItemsAnalyser extends PlayPacketAnalyser {
	
	public S30PacketWindowItemsAnalyser(ClassContainer container, HookMap hookMap) {
		super("S30PacketWindowItems", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWindowID", "I", "I"),
				new FieldMappingData("getItems", "[L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;") };
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
