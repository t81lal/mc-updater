package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S1FPacketSetExperienceAnalyser extends PlayPacketAnalyser {
	
	public S1FPacketSetExperienceAnalyser() {
		super("S1FPacketSetExperience");
		hooks = new FieldHook[] {
				new FieldHook("getExperienceBar", "F", "F"),
				new FieldHook("getLevel", "I", "I"),
				new FieldHook("getTotalExperience", "I", "I"), };
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