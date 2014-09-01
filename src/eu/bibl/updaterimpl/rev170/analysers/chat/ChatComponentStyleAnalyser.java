package eu.bibl.updaterimpl.rev170.analysers.chat;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ChatComponentStyleAnalyser extends Analyser {
	
	public ChatComponentStyleAnalyser(ClassContainer container, HookMap hookMap) {
		super("ChatComponentStyle", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		ClassMappingData owner = hookMap.getClassByOfuscatedName(cn.name);
		if (owner == null)
			return false;
		return owner.getRefactoredName().equals("ChatComponentStyle");
	}
	
	@Override
	public InterfaceMappingData run() {
		ClassMappingData chatComponent = new ClassMappingData((String) (cn.interfaces.get(0)), "ChatComponent");
		hookMap.addClass(chatComponent);
		
		InterfaceHook hook = new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "chat/IChatComponentStyle", MinecraftAnalyser.INTERFACES + "chat/IChatComponent");
		classHook.setInterfaceHook(hook);
	}
}
