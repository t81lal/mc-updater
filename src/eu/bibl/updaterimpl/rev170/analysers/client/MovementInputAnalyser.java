package eu.bibl.updaterimpl.rev170.analysers.client;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class MovementInputAnalyser extends Analyser {
	
	public MovementInputAnalyser() {
		super("MovementInput");
		hooks = new FieldHook[] {
				new FieldHook("getMovementStafe", "F", "F"),
				new FieldHook("getMovementForward", "F", "F"),
				new FieldHook("isJumping", "Z", "Z"),
				new FieldHook("isSneaking", "Z", "Z") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook hook = map.getClassByObfuscatedName(cn.name);
		if (hook == null)
			return false;
		return hook.getRefactoredName().equals("MovementInput");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "client/IMovementInput"));
		
		addHook(hooks[3].buildObfFn(fields(cn, "Z").get(1)));
	}
}