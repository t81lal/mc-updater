package eu.bibl.updaterimpl.rev170.analysers.network.packet.handshake.client;
public class C00HandshakePacketAnalyser extends PacketBaseAnalyser {
	
	public C00HandshakePacketAnalyser(ClassContainer container, HookMap hookMap) {
		super("C00HandshakePacket", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getProtocolVersion", "I", "I"),
				new FieldMappingData("getServerIP", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getPort", "I", "I"),
				new FieldMappingData("getConnectionState", "L" + MinecraftAnalyser.INTERFACES + "network/IEnumConnectionState;") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByRefactoredName(name);
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/handshake/client/IC00HandshakePacket", MinecraftAnalyser.INTERFACES + "network/packet/IPacket"));
		
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
					addFieldHook(fieldHooks[i++].buildObfFin((FieldInsnNode) ain));
				}
			}
			break;
		}
	}
}
