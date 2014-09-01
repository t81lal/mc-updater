package eu.bibl.updaterimpl.rev170.analysers.entity;
public class EntityPlayerSPAnalyser extends Analyser {
	
	private static final int[] ENTITY_ACTION_STATE_REGEX = new int[] {
			ALOAD,
			ALOAD,
			GETFIELD,
			GETFIELD,
			PUTFIELD };
	
	public EntityPlayerSPAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityPlayerSP", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getMovementStafe", "F", "F"),
				new FieldMappingData("getMovementForward", "F", "F"),
				new FieldMappingData("isJumping", "Z", "Z"),
				new FieldMappingData("getRenderArmPitch", "F", "F"),
				new FieldMappingData("getRenderArmYaw", "F", "F"), };
		methodHooks = new CallbackMappingData[] { new BytecodeCallbackMappingData(null, "addChatMessage", "(Ljava/lang/String;)V") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityPlayerSP"))
			return true;
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityPlayerSP", MinecraftAnalyser.INTERFACES + "entity/IAbstractClientPlayer"));
		hookMap.addClass(new ClassMappingData(cn.superName, "AbstractClientPlayer"));
		
		hookAddChatMessage(hookMap.getClassByRefactoredName("GuiIngame"));
		
		findMovementInputFields();
	}
	
	private void findMovementInputFields() {
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, ENTITY_ACTION_STATE_REGEX);
			if (is.match() && is.size() == 3) {
				ClassMappingData movementInput = new ClassMappingData(Type.getType(((FieldInsnNode) is.getMatches().get(0)[2]).desc).getClassName(), "MovementInput");
				hookMap.addClass(movementInput);
				MovementInputAnalyser analyser = (MovementInputAnalyser) analysers.get("MovementInput");
				for(int i = 0; i < is.size(); i++) {
					FieldInsnNode movementFin = (FieldInsnNode) is.getMatches().get(i)[3];
					hookMap.addFieldMappingData(analyser.getHooks()[i].buildObfFin(movementFin).buildOwner(movementInput).identify());
					
					FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(i)[4];
					addFieldHook(fieldHooks[i].buildObfFin(fin));
				}
				
				int found = 0;
				
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == D2F) {
						FieldInsnNode fin = (FieldInsnNode) ain.getNext();
						found++;
						if (found == 1) {
							addFieldHook(fieldHooks[3].buildObfFin(fin));
						} else
							if (found == 2) {
								addFieldHook(fieldHooks[4].buildObfFin(fin));
							} else {
								break;
							}
					}
				}
				
				break;
			}
		}
	}
	
	private void hookAddChatMessage(ClassMappingData guiIngameHook) {
		addChatMessageFor: for(MethodNode mNode : methods(cn)) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof FieldInsnNode) {
					FieldInsnNode fin = (FieldInsnNode) ain;
					if (fin.desc.equals("L" + guiIngameHook.getObfuscatedName() + ";")) {
						String paramClassName = hookMap.getClassByRefactoredName("TranslatedChatComponent").getObfuscatedName();
						InsnList addChatList = new InsnList();
						addChatList.add(new VarInsnNode(ALOAD, 0));
						addChatList.add(new TypeInsnNode(NEW, paramClassName));
						addChatList.add(new InsnNode(DUP));
						addChatList.add(new VarInsnNode(ALOAD, 1));
						addChatList.add(new InsnNode(ICONST_0));
						addChatList.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
						addChatList.add(new MethodInsnNode(INVOKESPECIAL, paramClassName, "<init>", "(Ljava/lang/String;[Ljava/lang/Object;)V"));
						addChatList.add(new MethodInsnNode(INVOKEVIRTUAL, cn.name, mNode.name, mNode.desc));
						addChatList.add(new InsnNode(RETURN));
						BytecodeCallbackMappingData addChatMessageHook = (BytecodeCallbackMappingData) methodHooks[0];
						addChatMessageHook.setInstructions(addChatList);
						addChatMessageHook.buildObfMn(mNode);
						addHook(addChatMessageHook);
						break addChatMessageFor;
					}
				}
			}
		}
	}
}
