package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C04PacketPlayerPositionAnalyser extends PlayPacketAnalyser {
	
	public C04PacketPlayerPositionAnalyser(ClassContainer container, HookMap hookMap) {
		super("C04PacketPlayerPosition", container, hookMap);
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
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/play/client/IC04PacketPlayerPosition", MinecraftAnalyser.INTERFACES + "network/play/client/IC03PacketPlayer"));
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		C03PacketPlayerAnalyser analyser = (C03PacketPlayerAnalyser) analysers.get("C03PacketPlayer");
		analyser.addHook(analyser.getHooks()[1].buildObfFin(fins[0]));
		analyser.addHook(analyser.getHooks()[2].buildObfFin(fins[1]));
		analyser.addHook(analyser.getHooks()[3].buildObfFin(fins[2]));
		analyser.addHook(analyser.getHooks()[4].buildObfFin(fins[3]));
	}
}
