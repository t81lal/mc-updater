package eu.bibl.updaterimpl.rev170.analysers.network;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.asm.ClassNode;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.LoginPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.StatusPacketAnalyser;

public class EnumConnectionStateAnalyser extends Analyser {
	
	public EnumConnectionStateAnalyser(ClassContainer container, HookMap hookMap) {
		super("EnumConnectionState", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getValue"), new MappingData("I", "I"), false),
				/* 1 */new FieldMappingData(new MappingData("getServerBoundCache"), new MappingData("Lcom/google/common/collect/BiMap;", "Lcom/google/common/collect/BiMap;"), false),
				/* 2 */new FieldMappingData(new MappingData("getClientBoundCache"), new MappingData("Lcom/google/common/collect/BiMap;", "Lcom/google/common/collect/BiMap;"), false), };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData(new MappingData("addServerBoundPacket"), new MappingData("(ILjava/lang/Class;)L" + MinecraftAnalyser.INTERFACES + "network/IEnumConnectionState;"), null, null, false),
				new CallbackMappingData(new MappingData("addClientBoundPacket"), new MappingData("(ILjava/lang/Class;)L" + MinecraftAnalyser.INTERFACES + "network/IEnumConnectionState;"), null, null, false) };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "HANDSHAKING") && (InsnUtil.fields(cn, "Lcom/google/common/collect/BiMap;").size() == 2);
	}
	
	@Override
	public InterfaceMappingData run() {
		for (MethodNode m : cn.methods()) {
			if (m.name.equals("<init>")) {
				InstructionSearcher is = new InstructionSearcher(m.instructions, new int[] { PUTFIELD });
				if (is.search()) {
					FieldInsnNode value = (FieldInsnNode) is.getMatches().get(2)[0];
					FieldInsnNode server = (FieldInsnNode) is.getMatches().get(0)[0];
					FieldInsnNode client = (FieldInsnNode) is.getMatches().get(1)[0];
					addField(fieldHooks[0].buildObf(value));
					addField(fieldHooks[1].buildObf(server));
					addField(fieldHooks[2].buildObf(client));
					break;
				}
			}
		}
		
		boolean b = false;
		boolean b1 = false;
		for (MethodNode m : cn.methods()) {
			if (InsnUtil.containsLdc(m, "Clientbound packet ID ")) {
				addMethod(methodHooks[0].buildObfMethod(m));
				b = true;
			} else if (InsnUtil.containsLdc(m, "Serverbound packet ID ")) {
				addMethod(methodHooks[1].buildObfMethod(m));
				b1 = true;
			}
			if (b && b1)
				break;
		}
		
		for (MethodNode m : cn.methods()) {
			if (m.name.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == LDC) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst.equals("PLAY")) {
							MethodInsnNode min = (MethodInsnNode) InsnUtil.getNext(ldc, INVOKESPECIAL);
							findPlayPackets(min);
						} else if (ldc.cst.equals("HANDSHAKING")) {
							MethodInsnNode min = (MethodInsnNode) InsnUtil.getNext(ldc, INVOKESPECIAL);
							findHandshakePacket(min);
						} else if (ldc.cst.equals("STATUS")) {
							MethodInsnNode min = (MethodInsnNode) InsnUtil.getNext(ldc, INVOKESPECIAL);
							findStatusPackets(min);
						} else if (ldc.cst.equals("LOGIN")) {
							MethodInsnNode min = (MethodInsnNode) InsnUtil.getNext(ldc, INVOKESPECIAL);
							findLoginPackets(min);
						}
					}
				}
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/IEnumConnectionState");
	}
	
	private void findPlayPackets(MethodInsnNode min) {
		ClassNode node = getNode(min.owner);
		for (MethodNode m : node.methods()) {
			if (m.name.equals("<init>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain1 = (AbstractInsnNode) it.next();
					if (ain1.getOpcode() == INVOKEVIRTUAL) {
						MethodInsnNode min1 = (MethodInsnNode) ain1;
						if (min1.name.equals(methodHooks[0].getMethodName().getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							PlayPacketAnalyser.clientBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), PlayPacketAnalyser.realClientBoundPacketCache.get(num), null));
						} else if (min1.name.equals(methodHooks[1].getMethodName().getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							PlayPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), PlayPacketAnalyser.realServerBoundPacketCache.get(num), null));
						}
					}
				}
			}
		}
	}
	
	private void findHandshakePacket(MethodInsnNode min) {
		ClassNode node = getNode(min.owner);
		for (MethodNode m : node.methods()) {
			if (m.name.equals("<init>")) {
				// System.out.println(m.name + " " + m.desc);
				// ASMPrinter.consolePrint(m);
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == LDC) {
						LdcInsnNode lin = (LdcInsnNode) ain;
						Type type = (Type) lin.cst;
						hookMap.addClass(new ClassMappingData(type.getClassName(), "C00HandshakePacket", null));
						return;
					}
				}
			}
		}
	}
	
	private void findLoginPackets(MethodInsnNode min) {
		ClassNode node = getNode(min.owner);
		for (MethodNode m : node.methods()) {
			if (m.name.equals("<init>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain1 = (AbstractInsnNode) it.next();
					if (ain1.getOpcode() == INVOKEVIRTUAL) {
						MethodInsnNode min1 = (MethodInsnNode) ain1;
						if (min1.name.equals(methodHooks[0].getMethodName().getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							LoginPacketAnalyser.clientBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), LoginPacketAnalyser.realClientBoundPacketCache.get(num), null));
						} else if (min1.name.equals(methodHooks[1].getMethodName().getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							LoginPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), LoginPacketAnalyser.realServerBoundPacketCache.get(num), null));
						}
					}
				}
			}
		}
	}
	
	private void findStatusPackets(MethodInsnNode min) {
		ClassNode node = getNode(min.owner);
		for (MethodNode m : node.methods()) {
			if (m.name.equals("<init>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain1 = (AbstractInsnNode) it.next();
					if (ain1.getOpcode() == INVOKEVIRTUAL) {
						MethodInsnNode min1 = (MethodInsnNode) ain1;
						if (min1.name.equals(methodHooks[0].getMethodName().getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							StatusPacketAnalyser.clientBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), StatusPacketAnalyser.realClientBoundPacketCache.get(num), null));
						} else if (min1.name.equals(methodHooks[1].getMethodName().getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							StatusPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), StatusPacketAnalyser.realServerBoundPacketCache.get(num), null));
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
		if ((opcode == BIPUSH) || (opcode == BIPUSH))
			return ((IntInsnNode) ain).operand;
		return -69;
	}
}
