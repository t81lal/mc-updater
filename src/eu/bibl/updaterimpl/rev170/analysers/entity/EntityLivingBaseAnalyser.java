package eu.bibl.updaterimpl.rev170.analysers.entity;
public class EntityLivingBaseAnalyser extends Analyser {
	
	private static final int[] SWING_FIELDS_REGEX = new int[] {
			ALOAD,
			ICONST_M1,
			PUTFIELD };
	
	public EntityLivingBaseAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityLivingBase", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getCombatTracker", "L" + MinecraftAnalyser.INTERFACES + "entity/combat/ICombatTracker;"),
				new FieldMappingData("getActivePotions", "Ljava/util/HashMap;", "Ljava/util/HashMap;"),
				new FieldMappingData("getSwingProgress", "I", "I"),
				new FieldMappingData("isSwingInProgress", "Z", "Z") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityLivingBase"))
			return true;
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityLivingBase", MinecraftAnalyser.INTERFACES + "entity/IEntity"));
		hookMap.addClass(new ClassMappingData(cn.superName, "Entity"));
		
		addFieldHook(fieldHooks[1].buildObfFn(fields(cn, "Ljava/util/HashMap;").get(0)));
		
		findSwingFields();
	}
	
	private void findSwingFields() {
		EntityClientPlayerMPAnalyser playerAnalyser = (EntityClientPlayerMPAnalyser) analysers.get("EntityClientPlayerMP");
		CallbackMappingData swingItem = playerAnalyser.getCallbackMappingDatas()[0];
		MethodNode m = getMethod(swingItem);
		
		InsnSearcher is = new InsnSearcher(m.instructions, 0, SWING_FIELDS_REGEX);
		is.match();
		FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[2];
		addFieldHook(fieldHooks[2].buildObfFin(fin));
		addFieldHook(fieldHooks[3].buildObfFin((FieldInsnNode) getNext(fin.getNext(), PUTFIELD)));
	}
}
