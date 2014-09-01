package eu.bibl.updaterimpl.rev170.analysers.world;
public class ChunkCoordIntPairAnalyser extends Analyser {
	
	public ChunkCoordIntPairAnalyser(ClassContainer container, HookMap hookMap) {
		super("ChunkCoordIntPair", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getChunkXPos", "I", "I"),
				new FieldMappingData("getChunkZPos", "I", "I") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByRefactoredName("ChunkCoordIntPair");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IChunkCoordIntPair"));
		for(MethodNode m : methods(cn, "(II)V")) {
			if (m.name.equals("<init>")) {
				FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
				for(int i = 0; i < hooks.length; i++) {
					addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
				}
				break;
			}
		}
	}
}
