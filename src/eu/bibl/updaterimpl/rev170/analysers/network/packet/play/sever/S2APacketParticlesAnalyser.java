package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S2APacketParticlesAnalyser extends PlayPacketAnalyser {
	
	public S2APacketParticlesAnalyser(ClassContainer container, HookMap hookMap) {
		super("S2APacketParticles", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getParticleName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getX", "F", "F"),
				new FieldMappingData("getY", "F", "F"),
				new FieldMappingData("getZ", "F", "F"),
				new FieldMappingData("getOffsetX", "F", "F"),
				new FieldMappingData("getOffsetY", "F", "F"),
				new FieldMappingData("getOffsetZ", "F", "F"),
				new FieldMappingData("getParticleData", "F", "F"),
				new FieldMappingData("getParticleCount", "I", "I") };
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
