package eu.bibl.updaterimpl.rev170.analysers.network.packet.status.client;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.StatusPacketAnalyser;

public class C01PacketPingAnalyser extends StatusPacketAnalyser {
	
	public C01PacketPingAnalyser() {
		super("C01PacketPing");
		hooks = new FieldHook[] { new FieldHook("getTime", "J", "J") };
	}
	
	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "J").get(0)));
	}
}