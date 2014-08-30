package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.LoginPacketAnalyser;

public class S01PacketEncryptionRequestAnalyser extends LoginPacketAnalyser {
	
	public S01PacketEncryptionRequestAnalyser() {
		super("S01PacketEncryptionRequest");
		hooks = new FieldHook[] {
				new FieldHook("getString", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getPublicKey", "Ljava/security/PublicKey;", "Ljava/security/PublicKey;"),
				new FieldHook("getBytes", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "Ljava/lang/String;").get(0)));
		addHook(hooks[1].buildObfFn(fields(cn, "Ljava/security/PublicKey;").get(0)));
		addHook(hooks[2].buildObfFn(fields(cn, "[B").get(0)));
	}
}