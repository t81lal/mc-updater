package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S29PacketSoundEffectAnalyser extends PlayPacketAnalyser {
	
	public S29PacketSoundEffectAnalyser() {
		super("S29PacketSoundEffect");
		hooks = new FieldHook[] {
				new FieldHook("getSoundName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getEffectX", "I", "I"),
				new FieldHook("getEffectY", "I", "I"),
				new FieldHook("getEffectZ", "I", "I"),
				new FieldHook("getVolume", "F", "F"),
				new FieldHook("getPitch", "I", "I") };
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