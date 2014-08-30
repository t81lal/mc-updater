package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C00PacketKeepAliveAnalyser extends PlayPacketAnalyser {
	
	public C00PacketKeepAliveAnalyser() {
		super("C00PacketKeepAlive");
		hooks = new FieldHook[] { new FieldHook("getRandomInt", "I", "I") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		addHook(hooks[0].buildObfFin(getFieldNodes(m, PUTFIELD)[0]));
	}
}