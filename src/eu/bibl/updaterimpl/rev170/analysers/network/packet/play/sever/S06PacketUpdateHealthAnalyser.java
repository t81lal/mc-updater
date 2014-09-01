package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S06PacketUpdateHealthAnalyser extends PlayPacketAnalyser {
	
	public S06PacketUpdateHealthAnalyser(ClassContainer container, HookMap hookMap) {
		super("S06PacketUpdateHealth", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getHealth", "F", "F"),
				new FieldMappingData("getFood", "I", "I"),
				new FieldMappingData("getFoodSaturation", "F", "F") };
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
