package eu.bibl.updaterimpl.rev170.analysers.item;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ItemStackAnalyser extends Analyser {
	
	public ItemStackAnalyser(ClassContainer container, HookMap hookMap) {
		super("ItemStack", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getStackSize"), new MappingData("I", "I"), false),
				/* 1 */new FieldMappingData(new MappingData("getItem"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "item/IItem;"), false),
		/* 2 new FieldMappingData(new MappingData("getDamage"), new MappingData("I", "I"), false), */};
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if (c != null)
			return c.getRefactoredName().equals("ItemStack");
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		// TODO:
		// Find item damage, stack size and item.
		MethodNode biggestInit = null;
		for (MethodNode mNode : cn.methods()) {
			if (mNode.name.equals("<init>")) {
				if (biggestInit == null)
					biggestInit = mNode;
				else if (mNode.instructions.size() >= biggestInit.instructions.size())
					biggestInit = mNode;
			}
		}
		AbstractInsnNode firstAin = biggestInit.instructions.getFirst();
		FieldInsnNode itemFin = getNextFin(firstAin, PUTFIELD);
		FieldInsnNode stackSizeFin = getNextFin(itemFin, PUTFIELD);
		addField(fieldHooks[0].buildObf(stackSizeFin)); // getstacksize
		addField(fieldHooks[1].buildObf(itemFin));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/IItemStack");
	}
	
	public void findItem() {
		
	}
	
	public void findDamage() {
		
	}
	
	private FieldInsnNode getNextFin(AbstractInsnNode ain, int opcode) {
		AbstractInsnNode tempAin = ain.getNext();
		while (tempAin.getOpcode() != opcode) {
			if (tempAin.getNext() == null)
				break;
			if (tempAin.getOpcode() == opcode)
				break;
			tempAin = tempAin.getNext();
		}
		if (tempAin instanceof FieldInsnNode)
			return (FieldInsnNode) tempAin;
		return null;
	}
}
