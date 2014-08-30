package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C17PacketCustomPayloadAnalyser extends PlayPacketAnalyser {
	
	public C17PacketCustomPayloadAnalyser() {
		super("C17PacketCustomPayload");
		hooks = new FieldHook[] {
				new FieldHook("getChannel", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getPayloadLength", "I", "I"),
				new FieldHook("getPayload", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addHook(hooks[i].buildObfFin(fins[i]));
		}
	}
}