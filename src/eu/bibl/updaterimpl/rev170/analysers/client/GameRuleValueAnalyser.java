package eu.bibl.updaterimpl.rev170.analysers.client;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class GameRuleValueAnalyser extends Analyser {
	
	public GameRuleValueAnalyser() {
		super("GameRuleValue");
		hooks = new FieldHook[] { new FieldHook("getValueString", "Ljava/lang/String;", "Ljava/lang/String;"), new FieldHook("getValueBoolean", "Z", "Z"), new FieldHook("getValueInt", "I", "I"), new FieldHook("getValueDouble", "D", "D"), };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c == null)
			return false;
		return c.getRefactoredName().equals("GameRuleValue");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "client/IGameRuleValue"));
		
		FieldNode valueString = fields(cn, "Ljava/lang/String;").get(0);
		addHook(hooks[0].buildObfFn(valueString));
		
		FieldNode valueBoolean = fields(cn, "Z").get(0);
		addHook(hooks[1].buildObfFn(valueBoolean));
		
		FieldNode valueInt = fields(cn, "I").get(0);
		addHook(hooks[2].buildObfFn(valueInt));
		
		FieldNode valueDouble = fields(cn, "D").get(0);
		addHook(hooks[3].buildObfFn(valueDouble));
	}
}