package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C15PacketClientSettingsAnalyser extends PlayPacketAnalyser {
	
	public C15PacketClientSettingsAnalyser() {
		super("C15PacketClientSettings");
		hooks = new FieldHook[] {
				new FieldHook("getLocale", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getViewDistance", "I", "I"),
				new FieldHook("getChatVisiblity", "L" + INTERFACES + "chat/IChatVisibility;"),
				new FieldHook("chatColoursEnabled", "Z", "Z"),
				new FieldHook("getDifficulity", "L" + INTERFACES + "world/IDifficulty;"),
				new FieldHook("showCapes", "Z", "Z") };
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