package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S21PacketChunkDataAnalyser extends PlayPacketAnalyser {
	
	public S21PacketChunkDataAnalyser(ClassContainer container, HookMap hookMap) {
		super("S21PacketChunkData", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getChunkX", "I", "I"),
				new FieldMappingData("getChunkZ", "I", "I"),
				new FieldMappingData("isGroundUpContinuous", "Z", "Z"),
				new FieldMappingData("getPrimaryBitMap", "I", "I"),
				new FieldMappingData("getAddBitMap", "I", "I"),
				new FieldMappingData("getCompressedSize", "I", "I"),
				new FieldMappingData("getCompressedData", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getWriteMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, GETFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
	}
}
