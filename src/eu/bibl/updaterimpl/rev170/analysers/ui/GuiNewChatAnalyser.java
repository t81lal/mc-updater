package eu.bibl.updaterimpl.rev170.analysers.ui;

import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class GuiNewChatAnalyser extends Analyser {
	
	public GuiNewChatAnalyser(ClassContainer container, HookMap hookMap) {
		super("GuiNewChat", container, hookMap);
		methodHooks = new CallbackMappingData[] { new CallbackMappingData(new MappingData("realAddChatMessage"), new MappingData(""), null, null, false) };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData owner = (ClassMappingData) hookMap.getClassByRefactoredName("GuiNewChat");
		if (owner == null)
			return false;
		return owner.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		for (MethodNode mNode : cn.methods()) {
			if (mNode.desc.endsWith(";IIZ)V")) {
				methodHooks[0].getMethodDesc().setRefactoredName(mNode.desc);
				addMethod(methodHooks[0].buildObfMethod(mNode));
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "ui/IGuiNewChat");
	}
}
