package eu.bibl.updaterimpl.rev170.analysers.entity.combat;
public class CombatEntryAnalyser extends Analyser {
	
	public CombatEntryAnalyser(ClassContainer container, HookMap hookMap) {
		super("CombatEntry", container, hookMap);
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByRefactoredName("CombatEntry");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/combat/ICombatEntry"));
	}
}
