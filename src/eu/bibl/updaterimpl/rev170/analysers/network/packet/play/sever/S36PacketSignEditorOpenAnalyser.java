package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S36PacketSignEditorOpenAnalyser extends PlayPacketAnalyser {
	
	public S36PacketSignEditorOpenAnalyser(ClassContainer container, HookMap hookMap) {
		super("S36PacketSignEditorOpen", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I") };
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
