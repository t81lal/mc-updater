package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S29PacketSoundEffectAnalyser extends PlayPacketAnalyser {
	
	public S29PacketSoundEffectAnalyser(ClassContainer container, HookMap hookMap) {
		super("S29PacketSoundEffect", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getSoundName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getEffectX", "I", "I"),
				new FieldMappingData("getEffectY", "I", "I"),
				new FieldMappingData("getEffectZ", "I", "I"),
				new FieldMappingData("getVolume", "F", "F"),
				new FieldMappingData("getPitch", "I", "I") };
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
