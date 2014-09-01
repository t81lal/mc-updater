package eu.bibl.updaterimpl.rev170.analysers.network;
public class EnumConnectionStateAnalyser extends Analyser {
	
	public EnumConnectionStateAnalyser(ClassContainer container, HookMap hookMap) {
		super("EnumConnectionState", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getValue", "I", "I"),
				new FieldMappingData("getServerBoundCache", "Lcom/google/common/collect/BiMap;", "Lcom/google/common/collect/BiMap;"),
				new FieldMappingData("getClientBoundCache", "Lcom/google/common/collect/BiMap;", "Lcom/google/common/collect/BiMap;"), };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData("addServerBoundPacket", "(ILjava/lang/Class;)L" + MinecraftAnalyser.INTERFACES + "network/IEnumConnectionState;"),
				new CallbackMappingData("addClientBoundPacket", "(ILjava/lang/Class;)L" + MinecraftAnalyser.INTERFACES + "network/IEnumConnectionState;") };
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "HANDSHAKING") && fields(cn, "Lcom/google/common/collect/BiMap;").size() == 2;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/IEnumConnectionState"));
		
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("<init>")) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, new int[] { PUTFIELD });
				if (is.match()) {
					FieldInsnNode value = (FieldInsnNode) is.getMatches().get(2)[0];
					FieldInsnNode server = (FieldInsnNode) is.getMatches().get(0)[0];
					FieldInsnNode client = (FieldInsnNode) is.getMatches().get(1)[0];
					addFieldHook(fieldHooks[0].buildObfFin(value));
					addFieldHook(fieldHooks[1].buildObfFin(server));
					addFieldHook(fieldHooks[2].buildObfFin(client));
					break;
				}
			}
		}
		
		boolean b = false;
		boolean b1 = false;
		for(MethodNode m : methods(cn)) {
			if (containsLdc(m, "Clientbound packet ID ")) {
				addMethodHook(methodHooks[0].buildObfMn(m));
				b = true;
			} else if (containsLdc(m, "Serverbound packet ID ")) {
				addMethodHook(methodHooks[1].buildObfMn(m));
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
							hookMap.addClass(new ClassMappingData(type.getClassName(), PlayPacketAnalyser.realClientBoundPacketCache.get(num)));
						} else if (min1.name.equals(methodHooks[1].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							PlayPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), PlayPacketAnalyser.realServerBoundPacketCache.get(num)));
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
						hookMap.addClass(new ClassMappingData(type.getClassName(), "C00HandshakePacket"));
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
							hookMap.addClass(new ClassMappingData(type.getClassName(), LoginPacketAnalyser.realClientBoundPacketCache.get(num)));
						} else if (min1.name.equals(methodHooks[1].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							LoginPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), LoginPacketAnalyser.realServerBoundPacketCache.get(num)));
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
							hookMap.addClass(new ClassMappingData(type.getClassName(), StatusPacketAnalyser.realClientBoundPacketCache.get(num)));
						} else if (min1.name.equals(methodHooks[1].getObfuscatedName())) {
							int num = resolveNumber(ain1.getPrevious().getPrevious());
							Type type = (Type) ((LdcInsnNode) ain1.getPrevious()).cst;
							StatusPacketAnalyser.serverBoundPacketCache.put(num, type.getClassName());
							hookMap.addClass(new ClassMappingData(type.getClassName(), StatusPacketAnalyser.realServerBoundPacketCache.get(num)));
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
