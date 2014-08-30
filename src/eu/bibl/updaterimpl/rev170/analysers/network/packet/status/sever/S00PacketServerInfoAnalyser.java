package eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.StatusPacketAnalyser;

public class S00PacketServerInfoAnalyser extends StatusPacketAnalyser {
	
	public S00PacketServerInfoAnalyser() {
		super("S00PacketServerInfo");
		hooks = new FieldHook[] { new FieldHook("getServerStatusResponse", "L" + INTERFACES + "network/packet/status/sever/IServerStatusResponse;") };
	}
	
	@Override
	public void run1() {
		MinecraftAnalyser mc = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(mc.getHooks()[12].buildObfFn(fields(cn, "Lcom/google/gson/Gson;").get(0)));
		
		MethodNode m = getReadMethod(cn);
		FieldInsnNode response = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
		addHook(hooks[0].buildObfFin(response));
	}
}