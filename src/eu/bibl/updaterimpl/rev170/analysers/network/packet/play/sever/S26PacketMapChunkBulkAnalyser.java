package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S26PacketMapChunkBulkAnalyser extends PlayPacketAnalyser {
	
	public S26PacketMapChunkBulkAnalyser(ClassContainer container, HookMap hookMap) {
		super("S26PacketMapChunkBulk", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getDataLength", "I", "I"),
				new FieldMappingData("skylightSent", "Z", "Z") };
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
