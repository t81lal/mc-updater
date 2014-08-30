package eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.StatusPacketAnalyser;

public class S01PacketPongAnalyser extends StatusPacketAnalyser {
	
	public S01PacketPongAnalyser() {
		super("S01PacketPong");
		hooks = new FieldHook[] { new FieldHook("getTime", "J", "J") };
	}
	
	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "J").get(0)));
	}
}