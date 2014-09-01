package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NBTTagLongAnalyser extends NBTTagAnalyser {
	
	public NBTTagLongAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagLong", container, hookMap);
		fieldHooks = new FieldMappingData[] {
		/* 0 */new FieldMappingData(new MappingData("getData"), new MappingData("J", "J"), false), };
	}
	
	@Override
	public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagLong").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[0].buildObf(InsnUtil.fields(cn, "J").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagLong");
	}
}
