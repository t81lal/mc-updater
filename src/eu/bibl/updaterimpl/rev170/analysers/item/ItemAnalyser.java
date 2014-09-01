package eu.bibl.updaterimpl.rev170.analysers.item;

import java.util.List;

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

public class ItemAnalyser extends Analyser {
	
	public ItemAnalyser(ClassContainer container, HookMap hookMap) {
		super("Item", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getPotionEffect"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false),
				/* 1 */new FieldMappingData(new MappingData("getName"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false), };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if (c != null)
			return c.getRefactoredName().equals("Item");
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		List<FieldNode> stringFieldNodes = InsnUtil.fields(cn, "Ljava/lang/String;");
		FieldNode potionEffectFieldNode = stringFieldNodes.get(0);
		FieldNode nameFieldNode = stringFieldNodes.get(1);
		addField(fieldHooks[0].buildObf(potionEffectFieldNode));
		addField(fieldHooks[1].buildObf(nameFieldNode));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/IItem");
	}
}
