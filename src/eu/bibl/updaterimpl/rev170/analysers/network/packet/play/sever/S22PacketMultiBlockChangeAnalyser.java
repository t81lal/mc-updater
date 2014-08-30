package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class S22PacketMultiBlockChangeAnalyser extends PlayPacketAnalyser {
	
	public S22PacketMultiBlockChangeAnalyser() {
		super("S22PacketMultiBlockChange");
		hooks = new FieldHook[] {
				new FieldHook("getChunkPosition", "L" + INTERFACES + "world/IChunkCoordIntPair;"),
				new FieldHook("getRecordsCount", "I", "I"),
				new FieldHook("getRecords", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addHook(hooks[i].buildObfFin(fins[i]));
		}
		map.addClass(new ClassHook(Type.getType(hooks[0].getObfuscatedDesc()).getClassName(), "ChunkCoordIntPair"));
	}
}