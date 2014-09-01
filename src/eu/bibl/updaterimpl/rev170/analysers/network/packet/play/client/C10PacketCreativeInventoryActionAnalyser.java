package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C10PacketCreativeInventoryActionAnalyser extends PlayPacketAnalyser {
	
	public C10PacketCreativeInventoryActionAnalyser(ClassContainer container, HookMap hookMap) {
		super("C10PacketCreativeInventoryAction", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getSlot", "I", "I"),
				new FieldMappingData("getClickedItem", "L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;") };
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
