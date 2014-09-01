package eu.bibl.updaterimpl.rev170.analysers.client;
public class GameRulesAnalyser extends Analyser {
	
	public GameRulesAnalyser(ClassContainer container, HookMap hookMap) {
		super("GameRules", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getSettings", "Ljava/util/TreeMap;", "Ljava/util/TreeMap;") };
		methodHooks = new CallbackMappingData[] { new CallbackMappingData("addGameRule", "(Ljava/lang/String;Ljava/lang/String;)V", "(Ljava/lang/String;Ljava/lang/String;)V") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c == null)
			return false;
		return c.getRefactoredName().equals("GameRules");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/IGameRules"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Ljava/util/TreeMap;").get(0)));
		
		for(MethodNode m : methods(cn, "(Ljava/lang/String;Ljava/lang/String;)V")) {
			TypeInsnNode tin = (TypeInsnNode) getNext(m.instructions.getFirst(), NEW);
			if (tin != null) {
				addMethodHook(methodHooks[0].buildObfMn(m));
				hookMap.addClass(new ClassMappingData(tin.desc, "GameRuleValue"));
			}
		}
	}
}
