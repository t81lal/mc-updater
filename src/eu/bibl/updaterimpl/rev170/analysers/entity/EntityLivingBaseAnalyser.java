package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class EntityLivingBaseAnalyser extends Analyser {
	
	private static final int[] SWING_FIELDS_REGEX = new int[] {
			ALOAD,
			ICONST_M1,
			PUTFIELD };
	
	public EntityLivingBaseAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityLivingBase", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getCombatTracker"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "entity/combat/ICombatTracker;"), false),
				/* 1 */new FieldMappingData(new MappingData("getActivePotions"), new MappingData("Ljava/util/HashMap;", "Ljava/util/HashMap;"), false),
				/* 2 */new FieldMappingData(new MappingData("getSwingProgress"), new MappingData("I", "I"), false),
				/* 3 */new FieldMappingData(new MappingData("isSwingInProgress"), new MappingData("Z", "Z"), false), };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if ((c != null) && c.getRefactoredName().equals("EntityLivingBase"))
			return true;
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		hookMap.addClass(new ClassMappingData(cn.superName, "Entity", null));
		
		addField(fieldHooks[1].buildObf(InsnUtil.fields(cn, "Ljava/util/HashMap;").get(0)));
		
		findSwingFields();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityLivingBase");
	}
	
	private void findSwingFields() {
		EntityClientPlayerMPAnalyser playerAnalyser = (EntityClientPlayerMPAnalyser) AnalyserCache.contextGet("EntityClientPlayerMP");
		CallbackMappingData swingItem = playerAnalyser.getMethodHooks()[0];
		MethodNode m = null;
		
		for (MethodNode m1 : cn.methods()) {
			if (m1.name.equals(swingItem.getMethodName().getObfuscatedName()) && m1.desc.equals(swingItem.getMethodDesc().getObfuscatedName()))
				m = m1;
		}
		
		InstructionSearcher is = new InstructionSearcher(m.instructions, SWING_FIELDS_REGEX);
		is.search();
		FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[2];
		addField(fieldHooks[2].buildObf(fin));
		addField(fieldHooks[3].buildObf((FieldInsnNode) InsnUtil.getNext(fin.getNext(), PUTFIELD)));
	}
}
