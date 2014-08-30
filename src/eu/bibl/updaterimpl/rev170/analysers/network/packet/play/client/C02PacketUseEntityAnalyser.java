package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C02PacketUseEntityAnalyser extends PlayPacketAnalyser {
	
	public C02PacketUseEntityAnalyser() {
		super("C02PacketUseEntity");
		hooks = new FieldHook[] {
				new FieldHook("getTargetEntityID", "I", "I"),
				new FieldHook("getAction", "L" + INTERFACES + "entity/IUseEntityAction;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addHook(hooks[0].buildObfFin(fins[0]));
		addHook(hooks[1].buildObfFin(fins[1]));
	}
}