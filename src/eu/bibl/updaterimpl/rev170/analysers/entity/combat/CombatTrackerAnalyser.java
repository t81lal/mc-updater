package eu.bibl.updaterimpl.rev170.analysers.entity.combat;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
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
	
	public CombatTrackerAnalyser() {
		super("CombatTracker");
		hooks = new FieldHook[] {
				new FieldHook("getDamageSources", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldHook("getFighter", "L" + INTERFACES + "entity/IEntityLivingBase;"),
		
		};
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "death.attack.generic");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/combat/ICombatTracker"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
		addHook(hooks[1].buildObfFn(fields(cn, "L" + map.getClassByRefactoredName("EntityLivingBase").getObfuscatedName() + ";").get(0)));
		
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, PATTERN);
			if (is.match()) {
				TypeInsnNode tin = (TypeInsnNode) is.getMatches().get(0)[0];
				map.addClass(new ClassHook(tin.desc, "CombatEntry"));
			}
		}
		
		EntityLivingBaseAnalyser analyser = (EntityLivingBaseAnalyser) analysers.get("EntityLivingBase");
		ClassNode node = analysisMap.requestNode(analyser.getClassHook().getObfuscatedName());
		analyser.addHook(analyser.getHooks()[0].buildObfFn(fields(node, "L" + cn.name + ";").get(0)));
	}
}