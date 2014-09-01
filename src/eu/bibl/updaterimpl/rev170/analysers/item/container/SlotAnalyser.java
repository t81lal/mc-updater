package eu.bibl.updaterimpl.rev170.analysers.item.container;
public class SlotAnalyser extends Analyser {
	
	public SlotAnalyser(ClassContainer container, HookMap hookMap) {
		super("Slot", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getIndex", "I", "I"),
				new FieldMappingData("getNumber", "I", "I"),
				new FieldMappingData("getXDisplayPos", "I", "I"),
				new FieldMappingData("getYDisplayPos", "I", "I") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData owner = hookMap.getClassByObfuscatedName(cn.name);
		if (owner == null)
			return false;
		return owner.getRefactoredName().equals("Slot");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/container/ISlot"));
		
		findAll();
	}
	
	private void findAll() {
		ArrayList<FieldNode> intFieldNodes = fields(cn, "I");
		if (intFieldNodes.size() == 4) {
			FieldNode indexFieldNode = intFieldNodes.get(0);
			FieldNode numberFieldNode = intFieldNodes.get(1);
			FieldNode xDisplayPosFieldNode = intFieldNodes.get(2);
			FieldNode yDisplayPosFieldNode = intFieldNodes.get(3);
			addFieldHook(fieldHooks[0].buildObfFn(indexFieldNode));
			addFieldHook(fieldHooks[1].buildObfFn(numberFieldNode));
			addFieldHook(fieldHooks[2].buildObfFn(xDisplayPosFieldNode));
			addFieldHook(fieldHooks[3].buildObfFn(yDisplayPosFieldNode));
		}
	}
}
