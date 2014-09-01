package eu.bibl.updaterimpl.rev170.analysers.entity.combat;
public class CombatTrackerAnalyser extends Analyser {
	
	private static final int[] PATTERN = new int[] {
			NEW,
			DUP,
			ALOAD,
			ALOAD,
			GETFIELD,
			GETFIELD,
			FLOAD,
			FLOAD,
			ALOAD,
			GETFIELD,
			ALOAD,
			GETFIELD,
			GETFIELD,
			INVOKESPECIAL,
			ASTORE };
	
	public CombatTrackerAnalyser(ClassContainer container, HookMap hookMap) {
		super("CombatTracker", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getDamageSources", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldMappingData("getFighter", "L" + MinecraftAnalyser.INTERFACES + "entity/IEntityLivingBase;"),
		
		};
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "death.attack.generic");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/combat/ICombatTracker"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
		addFieldHook(fieldHooks[1].buildObfFn(fields(cn, "L" + hookMap.getClassByRefactoredName("EntityLivingBase").getObfuscatedName() + ";").get(0)));
		
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, PATTERN);
			if (is.match()) {
				TypeInsnNode tin = (TypeInsnNode) is.getMatches().get(0)[0];
				hookMap.addClass(new ClassMappingData(tin.desc, "CombatEntry"));
			}
		}
		
		EntityLivingBaseAnalyser analyser = (EntityLivingBaseAnalyser) analysers.get("EntityLivingBase");
		ClassNode node = analysisMap.requestNode(analyser.getClassMappingData().getObfuscatedName());
		analyser.addHook(analyser.getHooks()[0].buildObfFn(fields(node, "L" + cn.name + ";").get(0)));
	}
}
