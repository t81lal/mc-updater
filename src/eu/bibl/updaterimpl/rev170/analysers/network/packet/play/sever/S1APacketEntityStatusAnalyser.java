package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S1APacketEntityStatusAnalyser extends PlayPacketAnalyser {
	
	public S1APacketEntityStatusAnalyser(ClassContainer container, HookMap hookMap) {
		super("S1APacketEntityStatus", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getEntityStatus", "B", "B") };
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
