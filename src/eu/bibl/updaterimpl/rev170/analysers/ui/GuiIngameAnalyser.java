package eu.bibl.updaterimpl.rev170.analysers.ui;
public class GuiIngameAnalyser extends Analyser {
	
	public static final int[] GUI_NEW_CHAT_REGEX = new int[] { ALOAD, GETFIELD };
	
	public GuiIngameAnalyser(ClassContainer container, HookMap hookMap) {
		super("GuiIngame", container, hookMap);
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
						if (ldc.cst.toString().equals("Allocated memory: ")) {
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
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "ui/IGuiIngame"));
		
		for(MethodNode mNode : methods(cn)) {
			InsnSearcher searcher = new InsnSearcher(mNode.instructions, 0, GUI_NEW_CHAT_REGEX);
			if (searcher.match() && mNode.instructions.getLast().getOpcode() == ARETURN) {
				AbstractInsnNode[] ains = searcher.getMatches().get(0);
				FieldInsnNode fin = (FieldInsnNode) ains[1];
				if (fin.desc.startsWith("L") && fin.desc.endsWith(";")) {
					String guiNewChat = fin.desc.substring(1, fin.desc.length()).replace(";", "");
					ClassMappingData guinc = new ClassMappingData(guiNewChat, "GuiNewChat");
					hookMap.addClass(guinc);
					break;
				}
			}
		}
	}
}
