package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S20PacketEntityPropertiesAnalyser extends PlayPacketAnalyser {
	
	public S20PacketEntityPropertiesAnalyser(ClassContainer container, HookMap hookMap) {
		super("S20PacketEntityProperties", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getProperties", "Ljava/util/List;", "Ljava/util/List;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addFieldHook(fieldHooks[0].buildObfFin(fins[0]));
		addFieldHook(fieldHooks[1].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
	}
}
