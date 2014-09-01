package eu.bibl.updaterimpl.rev170.analysers.entity.combat;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.asm.ClassNode;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityLivingBaseAnalyser;

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
				/* 0 */new FieldMappingData(new MappingData("getDamageSources"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false),
				/* 1 */new FieldMappingData(new MappingData("getFighter"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "entity/IEntityLivingBase;"), false), };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "death.attack.generic");
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[0].buildObf(InsnUtil.fields(cn, "Ljava/util/List;").get(0)));
		addField(fieldHooks[1].buildObf(InsnUtil.fields(cn, "L" + hookMap.getClassByRefactoredName("EntityLivingBase").getObfuscatedName() + ";").get(0)));
		
		for (MethodNode m : cn.methods()) {
			InstructionSearcher is = new InstructionSearcher(m.instructions, PATTERN);
			if (is.search()) {
				TypeInsnNode tin = (TypeInsnNode) is.getMatches().get(0)[0];
				hookMap.addClass(new ClassMappingData(tin.desc, "CombatEntry", null));
			}
		}
		
		EntityLivingBaseAnalyser analyser = (EntityLivingBaseAnalyser) AnalyserCache.contextGet("EntityLivingBase");
		ClassNode node = getNode(analyser.getClassHook().getObfuscatedName());
		analyser.addField(analyser.getFieldHooks()[0].buildObf(InsnUtil.fields(node, "L" + cn.name + ";").get(0)));
		
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/combat/ICombatTracker");
	}
}
