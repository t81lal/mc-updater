package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S21PacketChunkDataAnalyser extends PlayPacketAnalyser {
	
	public S21PacketChunkDataAnalyser() {
		super("S21PacketChunkData");
		hooks = new FieldHook[] {
				new FieldHook("getChunkX", "I", "I"),
				new FieldHook("getChunkZ", "I", "I"),
				new FieldHook("isGroundUpContinuous", "Z", "Z"),
				new FieldHook("getPrimaryBitMap", "I", "I"),
				new FieldHook("getAddBitMap", "I", "I"),
				new FieldHook("getCompressedSize", "I", "I"),
				new FieldHook("getCompressedData", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getWriteMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, GETFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addHook(hooks[i].buildObfFin(fins[i]));
		}
	}
}