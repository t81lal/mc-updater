package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S15PacketEntityRelMoveAnalyser extends PlayPacketAnalyser {
	
	public S15PacketEntityRelMoveAnalyser(ClassContainer container, HookMap hookMap) {
		super("S15PacketEntityRelMove", container, hookMap);
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
		FieldInsnNode x = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
		FieldInsnNode y = (FieldInsnNode) getNext(x.getNext(), PUTFIELD);
		FieldInsnNode z = (FieldInsnNode) getNext(y.getNext(), PUTFIELD);
		analyser.addHook(analyser.getHooks()[1].buildObfFin(x));
		analyser.addHook(analyser.getHooks()[2].buildObfFin(y));
		analyser.addHook(analyser.getHooks()[3].buildObfFin(z));
	}
}
