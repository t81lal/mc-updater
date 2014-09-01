package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NBTTagListAnalyser extends NBTTagAnalyser {
	
	public NBTTagListAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagList", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getData"), new MappingData("Ljava/util/List;", "Ljava/util/List;"), false),
				/* 1 */new FieldMappingData(new MappingData("getTagType"), new MappingData("B", "B"), false), };
	}
	
	@Override
	public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagList").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[0].buildObf(InsnUtil.fields(cn, "Ljava/util/List;").get(0)));
		addField(fieldHooks[1].buildObf(InsnUtil.fields(cn, "B").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagList");
	}
}
