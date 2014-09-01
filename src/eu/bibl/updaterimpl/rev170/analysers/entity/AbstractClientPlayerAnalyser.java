package eu.bibl.updaterimpl.rev170.analysers.entity;
public class AbstractClientPlayerAnalyser extends Analyser {
	
	public AbstractClientPlayerAnalyser(ClassContainer container, HookMap hookMap) {
		super("AbstractClientPlayer", container, hookMap);
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("AbstractClientPlayer"))
			return true;
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IAbstractClientPlayer", MinecraftAnalyser.INTERFACES + "entity/IEntityPlayer"));
		
		hookMap.addClass(new ClassMappingData(cn.superName, "EntityPlayer"));
	}
}
