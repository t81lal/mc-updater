package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;

public class EntityLivingBaseAnalyser extends Analyser {
	
	private static final int[] SWING_FIELDS_REGEX = new int[] {
			ALOAD,
			ICONST_M1,
			PUTFIELD };
	
	public EntityLivingBaseAnalyser() {
		super("EntityLivingBase");
		hooks = new FieldHook[] {
				new FieldHook("getCombatTracker", "L" + INTERFACES + "entity/combat/ICombatTracker;"),
				new FieldHook("getActivePotions", "Ljava/util/HashMap;", "Ljava/util/HashMap;"),
				new FieldHook("getSwingProgress", "I", "I"),
				new FieldHook("isSwingInProgress", "Z", "Z") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityLivingBase"))
			return true;
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IEntityLivingBase", INTERFACES + "entity/IEntity"));
		map.addClass(new ClassHook(cn.superName, "Entity"));
		
		addHook(hooks[1].buildObfFn(fields(cn, "Ljava/util/HashMap;").get(0)));
		
		findSwingFields();
	}
	
	private void findSwingFields() {
		EntityClientPlayerMPAnalyser playerAnalyser = (EntityClientPlayerMPAnalyser) analysers.get("EntityClientPlayerMP");
		MethodHook swingItem = playerAnalyser.getMethodHooks()[0];
		MethodNode m = getMethod(swingItem);
		
		InsnSearcher is = new InsnSearcher(m.instructions, 0, SWING_FIELDS_REGEX);
		is.match();
		FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[2];
		addHook(hooks[2].buildObfFin(fin));
		addHook(hooks[3].buildObfFin((FieldInsnNode) getNext(fin.getNext(), PUTFIELD)));
	}
}