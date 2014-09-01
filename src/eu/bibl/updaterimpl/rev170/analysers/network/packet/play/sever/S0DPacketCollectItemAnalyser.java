package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S0DPacketCollectItemAnalyser extends PlayPacketAnalyser {
	
	public S0DPacketCollectItemAnalyser(ClassContainer container, HookMap hookMap) {
		super("S0DPacketCollectItem", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getCollectorEntityID", "I", "I"),
				new FieldMappingData("getCollectedEntityID", "I", "I") };
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
