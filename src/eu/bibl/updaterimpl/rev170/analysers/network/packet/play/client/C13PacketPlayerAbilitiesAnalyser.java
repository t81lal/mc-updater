package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;

public class C13PacketPlayerAbilitiesAnalyser extends PlayPacketAnalyser{

	public C13PacketPlayerAbilitiesAnalyser() {
		super("C13PacketPlayerAbilities");
		hooks = new FieldHook[]{
			new FieldHook("isInvincible", "Z", "Z"),
			new FieldHook("canFly", "Z", "Z"),
			new FieldHook("isFlying", "Z", "Z"),
			new FieldHook("isCreative", "Z", "Z"),
			new FieldHook("getFlyingSpeed", "F", "F"),
			new FieldHook("getWalkingSpeed", "F", "F")
		};
	}

	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getAll(m);
		for(int i=0; i < fins.length; i++){
			addHook(hooks[i].buildObfFin(fins[i]));
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