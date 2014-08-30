package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S3EPacketTeamsAnalyser extends PlayPacketAnalyser {
	
	public S3EPacketTeamsAnalyser() {
		super("S3EPacketTeams");
		hooks = new FieldHook[] {
				new FieldHook("getTeamName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getMode", "I", "I"),
				new FieldHook("getTeamDisplayName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getTeamPrefix", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getTeamSuffix", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("isFriendlyFire", "I", "I"),
				new FieldHook("getPlayers", "Ljava/util/Collection;", "Ljava/util/Collection;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length - 1; i++) {
			addHook(hooks[i].buildObfFin(fins[i]));
		}
		addHook(hooks[6].buildObfFn(fields(cn, "Ljava/util/Collection;").get(0)));
	}
}