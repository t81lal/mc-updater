package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.FieldNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class EntityPlayerAnalyser extends Analyser {
	
	public EntityPlayerAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityPlayer", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getInventory"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "item/container/IPlayerInventory;"), false),
				/* 1 */new FieldMappingData(new MappingData("getInventoryContainer"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "item/container/IContainer;"), false),
				/* 2 */new FieldMappingData(new MappingData("getOpenContainer"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "item/container/IContainer;"), false), };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if ((c != null) && c.getRefactoredName().equals("EntityPlayer"))
			return true;
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		hookMap.addClass(new ClassMappingData(cn.superName, "EntityLivingBase", null));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityPlayer");
	}
	
	// Called from PlayerInventoryAnalyser
	public void findPlayerInventory(ClassMappingData playerInventoryHook) {
		for (FieldNode fNode : cn.fields()) {
			if (fNode.desc.equals("L" + playerInventoryHook.getObfuscatedName() + ";")) {
				addField(fieldHooks[0].buildObf(fNode)); // getinventory
				break;
			}
		}
	}
}
