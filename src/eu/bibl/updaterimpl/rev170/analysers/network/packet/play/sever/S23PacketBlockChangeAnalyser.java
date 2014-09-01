package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S23PacketBlockChangeAnalyser extends PlayPacketAnalyser {
	
	public S23PacketBlockChangeAnalyser(ClassContainer container, HookMap hookMap) {
		super("S23PacketBlockChange", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getBlock", "L" + MinecraftAnalyser.INTERFACES + "block/IBlock;"),
				new FieldMappingData("getBlockMetadata", "I", "I") };
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
