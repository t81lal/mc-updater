package eu.bibl.updaterimpl.rev170.analysers.chat;
public class TranslatedChatComponentAnalyser extends Analyser {
	
	public TranslatedChatComponentAnalyser(ClassContainer container, HookMap hookMap) {
		super("TranslatedChatComponent", container, hookMap);
	}
	
	@Override
public boolean accept() {
		for(MethodNode mNode : methods(cn)) {
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst != null && ldc.cst.toString().equals("tile.bed.occupied")) {
						AbstractInsnNode prevAin = ain.getPrevious();
						TypeInsnNode newInsn = (TypeInsnNode) prevAin.getPrevious();
						ClassMappingData tccOwner = new ClassMappingData(newInsn.desc, "TranslatedChatComponent");
						hookMap.addClass(tccOwner);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		// NOTE, DO NOT USE cn.name AS THIS IS BLOCKBED CLASS, NOT TCC!
		classHook = hookMap.getClassByRefactoredName("TranslatedChatComponent");
		InterfaceHook hook = new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "chat/ITranslatableChatComponent", MinecraftAnalyser.INTERFACES + "chat/IChatComponentStyle");
		classHook.setInterfaceHook(hook);
		
		cn = analysisMap.requestNode(classHook.getObfuscatedName());
		ClassMappingData chatStyle = new ClassMappingData(cn.superName, "ChatComponentStyle");
		hookMap.addClass(chatStyle);
	}
}
