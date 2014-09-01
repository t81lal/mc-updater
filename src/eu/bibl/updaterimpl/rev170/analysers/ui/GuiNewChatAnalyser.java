package eu.bibl.updaterimpl.rev170.analysers.ui;
public class GuiNewChatAnalyser extends Analyser {
	
	public GuiNewChatAnalyser(ClassContainer container, HookMap hookMap) {
		super("GuiNewChat", container, hookMap);
		methodHooks = new CallbackMappingData[] { new CallbackMappingData("realAddChatMessage", "") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData owner = hookMap.getClassByRefactoredName("GuiNewChat");
		if (owner == null)
			return false;
		return owner.getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		InterfaceHook hook = new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "ui/IGuiNewChat");
		classHook.setInterfaceHook(hook);
		
		for(MethodNode mNode : methods(cn)) {
			if (mNode.desc.endsWith(";IIZ)V")) {
				addMethodHook(methodHooks[0].buildObfMn(mNode).buildRefacDesc(mNode.desc));
			}
		}
	}
}
