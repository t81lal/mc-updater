package eu.bibl.updaterimpl.rev170.analysers.chat;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class ChatComponentAnalyser extends Analyser {
	
	public ChatComponentAnalyser() {
		super("ChatComponent");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook owner = map.getClassByObfuscatedName(cn.name);
		if (owner == null)
			return false;
		return owner.getRefactoredName().equals("ChatComponent");
	}
	
	@Override
	public void run() {
		InterfaceHook hook = new InterfaceHook(classHook, INTERFACES + "chat/IChatComponent");
		classHook.setInterfaceHook(hook);
		
		for(MethodNode mNode : methods(cn)) {
			if (mNode.desc.equals("()Ljava/lang/String;")) {
				// MethodNode msg = new MethodNode(ACC_PUBLIC + ACC_ABSTRACT,
				// "getMsg", mNode.desc, null, null);
				
			}
		}
	}
}