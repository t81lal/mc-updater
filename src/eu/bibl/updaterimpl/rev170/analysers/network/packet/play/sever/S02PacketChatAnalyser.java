package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S02PacketChatAnalyser extends PlayPacketAnalyser {
	
	public S02PacketChatAnalyser() {
		super("S02PacketChat");
		hooks = new FieldHook[] { new FieldHook("getChatComponent", "L" + INTERFACES + "chat/IChatComponent;") };
	}
	
	@Override
	public void run1() {
		addHook(hooks[0].buildObfFn(fields(cn, "L" + map.getClassByRefactoredName("ChatComponent").getObfuscatedName() + ";").get(0)));
	}
}