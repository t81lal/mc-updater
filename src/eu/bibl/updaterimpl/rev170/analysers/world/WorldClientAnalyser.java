package eu.bibl.updaterimpl.rev170.analysers.world;
public class WorldClientAnalyser extends Analyser {
	
	public WorldClientAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldClient", container, hookMap);
	}
	
	@Override
public boolean accept() {
		for(MethodNode mNode : methods(cn)) {
			if (!mNode.name.equals("<init>"))
				continue;
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst != null) {
						if (ldc.cst.toString().equals("MpServer")) {
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
		InterfaceHook mcInterfaceData = new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorldClient", MinecraftAnalyser.INTERFACES + "world/IWorld");
		classHook.setInterfaceHook(mcInterfaceData);
		
		hookMap.addClass(new ClassMappingData(cn.superName, "World"));
	}
}
