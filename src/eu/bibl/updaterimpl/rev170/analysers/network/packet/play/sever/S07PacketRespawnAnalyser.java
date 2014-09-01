package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S07PacketRespawnAnalyser extends PlayPacketAnalyser {
	
	public S07PacketRespawnAnalyser(ClassContainer container, HookMap hookMap) {
		super("S07PacketRespawn", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getDimension", "I", "I"),
				new FieldMappingData("getDifficulty", "L" + MinecraftAnalyser.INTERFACES + "world/IDifficulty;"),
				new FieldMappingData("getGameMode", "L" + MinecraftAnalyser.INTERFACES + "world/IGameMode;"),
				new FieldMappingData("getWorldType", "L" + MinecraftAnalyser.INTERFACES + "world/IWorldType;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
	}
}
