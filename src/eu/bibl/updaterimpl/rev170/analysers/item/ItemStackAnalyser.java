package eu.bibl.updaterimpl.rev170.analysers.item;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class ItemStackAnalyser extends Analyser {
	
	public ItemStackAnalyser() {
		super("ItemStack");
		hooks = new FieldHook[] {
				new FieldHook("getStackSize", "I", "I"),
				new FieldHook("getItem", "L" + INTERFACES + "item/IItem;"),
		// new FieldHook("getDamage", "I", "I")
		};
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null)
			return c.getRefactoredName().equals("ItemStack");
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "item/IItemStack"));
		
		// TODO:
		// Finds item damage, stack size and item.
		MethodNode biggestInit = null;
		for(MethodNode mNode : methods(cn)) {
			if (mNode.name.equals("<init>")) {
				if (biggestInit == null)
					biggestInit = mNode;
				else
					if (mNode.instructions.size() >= biggestInit.instructions.size())
						biggestInit = mNode;
			}
		}
		AbstractInsnNode firstAin = biggestInit.instructions.getFirst();
		FieldInsnNode itemFin = getNextFin(firstAin, PUTFIELD);
		FieldInsnNode stackSizeFin = getNextFin(itemFin, PUTFIELD);
		addHook(hooks[0].buildObfFin(stackSizeFin)); // getstacksize
		addHook(hooks[1].buildObfFin(itemFin));
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