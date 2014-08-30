package eu.bibl.updaterimpl.rev170.analysers.chat;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class ChatComponentStyleAnalyser extends Analyser {
	
	public ChatComponentStyleAnalyser() {
		super("ChatComponentStyle");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook owner = map.getClassByObfuscatedName(cn.name);
		if (owner == null)
			return false;
		return owner.getRefactoredName().equals("ChatComponentStyle");
	}
	
	@Override
	public void run() {
		ClassHook chatComponent = new ClassHook((String) (cn.interfaces.get(0)), "ChatComponent");
		map.addClass(chatComponent);
		
		InterfaceHook hook = new InterfaceHook(classHook, INTERFACES + "chat/IChatComponentStyle", INTERFACES + "chat/IChatComponent");
		classHook.setInterfaceHook(hook);
	}
}