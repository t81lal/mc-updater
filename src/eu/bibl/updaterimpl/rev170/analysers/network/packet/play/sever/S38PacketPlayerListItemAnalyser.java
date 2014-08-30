package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S38PacketPlayerListItemAnalyser extends PlayPacketAnalyser {
	
	public S38PacketPlayerListItemAnalyser() {
		super("S38PacketPlayerListItem");
		hooks = new FieldHook[] {
				new FieldHook("getPlayerName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("isOnline", "Z", "Z"),
				new FieldHook("getPing", "I", "I") };
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