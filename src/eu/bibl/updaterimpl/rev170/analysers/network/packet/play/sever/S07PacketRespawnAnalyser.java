package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S07PacketRespawnAnalyser extends PlayPacketAnalyser {
	
	public S07PacketRespawnAnalyser() {
		super("S07PacketRespawn");
		hooks = new FieldHook[] {
				new FieldHook("getDimension", "I", "I"),
				new FieldHook("getDifficulty", "L" + INTERFACES + "world/IDifficulty;"),
				new FieldHook("getGameMode", "L" + INTERFACES + "world/IGameMode;"),
				new FieldHook("getWorldType", "L" + INTERFACES + "world/IWorldType;") };
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