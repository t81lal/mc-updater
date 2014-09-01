package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S22PacketMultiBlockChangeAnalyser extends PlayPacketAnalyser {
	
	public S22PacketMultiBlockChangeAnalyser(ClassContainer container, HookMap hookMap) {
		super("S22PacketMultiBlockChange", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getChunkPosition", "L" + MinecraftAnalyser.INTERFACES + "world/IChunkCoordIntPair;"),
				new FieldMappingData("getRecordsCount", "I", "I"),
				new FieldMappingData("getRecords", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
		hookMap.addClass(new ClassMappingData(Type.getType(hooks[0].getObfuscatedDesc()).getClassName(), "ChunkCoordIntPair"));
	}
}
