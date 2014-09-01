package eu.bibl.updaterimpl.rev170.analysers.item.container;
public class PlayerInventoryAnalyser extends Analyser {
	
	public PlayerInventoryAnalyser(ClassContainer container, HookMap hookMap) {
		super("PlayerInventory", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getOwningPlayer", "L" + MinecraftAnalyser.INTERFACES + "entity/IEntityPlayer;"),
				new FieldMappingData("getInventoryItems", "[L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;"),
				new FieldMappingData("getArmourItems", "[L" + MinecraftAnalyser.INTERFACES + "item/IItemStack;"),
				new FieldMappingData("getCurrentSlot", "I", "I") };
	}
	
	@Override
public boolean accept() {
		for(MethodNode mNode : methods(cn)) {
			if (mNode.desc.startsWith("(L") && mNode.desc.endsWith(";)Z")) {
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst.toString().equals("Item being added"))
								return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "item/container/IPlayerInventory"));
		
		findItemItemStackAndGetItemIDAndDamage();
		findInventoryArmourItemsAndOwningPlayer();
		findCurrentSlot();
		findPlayerInventory();
	}
	
	private void findInventoryArmourItemsAndOwningPlayer() {
		inventoryFor: for(MethodNode m : methods(cn)) {
			if (m.name.equals("<init>")) {
				InsnList list = m.instructions;
				AbstractInsnNode tempAin = list.getLast();
				while (tempAin.getOpcode() != PUTFIELD) {
					if (tempAin.getPrevious() == null)
						break;
					tempAin = tempAin.getPrevious();
					if (tempAin.getOpcode() == PUTFIELD)
						break;
				}
				if (tempAin.getOpcode() == PUTFIELD) {
					FieldInsnNode playerFin = (FieldInsnNode) tempAin;
					addFieldHook(fieldHooks[0].buildObfFin(playerFin));// getowningplayer
				}
				boolean foundMainInventory = false;
				boolean foundArmourInventory = false;
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == BIPUSH) {
						IntInsnNode iin = (IntInsnNode) ain;
						if (iin.operand == 36) {
							FieldInsnNode fin = (FieldInsnNode) ain.getNext().getNext();
							addFieldHook(fieldHooks[1].buildObfFin(fin));// getinventoryitems
							foundMainInventory = true;
						}
					} else
						if (ain.getOpcode() == ICONST_4) {
							FieldInsnNode fin = (FieldInsnNode) ain.getNext().getNext();
							addFieldHook(fieldHooks[2].buildObfFin(fin));// getarmouritems
						}
					if (foundMainInventory && foundArmourInventory)
						break inventoryFor;
				}
			}
		}
	}
	
	private void findItemItemStackAndGetItemIDAndDamage() {
		ItemStackAnalyser analyser = (ItemStackAnalyser) analysers.get("ItemStack");
		for(MethodNode mNode : methods(cn)) {
			if (mNode.desc.startsWith("(L") && mNode.desc.endsWith(";)Z")) {
				boolean foundItemStackClass = false;
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (!foundItemStackClass) {
						if (ain instanceof LdcInsnNode) {
							LdcInsnNode ldc = (LdcInsnNode) ain;
							if (ldc.cst != null) {
								if (ldc.cst.toString().equals("Item being added")) {
									Type[] parameters = Type.getArgumentTypes(mNode.desc);
									String itemStackClass = parameters[0].getInternalName().substring(0, parameters[0].getInternalName().length()).replace(";", "");
									hookMap.addClass(new ClassMappingData(itemStackClass, "ItemStack"));
									foundItemStackClass = true;
								}
							}
						}
					} else {
						if (ain instanceof LdcInsnNode) {
							LdcInsnNode ldc = (LdcInsnNode) ain;
							if (ldc.cst != null) {
								String strung = ldc.cst.toString();
								if (strung.equals("Item ID")) {
									MethodInsnNode localMin = (MethodInsnNode) ain.getNext().getNext();
									// addMethodHook(methodHooks[0].buildObfMin(localMin));//getitem
									Type returnType = Type.getReturnType(localMin.desc);
									String itemClass = returnType.getInternalName().substring(0, returnType.getInternalName().length()).replace(";", "");
									hookMap.addClass(new ClassMappingData(itemClass, "Item"));
									localMin = (MethodInsnNode) localMin.getNext();
									MinecraftAnalyser minecraftAnalyser = (MinecraftAnalyser) analysers.get("Minecraft");
									addMinecraftHook(minecraftAnalyser.getCallbackMappingDatas()[1].buildObfMin(localMin), hookMap.getClassByRefactoredName("Item").getObfuscatedName());
								} else
									if (strung.equals("Item data")) {
										MethodInsnNode localMin = (MethodInsnNode) ain.getNext().getNext();
										// addMethodHook(methodHooks[1].buildObfMin(localMin));//getdamage
									}
							}
						}
					}
				}
			}
		}
	}
	
	private void findPlayerInventory() {
		EntityPlayerAnalyser analyser = (EntityPlayerAnalyser) analysers.get("EntityPlayer");
		analyser.findPlayerInventory(classHook);
	}
	
	private void findCurrentSlot() {
		FieldNode currentSlotFieldNode = fields(cn, "I").get(0);
		addFieldHook(fieldHooks[3].buildObfFn(currentSlotFieldNode));
	}
}
