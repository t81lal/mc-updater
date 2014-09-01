package eu.bibl.updaterimpl.rev170.analysers.entity;
public class UseEntityActionAnalyser extends Analyser {
	
	public UseEntityActionAnalyser(ClassContainer container, HookMap hookMap) {
		super("UseEntityAction", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getActionID", "I", "I") };
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "INTERACT");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IUseEntityAction"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[8].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
		
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("<init>")) {
				addFieldHook(fieldHooks[0].buildObfFin((FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD)));
				break;
			}
		}
	}
}
