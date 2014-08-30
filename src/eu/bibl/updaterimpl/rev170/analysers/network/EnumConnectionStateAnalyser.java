package eu.bibl.updaterimpl.rev170.analysers.network;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.LoginPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.StatusPacketAnalyser;

public class EnumConnectionStateAnalyser extends Analyser {
	
	public EnumConnectionStateAnalyser() {
		super("EnumConnectionState");
		hooks = new FieldHook[] {
				new FieldHook("getValue", "I", "I"),
				new FieldHook("getServerBoundCache", "Lcom/google/common/collect/BiMap;", "Lcom/google/common/collect/BiMap;"),
				new FieldHook("getClientBoundCache", "Lcom/google/common/collect/BiMap;", "Lcom/google/common/collect/BiMap;"), };
		methodHooks = new MethodHook[] {
				new MethodHook("addServerBoundPacket", "(ILjava/lang/Class;)L" + INTERFACES + "network/IEnumConnectionState;"),
				new MethodHook("addClientBoundPacket", "(ILjava/lang/Class;)L" + INTERFACES + "network/IEnumConnectionState;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "HANDSHAKING") && fields(cn, "Lcom/google/common/collect/BiMap;").size() == 2;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/IEnumConnectionState"));
		
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("<init>")) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, new int[] { PUTFIELD });
				if (is.match()) {
					FieldInsnNode value = (FieldInsnNode) is.getMatches().get(2)[0];
					FieldInsnNode server = (FieldInsnNode) is.getMatches().get(0)[0];
					FieldInsnNode client = (FieldInsnNode) is.getMatches().get(1)[0];
					addHook(hooks[0].buildObfFin(value));
					addHook(hooks[1].buildObfFin(server));
					addHook(hooks[2].buildObfFin(client));
					break;
				}
			}
		}
		
		boolean b = false;
		boolean b1 = false;
		for(MethodNode m : methods(cn)) {
			if (containsLdc(m, "Clientbound packet ID ")) {
				addHook(methodHooks[0].buildObfMn(m));
				b = true;
			} else if (containsLdc(m, "Serverbound packet ID ")) {
				addHook(methodHooks[1].buildObfMn(m));
				b1 = true;
			}
			if (b && b1)
				break;
		}
		
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == LDC) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst.equals("PLAY")) {
							MethodInsnNode min = (MethodInsnNode) getNext(ldc, INVOKESPECIAL);
							findPlayPackets(min);
						} else if (ldc.cst.equals("HANDSHAKING")) {
							MethodInsnNode min = (MethodInsnNode) getNext(ldc, INVOKESPECIAL);
							findHandshakePacket(min);
						} else if (ldc.cst.equals("STATUS")) {
							MethodInsnNode min = (MethodInsnNode) getNext(ldc, INVOKESPECIAL);
							findStatusPackets(min);
						} else if (ldc.cst.equals("LOGIN")) {
							MethodInsnNode min = (MethodInsnNode) getNext(ldc, INVOKESPECIAL);
							findLoginPackets(min);
						}
					}
				}
			}
		}
	}
	
	private void findPlayPackets(MethodInsnNode min) {
		ClassNode node = analysisMap.requestNode(min.owner);
		for(MethodNode m : methods(node)) {
			if (m.name.equals("<init>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain1 = (AbstractInsnNode) it.next();
					if (ain1.getOpcode() == INVOKEVIRTUAL) {
						MethodInsnNode min1 = (MethodInsnNode) ain1;
						if (min1.name.equals(methodHooks[0].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							PlayPacketAnalyser.clientBoundPacketCache.put(num, type.getClassName());
							map.addClass(new ClassHook(type.getClassName(), PlayPacketAnalyser.realClientBoundPacketCache.get(num)));
						} else if (min1.name.equals(methodHooks[1].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							PlayPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							map.addClass(new ClassHook(type.getClassName(), PlayPacketAnalyser.realServerBoundPacketCache.get(num)));
						}
					}
				}
			}
		}
	}
	
	private void findHandshakePacket(MethodInsnNode min) {
		ClassNode node = analysisMap.requestNode(min.owner);
		for(MethodNode m : methods(node)) {
			if (m.name.equals("<init>")) {
				// System.out.println(m.name + " " + m.desc);
				// ASMPrinter.consolePrint(m);
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == LDC) {
						LdcInsnNode lin = (LdcInsnNode) ain;
						Type type = (Type) lin.cst;
						map.addClass(new ClassHook(type.getClassName(), "C00HandshakePacket"));
						return;
					}
				}
			}
		}
	}
	
	private void findLoginPackets(MethodInsnNode min) {
		ClassNode node = analysisMap.requestNode(min.owner);
		for(MethodNode m : methods(node)) {
			if (m.name.equals("<init>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain1 = (AbstractInsnNode) it.next();
					if (ain1.getOpcode() == INVOKEVIRTUAL) {
						MethodInsnNode min1 = (MethodInsnNode) ain1;
						if (min1.name.equals(methodHooks[0].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							LoginPacketAnalyser.clientBoundPacketCache.put(num, type.getClassName());
							map.addClass(new ClassHook(type.getClassName(), LoginPacketAnalyser.realClientBoundPacketCache.get(num)));
						} else if (min1.name.equals(methodHooks[1].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							LoginPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							map.addClass(new ClassHook(type.getClassName(), LoginPacketAnalyser.realServerBoundPacketCache.get(num)));
						}
					}
				}
			}
		}
	}
	
	private void findStatusPackets(MethodInsnNode min) {
		ClassNode node = analysisMap.requestNode(min.owner);
		for(MethodNode m : methods(node)) {
			if (m.name.equals("<init>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain1 = (AbstractInsnNode) it.next();
					if (ain1.getOpcode() == INVOKEVIRTUAL) {
						MethodInsnNode min1 = (MethodInsnNode) ain1;
						if (min1.name.equals(methodHooks[0].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							StatusPacketAnalyser.clientBoundPacketCache.put(num, type.getClassName());
							map.addClass(new ClassHook(type.getClassName(), StatusPacketAnalyser.realClientBoundPacketCache.get(num)));
						} else if (min1.name.equals(methodHooks[1].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							StatusPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							map.addClass(new ClassHook(type.getClassName(), StatusPacketAnalyser.realServerBoundPacketCache.get(num)));
						}
					}
				}
			}
		}
	}
	
	private int resolveNumber(AbstractInsnNode ain) {
		int opcode = ain.getOpcode();
		if (opcode == ICONST_0)
			return 0;
		if (opcode == ICONST_1)
			return 1;
		if (opcode == ICONST_2)
			return 2;
		if (opcode == ICONST_3)
			return 3;
		if (opcode == ICONST_4)
			return 4;
		if (opcode == ICONST_5)
			return 5;
		if (opcode == ICONST_M1)
			return -1;
		if (opcode == BIPUSH || opcode == BIPUSH)
			return ((IntInsnNode) ain).operand;
		return -69;
	}
}