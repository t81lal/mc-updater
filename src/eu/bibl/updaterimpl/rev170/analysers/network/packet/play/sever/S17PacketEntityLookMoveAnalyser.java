package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S17PacketEntityLookMoveAnalyser extends PlayPacketAnalyser {
	
	public S17PacketEntityLookMoveAnalyser(ClassContainer container, HookMap hookMap) {
		super("S17PacketEntityLookMove", container, hookMap);
	}
	
	@Override
public boolean accept() {
		boolean b = hookMap.getClassByRefactoredName("S14PacketEntity").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		return hookMap.getClassByRefactoredName(name).getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run1() {
		classHook.getInterfaceHook().setSuperClass(MinecraftAnalyser.INTERFACES + "network/packet/play/server/IS14PacketEntity");
	}
}
