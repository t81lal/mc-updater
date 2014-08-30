package eu.bibl.updaterimpl.rev170.analysers.ui;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;

public class GuiNewChatAnalyser extends Analyser {
	
	public GuiNewChatAnalyser() {
		super("GuiNewChat");
		methodHooks = new MethodHook[] { new MethodHook("realAddChatMessage", "") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook owner = map.getClassByRefactoredName("GuiNewChat");
		if (owner == null)
			return false;
		return owner.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		InterfaceHook hook = new InterfaceHook(classHook, INTERFACES + "ui/IGuiNewChat");
		classHook.setInterfaceHook(hook);
		
		for(MethodNode mNode : methods(cn)) {
			if (mNode.desc.endsWith(";IIZ)V")) {
				addHook(methodHooks[0].buildObfMn(mNode).buildRefacDesc(mNode.desc));
			}
		}
	}
}