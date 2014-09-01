package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S12PacketEntityVelocityAnalyser extends PlayPacketAnalyser {
	
	public S12PacketEntityVelocityAnalyser(ClassContainer container, HookMap hookMap) {
		super("S12PacketEntityVelocity", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getVelocityX", "I", "I"),
				new FieldMappingData("getVelocityY", "I", "I"),
				new FieldMappingData("getVelocityZ", "I", "I"), };
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
