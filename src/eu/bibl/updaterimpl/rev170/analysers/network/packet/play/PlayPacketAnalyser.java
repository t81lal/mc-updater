package eu.bibl.updaterimpl.rev170.analysers.network.packet.play;

import java.util.HashMap;

import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.PacketBaseAnalyser;

public abstract class PlayPacketAnalyser extends PacketBaseAnalyser {
	
	public static HashMap<Integer, String> realServerBoundPacketCache = new HashMap<Integer, String>();
	public static HashMap<Integer, String> realClientBoundPacketCache = new HashMap<Integer, String>();
	
	public static HashMap<Integer, String> serverBoundPacketCache = new HashMap<Integer, String>();
	public static HashMap<Integer, String> clientBoundPacketCache = new HashMap<Integer, String>();
	
	static {
		realClientBoundPacketCache.put(0, "S00PacketKeepAlive");
		realClientBoundPacketCache.put(1, "S01PacketJoinGame");
		realClientBoundPacketCache.put(2, "S02PacketChat");
		realClientBoundPacketCache.put(3, "S03PacketTimeUpdate");
		realClientBoundPacketCache.put(4, "S04PacketEntityEquipment");
		realClientBoundPacketCache.put(5, "S05PacketSpawnPosition");
		realClientBoundPacketCache.put(6, "S06PacketUpdateHealth");
		realClientBoundPacketCache.put(7, "S07PacketRespawn");
		realClientBoundPacketCache.put(8, "S08PacketPlayerPosLook");
		realClientBoundPacketCache.put(9, "S09PacketHeldItemChange");
		realClientBoundPacketCache.put(10, "S0APacketUseBed");
		realClientBoundPacketCache.put(11, "S0BPacketAnimation");
		realClientBoundPacketCache.put(12, "S0CPacketSpawnPlayer");
		realClientBoundPacketCache.put(13, "S0DPacketCollectItem");
		realClientBoundPacketCache.put(14, "S0EPacketSpawnObject");
		realClientBoundPacketCache.put(15, "S0FPacketSpawnMob");
		realClientBoundPacketCache.put(16, "S10PacketSpawnPainting");
		realClientBoundPacketCache.put(17, "S11PacketSpawnExperienceOrb");
		realClientBoundPacketCache.put(18, "S12PacketEntityVelocity");
		realClientBoundPacketCache.put(19, "S13PacketDestroyEntities");
		realClientBoundPacketCache.put(20, "S14PacketEntity");
		realClientBoundPacketCache.put(21, "S15PacketEntityRelMove");
		realClientBoundPacketCache.put(22, "S16PacketEntityLook");
		realClientBoundPacketCache.put(23, "S17PacketEntityLookMove");
		realClientBoundPacketCache.put(24, "S18PacketEntityTeleport");
		realClientBoundPacketCache.put(25, "S19PacketEntityHeadLook");
		realClientBoundPacketCache.put(26, "S1APacketEntityStatus");
		realClientBoundPacketCache.put(27, "S1BPacketEntityAttach");
		realClientBoundPacketCache.put(28, "S1CPacketEntityMetadata");
		realClientBoundPacketCache.put(29, "S1DPacketEntityEffect");
		realClientBoundPacketCache.put(30, "S1EPacketRemoveEntityEffect");
		realClientBoundPacketCache.put(31, "S1FPacketSetExperience");
		realClientBoundPacketCache.put(32, "S20PacketEntityProperties");
		realClientBoundPacketCache.put(33, "S21PacketChunkData");
		realClientBoundPacketCache.put(34, "S22PacketMultiBlockChange");
		realClientBoundPacketCache.put(35, "S23PacketBlockChange");
		realClientBoundPacketCache.put(36, "S24PacketBlockAction");
		realClientBoundPacketCache.put(37, "S25PacketBlockBreakAnim");
		realClientBoundPacketCache.put(38, "S26PacketMapChunkBulk");
		realClientBoundPacketCache.put(39, "S27PacketExplosion");
		realClientBoundPacketCache.put(40, "S28PacketEffect");
		realClientBoundPacketCache.put(41, "S29PacketSoundEffect");
		realClientBoundPacketCache.put(42, "S2APacketParticles");
		realClientBoundPacketCache.put(43, "S2BPacketChangeGameState");
		realClientBoundPacketCache.put(44, "S2CPacketSpawnGlobalEntity");
		realClientBoundPacketCache.put(45, "S2DPacketOpenWindow");
		realClientBoundPacketCache.put(46, "S2EPacketCloseWindow");
		realClientBoundPacketCache.put(47, "S2FPacketSetSlot");
		realClientBoundPacketCache.put(48, "S30PacketWindowItems");
		realClientBoundPacketCache.put(49, "S31PacketWindowProperty");
		realClientBoundPacketCache.put(50, "S32PacketConfirmTransaction");
		realClientBoundPacketCache.put(51, "S33PacketUpdateSign");
		realClientBoundPacketCache.put(52, "S34PacketMaps");
		realClientBoundPacketCache.put(53, "S35PacketUpdateTileEntity");
		realClientBoundPacketCache.put(54, "S36PacketSignEditorOpen");
		realClientBoundPacketCache.put(55, "S37PacketStatistics");
		realClientBoundPacketCache.put(56, "S38PacketPlayerListItem");
		realClientBoundPacketCache.put(57, "S39PacketPlayerAbilities");
		realClientBoundPacketCache.put(58, "S3APacketTabComplete");
		realClientBoundPacketCache.put(59, "S3BPacketScoreboardObjective");
		realClientBoundPacketCache.put(60, "S3CPacketUpdateScore");
		realClientBoundPacketCache.put(61, "S3DPacketDisplayScoreboard");
		realClientBoundPacketCache.put(62, "S3EPacketTeams");
		realClientBoundPacketCache.put(63, "S3FPacketCustomPayload");
		realClientBoundPacketCache.put(64, "S40PacketDisconnect");
		realServerBoundPacketCache.put(0, "C00PacketKeepAlive");
		realServerBoundPacketCache.put(1, "C01PacketChatMessage");
		realServerBoundPacketCache.put(2, "C02PacketUseEntity");
		realServerBoundPacketCache.put(3, "C03PacketPlayer");
		realServerBoundPacketCache.put(4, "C04PacketPlayerPosition");
		realServerBoundPacketCache.put(5, "C05PacketPlayerLook");
		realServerBoundPacketCache.put(6, "C06PacketPlayerPosLook");
		realServerBoundPacketCache.put(7, "C07PacketPlayerDigging");
		realServerBoundPacketCache.put(8, "C08PacketPlayerBlockPlacement");
		realServerBoundPacketCache.put(9, "C09PacketHeldItemChange");
		realServerBoundPacketCache.put(10, "C0APacketAnimation");
		realServerBoundPacketCache.put(11, "C0BPacketEntityAction");
		realServerBoundPacketCache.put(12, "C0CPacketInput");
		realServerBoundPacketCache.put(13, "C0DPacketCloseWindow");
		realServerBoundPacketCache.put(14, "C0EPacketClickWindow");
		realServerBoundPacketCache.put(15, "C0FPacketConfirmTransaction");
		realServerBoundPacketCache.put(16, "C10PacketCreativeInventoryAction");
		realServerBoundPacketCache.put(17, "C11PacketEnchantItem");
		realServerBoundPacketCache.put(18, "C12PacketUpdateSign");
		realServerBoundPacketCache.put(19, "C13PacketPlayerAbilities");
		realServerBoundPacketCache.put(20, "C14PacketTabComplete");
		realServerBoundPacketCache.put(21, "C15PacketClientSettings");
		realServerBoundPacketCache.put(22, "C16PacketClientStatus");
		realServerBoundPacketCache.put(23, "C17PacketCustomPayload");
	}
	
	public PlayPacketAnalyser(String name, ClassContainer container, HookMap hookMap) {
		super(name, container, hookMap);
	}
	
	@Override
	public boolean accept() {
		boolean b = hookMap.getClassByRefactoredName("Packet").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		return hookMap.getClassByRefactoredName(name).getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		String pck = realServerBoundPacketCache.containsValue(name) ? "client" : "server";
		run1();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/play/" + pck + "/I" + name);
	}
	
	public abstract void run1();
	
	@Override
	public boolean accept1() {
		return true;
	}
}
