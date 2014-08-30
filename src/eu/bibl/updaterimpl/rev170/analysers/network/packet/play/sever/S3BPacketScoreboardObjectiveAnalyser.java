package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S3BPacketScoreboardObjectiveAnalyser extends PlayPacketAnalyser {
	
	public S3BPacketScoreboardObjectiveAnalyser() {
		super("S3BPacketScoreboardObjective");
		hooks = new FieldHook[] {
				new FieldHook("getObjectiveName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getObjectiveValue", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getMode", "I", "I") };
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