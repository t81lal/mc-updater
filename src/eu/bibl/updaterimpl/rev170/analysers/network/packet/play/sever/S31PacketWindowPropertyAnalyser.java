package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S31PacketWindowPropertyAnalyser extends PlayPacketAnalyser {
	
	public S31PacketWindowPropertyAnalyser(ClassContainer container, HookMap hookMap) {
		super("S31PacketWindowProperty", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWindowID", "I", "I"),
				new FieldMappingData("getProperty", "I", "I"),
				new FieldMappingData("getValue", "I", "I") };
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
