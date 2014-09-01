package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S40PacketDisconnectAnalyser extends PlayPacketAnalyser {
	
	public S40PacketDisconnectAnalyser(ClassContainer container, HookMap hookMap) {
		super("S40PacketDisconnect", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getReason", "L" + MinecraftAnalyser.INTERFACES + "chat/IChatComponent;") };
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
