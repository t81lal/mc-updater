package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C02PacketUseEntityAnalyser extends PlayPacketAnalyser {
	
	public C02PacketUseEntityAnalyser(ClassContainer container, HookMap hookMap) {
		super("C02PacketUseEntity", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getTargetEntityID", "I", "I"),
				new FieldMappingData("getAction", "L" + MinecraftAnalyser.INTERFACES + "entity/IUseEntityAction;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addFieldHook(fieldHooks[0].buildObfFin(fins[0]));
		addFieldHook(fieldHooks[1].buildObfFin(fins[1]));
	}
}
