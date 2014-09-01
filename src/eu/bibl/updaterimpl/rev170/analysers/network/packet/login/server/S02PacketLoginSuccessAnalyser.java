package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server;
public class S02PacketLoginSuccessAnalyser extends LoginPacketAnalyser {
	
	public S02PacketLoginSuccessAnalyser(ClassContainer container, HookMap hookMap) {
		super("S02PacketLoginSuccess", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getGameProfile", "Lcom/mojang/authlib/GameProfile;", "Lcom/mojang/authlib/GameProfile;") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Lcom/mojang/authlib/GameProfile;").get(0)));
	}
}
