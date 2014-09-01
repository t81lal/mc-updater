package eu.bibl.updaterimpl.rev170.analysers.item.container;
public class ContainerAnalyser extends Analyser {
	
	public ContainerAnalyser(ClassContainer container, HookMap hookMap) {
		super("Container", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getSlots", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldMappingData("getItemStacks", "Ljava/util/List;", "Ljava/util/List;") };
	}
	
	@Override
public boolean accept() {
		for(MethodNode mNode : methods(cn)) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst != null) {
						if (ldc.cst.toString().equals("Listener already listening"))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/container/IContainer"));
		
		// TODO:
		// Finds player inventory container and open container in entityplayer
		findEntityPlayerInventoryFields();
		findItemStackAndSizeFields();
	}
	
	private void findEntityPlayerInventoryFields() {
		EntityPlayerAnalyser analyser = (EntityPlayerAnalyser) analysers.get("EntityPlayer");
		ClassMappingData entityPlayerHook = hookMap.getClassByRefactoredName("EntityPlayer");
		if (entityPlayerHook != null) {
			ClassNode entityPlayerNode = analysisMap.requestNode(entityPlayerHook.getObfuscatedName());
			ArrayList<FieldNode> containerFieldNodes = fields(entityPlayerNode, "L" + cn.name + ";");
			if (containerFieldNodes.size() == 2) {
				FieldNode inventoryContainerFieldNode = containerFieldNodes.get(0);
				FieldNode openContainerFieldNode = containerFieldNodes.get(1);
				analyser.addHook(analyser.getHooks()[1].buildObfFn(inventoryContainerFieldNode));
				analyser.addHook(analyser.getHooks()[2].buildObfFn(openContainerFieldNode));
			}
		}
	}
	
	private void findItemStackAndSizeFields() {
		slotClassFor: for(MethodNode mNode : methods(cn)) {
			if (!mNode.name.equals("<init>")) {
				Type returnType = Type.getReturnType(mNode.desc);
				String slotClassName = returnType.getClassName();
				hookMap.addClass(new ClassMappingData(slotClassName, "Slot"));
				
				ListIterator<?> it = mNode.instructions.iterator();
				int fieldInsns = 0;
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof FieldInsnNode) {
						fieldInsns++;
						if (fieldInsns == 3) {
							FieldInsnNode fin = (FieldInsnNode) ain;
							addFieldHook(fieldHooks[0].buildObfFin(fin));
						} else
							if (fieldInsns == 4) {
								FieldInsnNode fin = (FieldInsnNode) ain;
								addFieldHook(fieldHooks[1].buildObfFin(fin));
								break slotClassFor;
							}
					}
				}
				break;
			}
		}
	}
}
