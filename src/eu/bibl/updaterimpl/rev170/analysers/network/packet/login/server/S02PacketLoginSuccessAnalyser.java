package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.LoginPacketAnalyser;

public class S02PacketLoginSuccessAnalyser extends LoginPacketAnalyser {
	
	public S02PacketLoginSuccessAnalyser() {
		super("S02PacketLoginSuccess");
		hooks = new FieldHook[] { new FieldHook("getGameProfile", "Lcom/mojang/authlib/GameProfile;", "Lcom/mojang/authlib/GameProfile;") };
	}
	
	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "Lcom/mojang/authlib/GameProfile;").get(0)));
	}
}