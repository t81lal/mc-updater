package eu.bibl.updaterimpl.rev170.analysers.system;
public class ShutdownHookAnalyser extends Analyser {
	
	public ShutdownHookAnalyser(ClassContainer container, HookMap hookMap) {
		super("ShutdownHook", container, hookMap);
	}
	
	@Override
public boolean accept() {
		boolean b = cn.superName.equals("java/lang/Thread");
		if (!b)
			return false;
		for(MethodNode m : methods(cn)) {
			if (m.instructions.size() != 2)
				continue;
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain.getOpcode() == INVOKESTATIC) {
					MethodInsnNode min = (MethodInsnNode) ain;
					if (min.owner.equals(hookMap.getClassByRefactoredName("Minecraft").getObfuscatedName()))
						return true;
				}
			}
		}
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "system/ShutdownHook"));
		
		for(MethodNode m : methods(cn)) {
			if (!m.name.equals("<init>") && !m.desc.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == INVOKESTATIC) {
						MethodInsnNode min = (MethodInsnNode) ain;
						MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
						analyser.addMinecraftHook(analyser.getCallbackMappingDatas()[2].buildObfMin(min));
					}
				}
			}
		}
	}
}
