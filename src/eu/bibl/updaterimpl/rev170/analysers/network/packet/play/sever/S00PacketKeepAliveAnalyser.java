package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S00PacketKeepAliveAnalyser extends PlayPacketAnalyser{

	public S00PacketKeepAliveAnalyser() {
		super("S00PacketKeepAlive");
		hooks = new FieldHook[]{
			new FieldHook("getRandomInt", "I", "I")	
		};
	}

	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "I").get(0)));
	}
}