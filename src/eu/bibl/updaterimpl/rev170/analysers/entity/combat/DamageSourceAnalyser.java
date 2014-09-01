package eu.bibl.updaterimpl.rev170.analysers.entity.combat;
public class DamageSourceAnalyser extends Analyser {
	
	public DamageSourceAnalyser(ClassContainer container, HookMap hookMap) {
		super("DamageSource", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("isFireDamage", "Z", "Z"),
				new FieldMappingData("isUnblockable", "Z", "Z"),
				new FieldMappingData("getHungerDamage", "F", "F"),
				new FieldMappingData("isDamageAllowedInCreativeMode", "Z", "Z"),
				new FieldMappingData("isMagicDamage", "Z", "Z"),
				new FieldMappingData("isProjectile", "Z", "Z"),
				new FieldMappingData("isDifficultyScaled", "Z", "Z"),
				new FieldMappingData("isExplosion", "Z", "Z"),
				new FieldMappingData("getDamageType", "Ljava/lang/String;", "Ljava/lang/String;") };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData("setFireDamage", "()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"),
				new CallbackMappingData("setBypassesArmour", "()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"),
				new CallbackMappingData("setDamageAllowedInCreativeMode", "()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"),
				new CallbackMappingData("setMagicDamage", "()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"),
				new CallbackMappingData("setProjectile", "()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"),
				new CallbackMappingData("setDifficultyScaled", "()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"),
				new CallbackMappingData("setExplosion", "()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), };
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "outOfWorld");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource"));
		
		MinecraftAnalyser minecraftAnalyser = (MinecraftAnalyser) analysers.get("Minecraft");
		int sources = 13;
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == LDC) {
						FieldInsnNode fin = (FieldInsnNode) getNext(ain, PUTSTATIC);
						addMinecraftHook(minecraftAnalyser.getHooks()[sources++].buildObfFin(fin));
						
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst.equals("inFire")) {
							findInFire(ldc);
						} else if (ldc.cst.equals("onFire")) {
							fireIsUnblockable(ldc);
						} else if (ldc.cst.equals("outOfWorld")) {
							findCreativeDamage(ldc);
						} else if (ldc.cst.equals("magic")) {
							findMagicDamage(ldc);
						}
					}
				}
				break;
			}
		}
		
		for(MethodNode m : methods(cn)) {
			if (containsLdc(m, "arrow")) {
				findProjectile(m);
			}
			if (containsLdc(m, "explosion")) {
				findDifficultyExplosion(m);
			}
		}
		
		addFieldHook(fieldHooks[8].buildObfFn(fields(cn, "Ljava/lang/String;").get(0)));
	}
	
	private void findProjectile(MethodNode m) {
		MethodInsnNode[] mins = getMethodNodes(m, INVOKEVIRTUAL);
		MethodInsnNode call = mins[mins.length - 1];
		m = getMethod(call);
		FieldInsnNode fin = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
		addFieldHook(fieldHooks[5].buildObfFin(fin));
		addMethodHook(methodHooks[4].buildObfMn(m));
	}
	
	private void findDifficultyExplosion(MethodNode old) {
		MethodInsnNode[] mins = getMethodNodes(old, INVOKEVIRTUAL);
		MethodInsnNode call = mins[mins.length - 2];
		MethodNode newM = getMethod(call);
		FieldInsnNode fin = (FieldInsnNode) getNext(newM.instructions.getFirst(), PUTFIELD);
		addFieldHook(fieldHooks[6].buildObfFin(fin));
		addMethodHook(methodHooks[5].buildObfMn(newM));
		
		call = mins[mins.length - 1];
		newM = getMethod(call);
		fin = (FieldInsnNode) getNext(newM.instructions.getFirst(), PUTFIELD);
		addFieldHook(fieldHooks[7].buildObfFin(fin));
		addMethodHook(methodHooks[6].buildObfMn(newM));
	}
	
	private void findInFire(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext());
		addFieldHook(fieldHooks[0].buildObfFin(getFieldNodes(m, PUTFIELD)[0]));
		addMethodHook(methodHooks[0].buildObfMn(m));
	}
	
	private void fireIsUnblockable(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addFieldHook(fieldHooks[1].buildObfFin(fins[0]));
		addFieldHook(fieldHooks[2].buildObfFin(fins[1]));
		addMethodHook(methodHooks[1].buildObfMn(m));
	}
	
	private void findCreativeDamage(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addFieldHook(fieldHooks[3].buildObfFin(fins[0]));
		addMethodHook(methodHooks[2].buildObfMn(m));
	}
	
	private void findMagicDamage(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addFieldHook(fieldHooks[4].buildObfFin(fins[0]));
		addMethodHook(methodHooks[3].buildObfMn(m));
		
	}
}
