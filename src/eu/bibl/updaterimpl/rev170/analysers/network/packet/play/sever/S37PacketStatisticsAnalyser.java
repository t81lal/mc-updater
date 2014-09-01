package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S37PacketStatisticsAnalyser extends PlayPacketAnalyser {
	
	public S37PacketStatisticsAnalyser(ClassContainer container, HookMap hookMap) {
		super("S37PacketStatistics", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getStatistics", "Ljava/util/Map;", "Ljava/util/Map;") };
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
