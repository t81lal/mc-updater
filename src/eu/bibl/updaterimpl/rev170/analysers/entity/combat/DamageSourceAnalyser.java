package eu.bibl.updaterimpl.rev170.analysers.entity.combat;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class DamageSourceAnalyser extends Analyser {
	
	public DamageSourceAnalyser() {
		super("DamageSource");
		hooks = new FieldHook[] {
				new FieldHook("isFireDamage", "Z", "Z"),
				new FieldHook("isUnblockable", "Z", "Z"),
				new FieldHook("getHungerDamage", "F", "F"),
				new FieldHook("isDamageAllowedInCreativeMode", "Z", "Z"),
				new FieldHook("isMagicDamage", "Z", "Z"),
				new FieldHook("isProjectile", "Z", "Z"),
				new FieldHook("isDifficultyScaled", "Z", "Z"),
				new FieldHook("isExplosion", "Z", "Z"),
				new FieldHook("getDamageType", "Ljava/lang/String;", "Ljava/lang/String;") };
		methodHooks = new MethodHook[] {
				new MethodHook("setFireDamage", "()L" + INTERFACES + "entity/combat/IDamageSource;"),
				new MethodHook("setBypassesArmour", "()L" + INTERFACES + "entity/combat/IDamageSource;"),
				new MethodHook("setDamageAllowedInCreativeMode", "()L" + INTERFACES + "entity/combat/IDamageSource;"),
				new MethodHook("setMagicDamage", "()L" + INTERFACES + "entity/combat/IDamageSource;"),
				new MethodHook("setProjectile", "()L" + INTERFACES + "entity/combat/IDamageSource;"),
				new MethodHook("setDifficultyScaled", "()L" + INTERFACES + "entity/combat/IDamageSource;"),
				new MethodHook("setExplosion", "()L" + INTERFACES + "entity/combat/IDamageSource;"), };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "outOfWorld");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/combat/IDamageSource"));
		
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
		
		addHook(hooks[8].buildObfFn(fields(cn, "Ljava/lang/String;").get(0)));
	}
	
	private void findProjectile(MethodNode m) {
		MethodInsnNode[] mins = getMethodNodes(m, INVOKEVIRTUAL);
		MethodInsnNode call = mins[mins.length - 1];
		m = getMethod(call);
		FieldInsnNode fin = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
		addHook(hooks[5].buildObfFin(fin));
		addHook(methodHooks[4].buildObfMn(m));
	}
	
	private void findDifficultyExplosion(MethodNode old) {
		MethodInsnNode[] mins = getMethodNodes(old, INVOKEVIRTUAL);
		MethodInsnNode call = mins[mins.length - 2];
		MethodNode newM = getMethod(call);
		FieldInsnNode fin = (FieldInsnNode) getNext(newM.instructions.getFirst(), PUTFIELD);
		addHook(hooks[6].buildObfFin(fin));
		addHook(methodHooks[5].buildObfMn(newM));
		
		call = mins[mins.length - 1];
		newM = getMethod(call);
		fin = (FieldInsnNode) getNext(newM.instructions.getFirst(), PUTFIELD);
		addHook(hooks[7].buildObfFin(fin));
		addHook(methodHooks[6].buildObfMn(newM));
	}
	
	private void findInFire(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext());
		addHook(hooks[0].buildObfFin(getFieldNodes(m, PUTFIELD)[0]));
		addHook(methodHooks[0].buildObfMn(m));
	}
	
	private void fireIsUnblockable(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addHook(hooks[1].buildObfFin(fins[0]));
		addHook(hooks[2].buildObfFin(fins[1]));
		addHook(methodHooks[1].buildObfMn(m));
	}
	
	private void findCreativeDamage(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addHook(hooks[3].buildObfFin(fins[0]));
		addHook(methodHooks[2].buildObfMn(m));
	}
	
	private void findMagicDamage(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addHook(hooks[4].buildObfFin(fins[0]));
		addHook(methodHooks[3].buildObfMn(m));
		
	}
}