package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.client;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.PacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.LoginPacketAnalyser;

public class C01PacketEncryptionResponseAnalyser extends LoginPacketAnalyser {
	
	public C01PacketEncryptionResponseAnalyser() {
		super("C01PacketEncryptionResponse");
		hooks = new FieldHook[] {
				new FieldHook("getData1", "[B", "[B"),
				new FieldHook("getData2", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		PacketAnalyser packetAnalyser = (PacketAnalyser) analysers.get("Packet");
		MethodHook readMethodHook = packetAnalyser.getMethodHooks()[0];
		for(MethodNode m : methods(cn)) {
			if (m.name.equals(readMethodHook.getObfuscatedName()) && m.desc.equals(readMethodHook.getObfuscatedDesc())) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, new int[] { PUTFIELD });
				if (is.match()) {
					addHook(hooks[0].buildObfFin((FieldInsnNode) is.getMatches().get(0)[0]));
					addHook(hooks[1].buildObfFin((FieldInsnNode) is.getMatches().get(1)[0]));
					break;
				}
			}
		}
	}
}