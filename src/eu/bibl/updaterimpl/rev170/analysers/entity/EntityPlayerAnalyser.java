package eu.bibl.updaterimpl.rev170.analysers.entity;
public class EntityPlayerAnalyser extends Analyser {
	
	public EntityPlayerAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityPlayer", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getInventory", "L" + MinecraftAnalyser.INTERFACES + "item/container/IPlayerInventory;"),
				new FieldMappingData("getInventoryContainer", "L" + MinecraftAnalyser.INTERFACES + "item/container/IContainer;"),
				new FieldMappingData("getOpenContainer", "L" + MinecraftAnalyser.INTERFACES + "item/container/IContainer;"), };
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityPlayer"))
			return true;
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityPlayer", MinecraftAnalyser.INTERFACES + "entity/IEntityLivingBase"));
		hookMap.addClass(new ClassMappingData(cn.superName, "EntityLivingBase"));
	}
	
	// Called from PlayerInventoryAnalyser
	public void findPlayerInventory(ClassMappingData playerInventoryHook) {
		for(FieldNode fNode : fields(cn)) {
			if (fNode.desc.equals("L" + playerInventoryHook.getObfuscatedName() + ";")) {
				addFieldHook(fieldHooks[0].buildObfFn(fNode)); // getinventory
				break;
			}
		}
	}
}
