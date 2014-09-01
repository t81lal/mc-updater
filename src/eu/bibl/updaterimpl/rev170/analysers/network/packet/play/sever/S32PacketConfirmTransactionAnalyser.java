package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S32PacketConfirmTransactionAnalyser extends PlayPacketAnalyser {
	
	public S32PacketConfirmTransactionAnalyser(ClassContainer container, HookMap hookMap) {
		super("S32PacketConfirmTransaction", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getWindowID", "I", "I"),
				new FieldMappingData("getActionNumber", "I", "I"),
				new FieldMappingData("isAccepted", "Z", "Z") };
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
