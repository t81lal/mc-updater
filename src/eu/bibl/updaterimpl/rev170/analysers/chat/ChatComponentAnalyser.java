package eu.bibl.updaterimpl.rev170.analysers.chat;

import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ChatComponentAnalyser extends Analyser {
	
	public ChatComponentAnalyser(ClassContainer container, HookMap hookMap) {
		super("ChatComponent", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		ClassMappingData owner = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if (owner == null)
			return false;
		return owner.getRefactoredName().equals("ChatComponent");
	}
	
	@Override
	public InterfaceMappingData run() {
		for (MethodNode mNode : cn.methods()) {
			if (mNode.desc.equals("()Ljava/lang/String;")) {
				// MethodNode msg = new MethodNode(ACC_PUBLIC + ACC_ABSTRACT,
				// "getMsg", mNode.desc, null, null);
				
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "chat/IChatComponent");
	}
}
