package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S16PacketEntityLookAnalyser extends PlayPacketAnalyser {
	
	public S16PacketEntityLookAnalyser(ClassContainer container, HookMap hookMap) {
		super("S16PacketEntityLook", container, hookMap);
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
		MethodNode m = getReadMethod(cn);
		S14PacketEntityAnalyser analyser = (S14PacketEntityAnalyser) analysers.get("S14PacketEntity");
		FieldInsnNode pitch = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
		FieldInsnNode yaw = (FieldInsnNode) getNext(pitch.getNext(), PUTFIELD);
		analyser.addHook(analyser.getHooks()[4].buildObfFin(pitch));
		analyser.addHook(analyser.getHooks()[5].buildObfFin(yaw));
	}
}
