package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C06PacketPlayerPosLookAnalyser extends PlayPacketAnalyser {
	
	public C06PacketPlayerPosLookAnalyser(ClassContainer container, HookMap hookMap) {
		super("C06PacketPlayerPosLook", container, hookMap);
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
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/play/client/IC06PacketPlayerPosLook", MinecraftAnalyser.INTERFACES + "network/play/client/IC03PacketPlayer"));
	}
}
