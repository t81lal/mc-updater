package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C05PacketPlayerLookAnalyser extends PlayPacketAnalyser {
	
	public C05PacketPlayerLookAnalyser(ClassContainer container, HookMap hookMap) {
		super("C05PacketPlayerLook", container, hookMap);
	}
	
	@Override
public boolean accept() {
		boolean b = hookMap.getClassByRefactoredName("C03PacketPlayer").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		return hookMap.getClassByRefactoredName(name).getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run1() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/play/client/IC05PacketPlayerLook", MinecraftAnalyser.INTERFACES + "network/play/client/IC03PacketPlayer"));
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		C03PacketPlayerAnalyser analyser = (C03PacketPlayerAnalyser) analysers.get("C03PacketPlayer");
		analyser.addHook(analyser.getHooks()[5].buildObfFin(fins[0]));
		analyser.addHook(analyser.getHooks()[6].buildObfFin(fins[1]));
	}
}
