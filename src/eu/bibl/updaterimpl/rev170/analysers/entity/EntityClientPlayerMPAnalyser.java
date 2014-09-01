package eu.bibl.updaterimpl.rev170.analysers.entity;
public class EntityClientPlayerMPAnalyser extends Analyser {
	
	private static final int[] SWING_ITEM_REGEX = new int[] {
			ALOAD,
			INVOKESPECIAL,
			ALOAD,
			GETFIELD,
			NEW,
			DUP,
			ALOAD,
			ICONST_1,
			INVOKESPECIAL,
			INVOKEVIRTUAL,
			RETURN };
	private static final int[] SEND_CHAT_MESSAGE_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			NEW,
			DUP,
			ALOAD,
			INVOKESPECIAL,
			INVOKEVIRTUAL };
	
	public EntityClientPlayerMPAnalyser(ClassContainer container, HookMap hookMap) {
		super("EntityClientPlayerMP", container, hookMap);
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData("swingItem", "()V", "()V"),
				new CallbackMappingData("sendChatMessage", "(Ljava/lang/String;)V") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData c = hookMap.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("EntityClientPlayerMP"))
			return true;
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IEntityClientPlayerMP", MinecraftAnalyser.INTERFACES + "entity/IEntityPlayerSP"));
		hookMap.addClass(new ClassMappingData(cn.superName, "EntityPlayerSP"));
		
		findCallbackMethods();
	}
	
	private void findCallbackMethods() {
		for(MethodNode mNode : methods(cn)) {
			if (containsRegex(mNode, SWING_ITEM_REGEX)) {
				addMethodHook(methodHooks[0].buildObfMn(mNode));
			} else
				if (containsRegex(mNode, SEND_CHAT_MESSAGE_REGEX)) {
					addMethodHook(methodHooks[1].buildObfMn(mNode));
				}
		}
	}
	
	private boolean containsRegex(MethodNode mNode, int[] opcodes) {
		int insnCount = 0;
		ListIterator<?> it = mNode.instructions.iterator();
		while (it.hasNext()) {
			if (insnCount > opcodes.length)
				return false;
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			int opcode = ain.getOpcode();
			int expectedOpcode = opcodes[insnCount];
			if (opcode != expectedOpcode) {
				return false;
			}
			if (insnCount == opcodes.length - 1) {
				return true;
			}
			insnCount++;
		}
		return false;
	}
}
