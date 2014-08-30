package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.LoginPacketAnalyser;

public class S00PacketDisconnectAnalyser extends LoginPacketAnalyser {
	
	public S00PacketDisconnectAnalyser() {
		super("S00PacketDisconnect");
		hooks = new FieldHook[] { new FieldHook("getChatComponent", "L" + INTERFACES + "IChatComponent;") };
	}
	
	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "L" + map.getClassByRefactoredName("ChatComponent").getObfuscatedName() + ";").get(0)));
	}
}