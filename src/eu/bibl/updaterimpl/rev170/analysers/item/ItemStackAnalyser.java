package eu.bibl.updaterimpl.rev170.analysers.item;
public class ItemStackAnalyser extends Analyser {
	
	public ItemStackAnalyser(ClassContainer container, HookMap hookMap) {
		super("ItemStack", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getStackSize", "I", "I"),
				new FieldMappingData("getItem", "L" + MinecraftAnalyser.INTERFACES + "item/IItem;"),
		// new FieldMappingData("getDamage", "I", "I")
		};
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null)
			return c.getRefactoredName().equals("ItemStack");
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/IItemStack"));
		
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
		addFieldHook(fieldHooks[0].buildObfFin(stackSizeFin)); // getstacksize
		addFieldHook(fieldHooks[1].buildObfFin(itemFin));
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
