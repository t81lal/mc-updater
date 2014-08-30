package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S09PacketHeldItemChangeAnalyser extends PlayPacketAnalyser{

	public S09PacketHeldItemChangeAnalyser() {
		super("S09PacketHeldItemChange");
		hooks = new FieldHook[]{
			new FieldHook("getSlot", "I", "I")	
		};
	}

	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "I").get(0)));
	}
}