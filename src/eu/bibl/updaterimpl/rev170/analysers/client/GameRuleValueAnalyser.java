package eu.bibl.updaterimpl.rev170.analysers.client;

import org.objectweb.asm.tree.FieldNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class GameRuleValueAnalyser extends Analyser {
	
	public GameRuleValueAnalyser(ClassContainer container, HookMap hookMap) {
		super("GameRuleValue", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getValueString"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false),
				/* 1 */new FieldMappingData(new MappingData("getValueBoolean"), new MappingData("Z", "Z"), false),
				/* 2 */new FieldMappingData(new MappingData("getValueInt"), new MappingData("I", "I"), false),
				/* 3 */new FieldMappingData(new MappingData("getValueDouble"), new MappingData("D", "D"), false) };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if (c == null)
			return false;
		return c.getRefactoredName().equals("GameRuleValue");
	}
	
	@Override
	public InterfaceMappingData run() {
		FieldNode valueString = InsnUtil.fields(cn, "Ljava/lang/String;").get(0);
		addField(fieldHooks[0].buildObf(valueString));
		
		FieldNode valueBoolean = InsnUtil.fields(cn, "Z").get(0);
		addField(fieldHooks[1].buildObf(valueBoolean));
		
		FieldNode valueInt = InsnUtil.fields(cn, "I").get(0);
		addField(fieldHooks[2].buildObf(valueInt));
		
		FieldNode valueDouble = InsnUtil.fields(cn, "D").get(0);
		addField(fieldHooks[3].buildObf(valueDouble));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/IGameRuleValue");
	}
}
