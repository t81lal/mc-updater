package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S2APacketParticlesAnalyser extends PlayPacketAnalyser {
	
	public S2APacketParticlesAnalyser() {
		super("S2APacketParticles");
		hooks = new FieldHook[] {
				new FieldHook("getParticleName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getX", "F", "F"),
				new FieldHook("getY", "F", "F"),
				new FieldHook("getZ", "F", "F"),
				new FieldHook("getOffsetX", "F", "F"),
				new FieldHook("getOffsetY", "F", "F"),
				new FieldHook("getOffsetZ", "F", "F"),
				new FieldHook("getParticleData", "F", "F"),
				new FieldHook("getParticleCount", "I", "I") };
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