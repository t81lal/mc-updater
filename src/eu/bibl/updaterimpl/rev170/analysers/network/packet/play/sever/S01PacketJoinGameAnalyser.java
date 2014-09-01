package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S01PacketJoinGameAnalyser extends PlayPacketAnalyser {
	
	public S01PacketJoinGameAnalyser(ClassContainer container, HookMap hookMap) {
		super("S01PacketJoinGame", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("isHardcore", "Z", "Z"),
				new FieldMappingData("getGameMode", "L" + MinecraftAnalyser.INTERFACES + "world/IGameMode;"),
				new FieldMappingData("getDifficulty", "L" + MinecraftAnalyser.INTERFACES + "world/IDifficulty;"),
				new FieldMappingData("getMaxPlayers", "I", "I"),
				new FieldMappingData("getWorldType", "L" + MinecraftAnalyser.INTERFACES + "world/IWorldType;"), };
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
