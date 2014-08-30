package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C16PacketClientStatusAnalyser extends PlayPacketAnalyser {
	
	public C16PacketClientStatusAnalyser() {
		super("C16PacketClientStatus");
		hooks = new FieldHook[] { new FieldHook("getClientState", "L" + INTERFACES + "client/IClientState;") };
	}
	
	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "L" + map.getClassByRefactoredName("ClientState").getObfuscatedName() + ";").get(0)));
	}
}