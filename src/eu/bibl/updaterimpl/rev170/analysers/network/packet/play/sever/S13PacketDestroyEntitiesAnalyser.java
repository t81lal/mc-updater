package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S13PacketDestroyEntitiesAnalyser extends PlayPacketAnalyser {
	
	public S13PacketDestroyEntitiesAnalyser(ClassContainer container, HookMap hookMap) {
		super("S13PacketDestroyEntities", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getEntityIDs", "[I", "[I") };
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
