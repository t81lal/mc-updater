package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C13PacketPlayerAbilitiesAnalyser extends PlayPacketAnalyser{
	public C13PacketPlayerAbilitiesAnalyser(ClassContainer container, HookMap hookMap) {
		super("C13PacketPlayerAbilities", container, hookMap);
		fieldHooks = new FieldMappingData[]{
			new FieldMappingData("isInvincible", "Z", "Z"),
			new FieldMappingData("canFly", "Z", "Z"),
			new FieldMappingData("isFlying", "Z", "Z"),
			new FieldMappingData("isCreative", "Z", "Z"),
			new FieldMappingData("getFlyingSpeed", "F", "F"),
			new FieldMappingData("getWalkingSpeed", "F", "F")
		};
	}
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getAll(m);
		for(int i=0; i < fins.length; i++){
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
	}
	
	private FieldInsnNode[] getAll(MethodNode m){
		FieldInsnNode[] fins = new FieldInsnNode[6];
		AbstractInsnNode last = m.instructions.getFirst();
		int i = 0;
		while(i < 6){
			String s = "";
			while(!s.equals(cn.name)){
				last = (MethodInsnNode) getNext(last == null ? m.instructions.getFirst() : last.getNext(), INVOKEVIRTUAL);
				s = ((MethodInsnNode) last).owner;
				if(s.equals(cn.name))
					break;
			}
			
			FieldInsnNode fin = getFrom((MethodInsnNode) last);
			last = last.getNext();
			fins[i++] = fin;
		}
		return fins;
	}
	
	private FieldInsnNode getFrom(MethodInsnNode min){
		for(MethodNode m : methods(cn)){
			if(m.name.equals(min.name) && m.desc.equals(min.desc)){
				return (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
			}
		}
		return null;
	}
}
