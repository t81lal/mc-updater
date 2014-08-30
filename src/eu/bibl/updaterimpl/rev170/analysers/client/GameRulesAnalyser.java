package eu.bibl.updaterimpl.rev170.analysers.client;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;

public class GameRulesAnalyser extends Analyser {
	
	public GameRulesAnalyser() {
		super("GameRules");
		hooks = new FieldHook[] { new FieldHook("getSettings", "Ljava/util/TreeMap;", "Ljava/util/TreeMap;") };
		methodHooks = new MethodHook[] { new MethodHook("addGameRule", "(Ljava/lang/String;Ljava/lang/String;)V", "(Ljava/lang/String;Ljava/lang/String;)V") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c == null)
			return false;
		return c.getRefactoredName().equals("GameRules");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "client/IGameRules"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "Ljava/util/TreeMap;").get(0)));
		
		for(MethodNode m : methods(cn, "(Ljava/lang/String;Ljava/lang/String;)V")) {
			TypeInsnNode tin = (TypeInsnNode) getNext(m.instructions.getFirst(), NEW);
			if (tin != null) {
				addHook(methodHooks[0].buildObfMn(m));
				map.addClass(new ClassHook(tin.desc, "GameRuleValue"));
			}
		}
	}
}