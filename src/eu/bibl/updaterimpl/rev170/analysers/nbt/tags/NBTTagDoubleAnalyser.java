package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NBTTagDoubleAnalyser extends NBTTagAnalyser {
	
	public NBTTagDoubleAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagDouble", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getData"), new MappingData("D", "D"), false), };
	}
	
	@Override
	public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagDouble").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[0].buildObf(InsnUtil.fields(cn, "D").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagDouble");
	}
}
