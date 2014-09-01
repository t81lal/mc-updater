package eu.bibl.updaterimpl.rev170.analysers.client;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class GameRulesAnalyser extends Analyser {
	
	public GameRulesAnalyser(ClassContainer container, HookMap hookMap) {
		super("GameRules", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData(new MappingData("getSettings"), new MappingData("Ljava/util/TreeMap;", "Ljava/util/TreeMap;"), false) };
		methodHooks = new CallbackMappingData[] { new CallbackMappingData(new MappingData("addGameRule"), new MappingData("(Ljava/lang/String;Ljava/lang/String;)V", "(Ljava/lang/String;Ljava/lang/String;)V"), null, null, false) };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if (c == null)
			return false;
		return c.getRefactoredName().equals("GameRules");
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[0].buildObf(InsnUtil.fields(cn, "Ljava/util/TreeMap;").get(0)));
		
		for (MethodNode m : InsnUtil.methods(cn, "(Ljava/lang/String;Ljava/lang/String;)V")) {
			TypeInsnNode tin = (TypeInsnNode) InsnUtil.getNext(m.instructions.getFirst(), NEW);
			if (tin != null) {
				addMethod(methodHooks[0].buildObfMethod(m));
				hookMap.addClass(new ClassMappingData(tin.desc, "GameRuleValue", null));
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/IGameRules");
	}
}
