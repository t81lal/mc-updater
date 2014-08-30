package eu.bibl.updaterimpl.rev170.analysers.entity;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;

public class EntityClientPlayerMPAnalyser extends Analyser {
	
	private static final int[] SWING_ITEM_REGEX = new int[] {
			ALOAD,
			INVOKESPECIAL,
			ALOAD,
			GETFIELD,
			NEW,
			DUP,
			ALOAD,
			ICONST_1,
			INVOKESPECIAL,
			INVOKEVIRTUAL,
			RETURN };
	private static final int[] SEND_CHAT_MESSAGE_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			NEW,
			DUP,
			ALOAD,
			INVOKESPECIAL,
			INVOKEVIRTUAL };
	
	public EntityClientPlayerMPAnalyser() {
		super("EntityClientPlayerMP");
		methodHooks = new MethodHook[] {
				new MethodHook("swingItem", "()V", "()V"),
				new MethodHook("sendChatMessage", "(Ljava/lang/String;)V") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityClientPlayerMP"))
			return true;
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IEntityClientPlayerMP", INTERFACES + "entity/IEntityPlayerSP"));
		map.addClass(new ClassHook(cn.superName, "EntityPlayerSP"));
		
		findCallbackMethods();
	}
	
	private void findCallbackMethods() {
		for(MethodNode mNode : methods(cn)) {
			if (containsRegex(mNode, SWING_ITEM_REGEX)) {
				addHook(methodHooks[0].buildObfMn(mNode));
			} else
				if (containsRegex(mNode, SEND_CHAT_MESSAGE_REGEX)) {
					addHook(methodHooks[1].buildObfMn(mNode));
				}
		}
	}
	
	private boolean containsRegex(MethodNode mNode, int[] opcodes) {
		int insnCount = 0;
		ListIterator<?> it = mNode.instructions.iterator();
		while (it.hasNext()) {
			if (insnCount > opcodes.length)
				return false;
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			int opcode = ain.getOpcode();
			int expectedOpcode = opcodes[insnCount];
			if (opcode != expectedOpcode) {
				return false;
			}
			if (insnCount == opcodes.length - 1) {
				return true;
			}
			insnCount++;
		}
		return false;
	}
}