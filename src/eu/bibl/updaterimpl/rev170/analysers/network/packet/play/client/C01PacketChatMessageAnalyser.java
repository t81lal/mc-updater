package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C01PacketChatMessageAnalyser extends PlayPacketAnalyser {
	
	public C01PacketChatMessageAnalyser() {
		super("C01PacketChatMessage");
		hooks = new FieldHook[] { new FieldHook("getMessage", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		addHook(hooks[0].buildObfFin(getFieldNodes(m, PUTFIELD)[0]));
	}
}