package eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever;
public class S00PacketServerInfoAnalyser extends StatusPacketAnalyser {
	
	public S00PacketServerInfoAnalyser(ClassContainer container, HookMap hookMap) {
		super("S00PacketServerInfo", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getServerStatusResponse", "L" + MinecraftAnalyser.INTERFACES + "network/packet/status/sever/IServerStatusResponse;") };
	}
	
	@Override
	public void run1() {
		MinecraftAnalyser mc = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(mc.getHooks()[12].buildObfFn(fields(cn, "Lcom/google/gson/Gson;").get(0)));
		
		MethodNode m = getReadMethod(cn);
		FieldInsnNode response = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
		addFieldHook(fieldHooks[0].buildObfFin(response));
	}
}
