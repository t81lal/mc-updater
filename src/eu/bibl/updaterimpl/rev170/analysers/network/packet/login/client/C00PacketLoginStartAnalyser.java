package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.client;
public class C00PacketLoginStartAnalyser extends LoginPacketAnalyser {
	
	public C00PacketLoginStartAnalyser(ClassContainer container, HookMap hookMap) {
		super("C00PacketLoginStart", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getGameProfile", "Lcom/mojang/authlib/GameProfile;", "Lcom/mojang/authlib/GameProfile;") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Lcom/mojang/authlib/GameProfile;").get(0)));
	}
}
