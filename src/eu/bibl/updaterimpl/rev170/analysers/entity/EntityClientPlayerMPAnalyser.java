package eu.bibl.updaterimpl.rev170.analysers.entity;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

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
	
	public EntityClientPlayerMPAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityClientPlayerMP", container, hookMap);
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData(new MappingData("swingItem"), new MappingData("()V", "()V"), null, null, false),
				new CallbackMappingData(new MappingData("sendChatMessage"), new MappingData("(Ljava/lang/String;)V"), null, null, false) };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if ((c != null) && c.getRefactoredName().equals("EntityClientPlayerMP"))
			return true;
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		hookMap.addClass(new ClassMappingData(cn.superName, "EntityPlayerSP", null));
		
		findCallbackMethods();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityClientPlayerMP");
	}
	
	private void findCallbackMethods() {
		for (MethodNode mNode : cn.methods()) {
			if (containsRegex(mNode, SWING_ITEM_REGEX)) {
				addMethod(methodHooks[0].buildObfMethod(mNode));
			} else if (containsRegex(mNode, SEND_CHAT_MESSAGE_REGEX)) {
				addMethod(methodHooks[1].buildObfMethod(mNode));
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
			if (insnCount == (opcodes.length - 1)) {
				return true;
			}
			insnCount++;
		}
		return false;
	}
}
