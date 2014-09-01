package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S34PacketMapsAnalyser extends PlayPacketAnalyser {
	
	public S34PacketMapsAnalyser(ClassContainer container, HookMap hookMap) {
		super("S34PacketMaps", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getItemDamage", "I", "I"),
				new FieldMappingData("getData", "[B", "[B") };
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
