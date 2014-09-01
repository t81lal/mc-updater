package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S24PacketBlockActionAnalyser extends PlayPacketAnalyser {
	
	public S24PacketBlockActionAnalyser(ClassContainer container, HookMap hookMap) {
		super("S24PacketBlockAction", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getByte1", "I", "I"),
				new FieldMappingData("getByte2", "I", "I"),
				new FieldMappingData("getBlock", "L" + MinecraftAnalyser.INTERFACES + "block/IBlock;") };
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
