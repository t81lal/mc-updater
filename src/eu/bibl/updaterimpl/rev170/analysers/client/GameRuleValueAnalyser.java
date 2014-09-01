package eu.bibl.updaterimpl.rev170.analysers.client;
public class GameRuleValueAnalyser extends Analyser {
	
	public GameRuleValueAnalyser(ClassContainer container, HookMap hookMap) {
		super("GameRuleValue", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getValueString", "Ljava/lang/String;", "Ljava/lang/String;"), new FieldMappingData("getValueBoolean", "Z", "Z"), new FieldMappingData("getValueInt", "I", "I"), new FieldMappingData("getValueDouble", "D", "D"), };
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c == null)
			return false;
		return c.getRefactoredName().equals("GameRuleValue");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/IGameRuleValue"));
		
		FieldNode valueString = fields(cn, "Ljava/lang/String;").get(0);
		addFieldHook(fieldHooks[0].buildObfFn(valueString));
		
		FieldNode valueBoolean = fields(cn, "Z").get(0);
		addFieldHook(fieldHooks[1].buildObfFn(valueBoolean));
		
		FieldNode valueInt = fields(cn, "I").get(0);
		addFieldHook(fieldHooks[2].buildObfFn(valueInt));
		
		FieldNode valueDouble = fields(cn, "D").get(0);
		addFieldHook(fieldHooks[3].buildObfFn(valueDouble));
	}
}
