package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S39PacketPlayerAbilitiesAnalyser extends PlayPacketAnalyser {
	
	public S39PacketPlayerAbilitiesAnalyser(ClassContainer container, HookMap hookMap) {
		super("S39PacketPlayerAbilities", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("isInvincible", "Z", "Z"),
				new FieldMappingData("canFly", "Z", "Z"),
				new FieldMappingData("isFlying", "Z", "Z"),
				new FieldMappingData("isCreativeMode", "Z", "Z"),
				new FieldMappingData("getFlyingSpeed", "F", "F"),
				new FieldMappingData("getWalkingSpeed", "F", "F") };
	}
	
	@Override
	public void run1() {
		MethodNode read = getReadMethod(cn);
		int i = 0;
		MethodInsnNode[] mins = getMethodNodes(read, INVOKEVIRTUAL);
		for(MethodInsnNode min : mins) {
			if (!min.owner.equals(cn.name))
				continue;
			MethodNode m = getMethod(min.name, min.desc);
			FieldInsnNode fin = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
			addFieldHook(fieldHooks[i++].buildObfFin(fin));
		}
	}
}
