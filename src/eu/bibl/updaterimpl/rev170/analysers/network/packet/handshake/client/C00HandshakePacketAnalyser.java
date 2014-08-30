package eu.bibl.updaterimpl.rev170.analysers.network.packet.handshake.client;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.PacketBaseAnalyser;

public class C00HandshakePacketAnalyser extends PacketBaseAnalyser {
	
	public C00HandshakePacketAnalyser() {
		super("C00HandshakePacket");
		hooks = new FieldHook[] {
				new FieldHook("getProtocolVersion", "I", "I"),
				new FieldHook("getServerIP", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldHook("getPort", "I", "I"),
				new FieldHook("getConnectionState", "L" + INTERFACES + "network/IEnumConnectionState;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook hook = map.getClassByRefactoredName(name);
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/packet/handshake/client/IC00HandshakePacket", INTERFACES + "network/packet/IPacket"));
		
		for(MethodNode m : methods(cn)) {
			if (!m.name.equals("<init>"))
				continue;
			if (!m.desc.startsWith("(ILjava/lang/String;I"))
				continue;
			int i = 0;
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain.getOpcode() == PUTFIELD) {
					addHook(hooks[i++].buildObfFin((FieldInsnNode) ain));
				}
			}
			break;
		}
	}
}