package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C08PacketPlayerBlockPlacementAnalyser extends PlayPacketAnalyser {
	
	public C08PacketPlayerBlockPlacementAnalyser(ClassContainer container, HookMap hookMap) {
		super("C08PacketPlayerBlockPlacement", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getDirection", "I", "I"),
				new FieldMappingData("getHeldItem", "L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;"),
				new FieldMappingData("getCursorX", "F", "F"),
				new FieldMappingData("getCursorY", "F", "F"),
				new FieldMappingData("getCursorZ", "F", "F") };
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
