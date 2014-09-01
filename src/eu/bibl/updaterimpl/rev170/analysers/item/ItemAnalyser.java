package eu.bibl.updaterimpl.rev170.analysers.item;
public class ItemAnalyser extends Analyser {
	
	public ItemAnalyser(ClassContainer container, HookMap hookMap) {
		super("Item", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getPotionEffect", "Ljava/lang/String;", "Ljava/lang/String;"), new FieldMappingData("getName", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null)
			return c.getRefactoredName().equals("Item");
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/IItem"));
		
		ArrayList<FieldNode> stringFieldNodes = fields(cn, "Ljava/lang/String;");
		FieldNode potionEffectFieldNode = stringFieldNodes.get(0);
		FieldNode nameFieldNode = stringFieldNodes.get(1);
		addFieldHook(fieldHooks[0].buildObfFn(potionEffectFieldNode));
		addFieldHook(fieldHooks[1].buildObfFn(nameFieldNode));
	}
}
