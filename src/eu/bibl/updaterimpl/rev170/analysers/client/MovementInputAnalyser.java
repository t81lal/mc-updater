package eu.bibl.updaterimpl.rev170.analysers.client;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class MovementInputAnalyser extends Analyser {
	
	public MovementInputAnalyser(ClassContainer container, HookMap hookMap) {
		super("MovementInput", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getMovementStafe"), new MappingData("F", "F"), false),
				/* 1 */new FieldMappingData(new MappingData("getMovementForward"), new MappingData("F", "F"), false),
				/* 2 */new FieldMappingData(new MappingData("isJumping"), new MappingData("Z", "Z"), false),
				/* 3 */new FieldMappingData(new MappingData("isSneaking"), new MappingData("Z", "Z"), false) };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData hook = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if (hook == null)
			return false;
		return hook.getRefactoredName().equals("MovementInput");
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[3].buildObf(InsnUtil.fields(cn, "Z").get(1)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/IMovementInput");
	}
}
