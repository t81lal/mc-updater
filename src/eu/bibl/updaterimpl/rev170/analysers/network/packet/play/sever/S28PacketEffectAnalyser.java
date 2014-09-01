package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S28PacketEffectAnalyser extends PlayPacketAnalyser {
	
	public S28PacketEffectAnalyser(ClassContainer container, HookMap hookMap) {
		super("S28PacketEffect", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEffectID", "I", "I"),
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getData", "I", "I"),
				new FieldMappingData("disableRelativeVolume", "Z", "Z") };
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
