package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S03PacketTimeUpdateAnalyser extends PlayPacketAnalyser {
	
	public S03PacketTimeUpdateAnalyser(ClassContainer container, HookMap hookMap) {
		super("S03PacketTimeUpdate", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWorldAge", "I", "I"),
				new FieldMappingData("getDayTime", "I", "I") };
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
