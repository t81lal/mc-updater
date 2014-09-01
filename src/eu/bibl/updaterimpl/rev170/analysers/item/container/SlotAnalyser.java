package eu.bibl.updaterimpl.rev170.analysers.item.container;

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

public class SlotAnalyser extends Analyser {
	
	public SlotAnalyser(ClassContainer container, HookMap hookMap) {
		super("Slot", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getIndex"), new MappingData("I", "I"), false),
				/* 1 */new FieldMappingData(new MappingData("getNumber"), new MappingData("I", "I"), false),
				/* 2 */new FieldMappingData(new MappingData("getXDisplayPos"), new MappingData("I", "I"), false),
				/* 3 */new FieldMappingData(new MappingData("getYDisplayPos"), new MappingData("I", "I"), false), };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData owner = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if (owner == null)
			return false;
		return owner.getRefactoredName().equals("Slot");
	}
	
	@Override
	public InterfaceMappingData run() {
		findAll();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/container/ISlot");
	}
	
	private void findAll() {
		List<FieldNode> intFieldNodes = InsnUtil.fields(cn, "I");
		if (intFieldNodes.size() == 4) {
			FieldNode indexFieldNode = intFieldNodes.get(0);
			FieldNode numberFieldNode = intFieldNodes.get(1);
			FieldNode xDisplayPosFieldNode = intFieldNodes.get(2);
			FieldNode yDisplayPosFieldNode = intFieldNodes.get(3);
			addField(fieldHooks[0].buildObf(indexFieldNode));
			addField(fieldHooks[1].buildObf(numberFieldNode));
			addField(fieldHooks[2].buildObf(xDisplayPosFieldNode));
			addField(fieldHooks[3].buildObf(yDisplayPosFieldNode));
		}
	}
}
