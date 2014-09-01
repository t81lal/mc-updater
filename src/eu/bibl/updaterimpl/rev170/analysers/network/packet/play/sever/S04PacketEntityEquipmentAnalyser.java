package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S04PacketEntityEquipmentAnalyser extends PlayPacketAnalyser {
	
	public S04PacketEntityEquipmentAnalyser(ClassContainer container, HookMap hookMap) {
		super("S04PacketEntityEquipment", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getSlot", "I", "I"),
				new FieldMappingData("getItem", "L" + MinecraftAnalyser.INTERFACES + "item/IItem;") };
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
