package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S20PacketEntityPropertiesAnalyser extends PlayPacketAnalyser {
	
	public S20PacketEntityPropertiesAnalyser() {
		super("S20PacketEntityProperties");
		hooks = new FieldHook[] {
				new FieldHook("getEntityID", "I", "I"),
				new FieldHook("getProperties", "Ljava/util/List;", "Ljava/util/List;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addHook(hooks[0].buildObfFin(fins[0]));
		addHook(hooks[1].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
	}
}