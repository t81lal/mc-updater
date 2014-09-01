package eu.bibl.updaterimpl.rev170.analysers.client;
public class MovementInputAnalyser extends Analyser {
	
	public MovementInputAnalyser(ClassContainer container, HookMap hookMap) {
		super("MovementInput", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getMovementStafe", "F", "F"),
				new FieldMappingData("getMovementForward", "F", "F"),
				new FieldMappingData("isJumping", "Z", "Z"),
				new FieldMappingData("isSneaking", "Z", "Z") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByObfuscatedName(cn.name);
		if (hook == null)
			return false;
		return hook.getRefactoredName().equals("MovementInput");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/IMovementInput"));
		
		addFieldHook(fieldHooks[3].buildObfFn(fields(cn, "Z").get(1)));
	}
}
