package eu.bibl.updaterimpl.rev170.analysers.chat;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class TranslatedChatComponentAnalyser extends Analyser {
	
	public TranslatedChatComponentAnalyser() {
		super("TranslatedChatComponent");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		for(MethodNode mNode : methods(cn)) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst != null && ldc.cst.toString().equals("tile.bed.occupied")) {
						AbstractInsnNode prevAin = ain.getPrevious();
						TypeInsnNode newInsn = (TypeInsnNode) prevAin.getPrevious();
						ClassHook tccOwner = new ClassHook(newInsn.desc, "TranslatedChatComponent");
						map.addClass(tccOwner);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		// NOTE, DO NOT USE cn.name AS THIS IS BLOCKBED CLASS, NOT TCC!
		classHook = map.getClassByRefactoredName("TranslatedChatComponent");
		InterfaceHook hook = new InterfaceHook(classHook, INTERFACES + "chat/ITranslatableChatComponent", INTERFACES + "chat/IChatComponentStyle");
		classHook.setInterfaceHook(hook);
		
		cn = analysisMap.requestNode(classHook.getObfuscatedName());
		ClassHook chatStyle = new ClassHook(cn.superName, "ChatComponentStyle");
		map.addClass(chatStyle);
	}
}