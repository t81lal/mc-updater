package eu.bibl.updaterimpl.rev170.analysers.entity.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class DamageSourceAnalyser extends Analyser {
	
	public DamageSourceAnalyser(ClassContainer container, HookMap hookMap) {
		super("DamageSource", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("isFireDamage"), new MappingData("Z", "Z"), false),
				/* 1 */new FieldMappingData(new MappingData("isUnblockable"), new MappingData("Z", "Z"), false),
				/* 2 */new FieldMappingData(new MappingData("getHungerDamage"), new MappingData("F", "F"), false),
				/* 3 */new FieldMappingData(new MappingData("isDamageAllowedInCreativeMode"), new MappingData("Z", "Z"), false),
				/* 4 */new FieldMappingData(new MappingData("isMagicDamage"), new MappingData("Z", "Z"), false),
				/* 5 */new FieldMappingData(new MappingData("isProjectile"), new MappingData("Z", "Z"), false),
				/* 6 */new FieldMappingData(new MappingData("isDifficultyScaled"), new MappingData("Z", "Z"), false),
				/* 7 */new FieldMappingData(new MappingData("isExplosion"), new MappingData("Z", "Z"), false),
				/* 8 */new FieldMappingData(new MappingData("getDamageType"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false) };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData(new MappingData("setFireDamage"), new MappingData("()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), null, null, false),
				new CallbackMappingData(new MappingData("setBypassesArmour"), new MappingData("()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), null, null, false),
				new CallbackMappingData(new MappingData("setDamageAllowedInCreativeMode"), new MappingData("()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), null, null, false),
				new CallbackMappingData(new MappingData("setMagicDamage"), new MappingData("()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), null, null, false),
				new CallbackMappingData(new MappingData("setProjectile"), new MappingData("()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), null, null, false),
				new CallbackMappingData(new MappingData("setDifficultyScaled"), new MappingData("()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), null, null, false),
				new CallbackMappingData(new MappingData("setExplosion"), new MappingData("()L" + MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource;"), null, null, false), };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "outOfWorld");
	}
	
	@Override
	public InterfaceMappingData run() {
		MinecraftAnalyser minecraftAnalyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
		int sources = 13;
		for (MethodNode m : cn.methods()) {
			if (m.name.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == LDC) {
						FieldInsnNode fin = (FieldInsnNode) InsnUtil.getNext(ain, PUTSTATIC);
						minecraftAnalyser.addField(minecraftAnalyser.getFieldHooks()[sources++].buildObf(fin));
						
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
		
		for (MethodNode m : cn.methods()) {
			if (InsnUtil.containsLdc(m, "arrow")) {
				findProjectile(m);
			}
			if (InsnUtil.containsLdc(m, "explosion")) {
				findDifficultyExplosion(m);
			}
		}
		
		addField(fieldHooks[8].buildObf(InsnUtil.fields(cn, "Ljava/lang/String;").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/combat/IDamageSource");
	}
	
	private void findProjectile(MethodNode m) {
		MethodInsnNode[] mins = getMethodNodes(m, INVOKEVIRTUAL);
		MethodInsnNode call = mins[mins.length - 1];
		m = getMethod(call);
		FieldInsnNode fin = (FieldInsnNode) InsnUtil.getNext(m.instructions.getFirst(), PUTFIELD);
		addField(fieldHooks[5].buildObf(fin));
		addMethod(methodHooks[4].buildObfMethod(m));
	}
	
	private void findDifficultyExplosion(MethodNode old) {
		MethodInsnNode[] mins = getMethodNodes(old, INVOKEVIRTUAL);
		MethodInsnNode call = mins[mins.length - 2];
		MethodNode newM = getMethod(call);
		FieldInsnNode fin = (FieldInsnNode) InsnUtil.getNext(newM.instructions.getFirst(), PUTFIELD);
		addField(fieldHooks[6].buildObf(fin));
		addMethod(methodHooks[5].buildObfMethod(newM));
		
		call = mins[mins.length - 1];
		newM = getMethod(call);
		fin = (FieldInsnNode) InsnUtil.getNext(newM.instructions.getFirst(), PUTFIELD);
		addField(fieldHooks[7].buildObf(fin));
		addMethod(methodHooks[6].buildObfMethod(newM));
	}
	
	private void findInFire(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext());
		addField(fieldHooks[0].buildObf(getFieldNodes(m, PUTFIELD)[0]));
		addMethod(methodHooks[0].buildObfMethod(m));
	}
	
	private void fireIsUnblockable(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addField(fieldHooks[1].buildObf(fins[0]));
		addField(fieldHooks[2].buildObf(fins[1]));
		addMethod(methodHooks[1].buildObfMethod(m));
	}
	
	private void findCreativeDamage(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addField(fieldHooks[3].buildObf(fins[0]));
		addMethod(methodHooks[2].buildObfMethod(m));
	}
	
	private void findMagicDamage(LdcInsnNode ldc) {
		MethodNode m = getMethod((MethodInsnNode) ldc.getNext().getNext().getNext());
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addField(fieldHooks[4].buildObf(fins[0]));
		addMethod(methodHooks[3].buildObfMethod(m));
	}
	
	private MethodNode getMethod(MethodInsnNode min) {
		for (MethodNode m : cn.methods()) {
			if (m.name.equals(min.name) && m.desc.equals(min.desc))
				return m;
		}
		return null;
	}
	
	private static MethodInsnNode[] getMethodNodes(MethodNode m, int opcode) {
		List<MethodInsnNode> fins = new ArrayList<MethodInsnNode>();
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain.getOpcode() == opcode) {
				fins.add((MethodInsnNode) ain);
			}
		}
		return fins.toArray(new MethodInsnNode[fins.size()]);
	}
	
	private static FieldInsnNode[] getFieldNodes(MethodNode m, int opcode) {
		List<FieldInsnNode> fins = new ArrayList<FieldInsnNode>();
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain.getOpcode() == opcode) {
				fins.add((FieldInsnNode) ain);
			}
		}
		return fins.toArray(new FieldInsnNode[fins.size()]);
	}
}
