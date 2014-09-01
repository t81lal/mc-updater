package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NBTTagIntArrayAnalyser extends NBTTagAnalyser {
	
	public NBTTagIntArrayAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagIntArray", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getData"), new MappingData("[I", "[I"), false), };
	}
	
	@Override
	public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagIntArray").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[0].buildObf(InsnUtil.fields(cn, "[I").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagIntArray");
	}
}
