package eu.bibl.updaterimpl.rev170;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updater.analysis.api.AnalysisProvider;
import eu.bibl.updater.analysis.api.ProviderInfo;
import eu.bibl.updater.util.HookMap;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.chat.ChatComponentAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.chat.ChatComponentStyleAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.chat.ChatVisibilityAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.chat.TranslatedChatComponentAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.client.ClientStateAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.client.GameRuleValueAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.client.GameRulesAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.client.MovementInputAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.client.profiler.ProfilerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.AbstractClientPlayerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.AxisAlignedBBAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.DataWatcherAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityClientPlayerMPAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityLivingBaseAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityPlayerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityPlayerSPAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.PlayerControllerMPAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.UseEntityActionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.combat.CombatEntryAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.combat.CombatTrackerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.combat.DamageSourceAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.item.ItemAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.item.ItemStackAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.item.container.ContainerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.item.container.PlayerInventoryAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.item.container.SlotAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.NBTBaseAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.NBTPrimitiveAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagByteAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagByteArrayAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagCompoundAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagDoubleAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagEndAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagFloatAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagIntAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagIntArrayAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagListAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagLongAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagShortAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.NBTTagStringAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.EnumConnectionStateAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.NetworkManagerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.PacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.PacketBufferAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.handshake.client.C00HandshakePacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.LoginPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.client.C00PacketLoginStartAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.client.C01PacketEncryptionResponseAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server.S00PacketDisconnectAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server.S01PacketEncryptionRequestAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server.S02PacketLoginSuccessAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.PlayPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C00PacketKeepAliveAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C01PacketChatMessageAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C02PacketUseEntityAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C03PacketPlayerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C04PacketPlayerPositionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C05PacketPlayerLookAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C06PacketPlayerPosLookAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C07PacketPlayerDiggingAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C08PacketPlayerBlockPlacementAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C09PacketHeldItemChangeAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C0APacketAnimationAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C0BPacketEntityActionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C0CPacketInputAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C0DPacketCloseWindowAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C0EPacketClickWindowAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C0FPacketConfirmTransactionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C10PacketCreativeInventoryActionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C11PacketEnchantItemAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C12PacketUpdateSignAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C13PacketPlayerAbilitiesAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C14PacketTabCompleteAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C15PacketClientSettingsAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C16PacketClientStatusAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.C17PacketCustomPayloadAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S00PacketKeepAliveAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S01PacketJoinGameAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S02PacketChatAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S03PacketTimeUpdateAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S04PacketEntityEquipmentAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S05PacketSpawnPositionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S06PacketUpdateHealthAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S07PacketRespawnAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S08PacketPlayerPosLookAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S09PacketHeldItemChangeAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S0APacketUseBedAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S0BPacketAnimationAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S0CPacketSpawnPlayerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S0DPacketCollectItemAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S0EPacketSpawnObjectAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S0FPacketSpawnMobAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S10PacketSpawnPaintingAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S11PacketSpawnExperienceOrbAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S12PacketEntityVelocityAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S13PacketDestroyEntitiesAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S14PacketEntityAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S15PacketEntityRelMoveAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S16PacketEntityLookAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S17PacketEntityLookMoveAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S18PacketEntityTeleportAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S19PacketEntityHeadLookAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S1APacketEntityStatusAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S1BPacketEntityAttachAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S1CPacketEntityMetadataAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S1DPacketEntityEffectAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S1EPacketRemoveEntityEffectAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S1FPacketSetExperienceAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S20PacketEntityPropertiesAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S21PacketChunkDataAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S22PacketMultiBlockChangeAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S23PacketBlockChangeAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S24PacketBlockActionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S25PacketBlockBreakAnimAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S26PacketMapChunkBulkAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S27PacketExplosionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S28PacketEffectAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S29PacketSoundEffectAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S2APacketParticlesAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S2BPacketChangeGameStateAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S2CPacketSpawnGlobalEntityAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S2DPacketOpenWindowAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S2EPacketCloseWindowAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S2FPacketSetSlotAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S30PacketWindowItemsAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S31PacketWindowPropertyAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S32PacketConfirmTransactionAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S33PacketUpdateSignAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S34PacketMapsAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S35PacketUpdateTileEntityAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S36PacketSignEditorOpenAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S37PacketStatisticsAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S38PacketPlayerListItemAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S39PacketPlayerAbilitiesAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S3APacketTabCompleteAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S3BPacketScoreboardObjectiveAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S3CPacketUpdateScoreAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S3DPacketDisplayScoreboardAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S3EPacketTeamsAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S3FPacketCustomPayloadAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.S40PacketDisconnectAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.StatusPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.client.C00PacketServerQueryAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.client.C01PacketPingAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever.S00PacketServerInfoAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever.S01PacketPongAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.system.ShutdownHookAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.ui.FontRendererAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.ui.GuiIngameAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.ui.GuiNewChatAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.ChunkCoordIntPairAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.DifficultyAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.GameModeAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.WorldAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.WorldClientAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.WorldInfoAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.WorldTypeAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.provider.WorldProviderAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.provider.WorldProviderEndAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.provider.WorldProviderHellAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.provider.WorldProviderOverworldAnalyser;

public class Revision170Provider implements AnalysisProvider, ProviderInfo {
	
	@Override
	public int[] getProviderGameRevisions() {
		return new int[] {
				170,
				172,
				173,
				174,
				175,
				178,
				179,
				1710 };
	}
	
	@Override
	public ProviderInfo retrieveProviderInfo() {
		return this;
	}
	
	@Override
	public List<Analyser> register() {
		StatusPacketAnalyser.clientBoundPacketCache.clear();
		StatusPacketAnalyser.serverBoundPacketCache.clear();
		LoginPacketAnalyser.clientBoundPacketCache.clear();
		LoginPacketAnalyser.serverBoundPacketCache.clear();
		PlayPacketAnalyser.clientBoundPacketCache.clear();
		PlayPacketAnalyser.serverBoundPacketCache.clear();
		Analyser.analysers = new HashMap<String, Analyser>();
		
		List<Analyser> analysers = new ArrayList<Analyser>();
		
		analysers.add(new MinecraftAnalyser());
		
		analysers.add(new NBTBaseAnalyser());
		analysers.add(new NBTTagEndAnalyser());
		analysers.add(new NBTTagByteAnalyser());
		analysers.add(new NBTTagByteArrayAnalyser());
		analysers.add(new NBTTagCompoundAnalyser());
		analysers.add(new NBTTagDoubleAnalyser());
		analysers.add(new NBTTagFloatAnalyser());
		analysers.add(new NBTTagIntAnalyser());
		analysers.add(new NBTTagIntArrayAnalyser());
		analysers.add(new NBTTagListAnalyser());
		analysers.add(new NBTTagLongAnalyser());
		analysers.add(new NBTTagShortAnalyser());
		analysers.add(new NBTTagStringAnalyser());
		analysers.add(new NBTPrimitiveAnalyser());
		
		analysers.add(new FontRendererAnalyser());
		analysers.add(new TranslatedChatComponentAnalyser());
		analysers.add(new ChatComponentStyleAnalyser());
		analysers.add(new ChatComponentAnalyser());
		analysers.add(new GuiIngameAnalyser());
		analysers.add(new GuiNewChatAnalyser());
		
		analysers.add(new WorldClientAnalyser());
		analysers.add(new WorldAnalyser());
		analysers.add(new WorldInfoAnalyser());
		analysers.add(new GameRulesAnalyser());
		analysers.add(new GameRuleValueAnalyser());
		analysers.add(new WorldTypeAnalyser());
		analysers.add(new GameModeAnalyser());
		analysers.add(new DifficultyAnalyser());
		
		analysers.add(new WorldProviderHellAnalyser());
		analysers.add(new WorldProviderAnalyser());
		analysers.add(new WorldProviderEndAnalyser());
		analysers.add(new WorldProviderOverworldAnalyser());
		
		analysers.add(new ProfilerAnalyser());
		
		analysers.add(new AxisAlignedBBAnalyser());
		analysers.add(new PlayerControllerMPAnalyser());
		analysers.add(new DataWatcherAnalyser());
		analysers.add(new EntityClientPlayerMPAnalyser());
		analysers.add(new EntityPlayerSPAnalyser());
		analysers.add(new AbstractClientPlayerAnalyser());
		analysers.add(new EntityPlayerAnalyser());
		analysers.add(new ChatVisibilityAnalyser());
		analysers.add(new EntityLivingBaseAnalyser());
		analysers.add(new DamageSourceAnalyser());
		analysers.add(new CombatTrackerAnalyser());
		analysers.add(new CombatEntryAnalyser());
		analysers.add(new EntityAnalyser());
		analysers.add(new MovementInputAnalyser());
		
		analysers.add(new PlayerInventoryAnalyser());
		analysers.add(new ContainerAnalyser());
		analysers.add(new ItemStackAnalyser());
		analysers.add(new ItemAnalyser());
		analysers.add(new SlotAnalyser());
		
		analysers.add(new EnumConnectionStateAnalyser());
		analysers.add(new PacketBufferAnalyser());
		analysers.add(new PacketAnalyser());
		analysers.add(new NetworkManagerAnalyser());
		// analysers.add(new NetHandlerLoginClientAnalyser());
		// analysers.add(new NetHandlerPlayClientAnalyser());
		// analysers.add(new INetHandlerLoginClientAnalyser());
		// analysers.add(new INetHandlerPlayClientAnalyser());
		
		// handshake - client
		analysers.add(new C00HandshakePacketAnalyser());
		
		// status - client
		analysers.add(new C00PacketServerQueryAnalyser());
		analysers.add(new C01PacketPingAnalyser());
		// status - server
		analysers.add(new S00PacketServerInfoAnalyser());
		analysers.add(new S01PacketPongAnalyser());
		
		// login - client
		analysers.add(new C00PacketLoginStartAnalyser());
		analysers.add(new C01PacketEncryptionResponseAnalyser());
		// login - sever
		analysers.add(new S00PacketDisconnectAnalyser());
		analysers.add(new S01PacketEncryptionRequestAnalyser());
		analysers.add(new S02PacketLoginSuccessAnalyser());
		
		// play - client
		analysers.add(new C00PacketKeepAliveAnalyser());
		analysers.add(new C01PacketChatMessageAnalyser());
		analysers.add(new UseEntityActionAnalyser());
		analysers.add(new C02PacketUseEntityAnalyser());
		analysers.add(new C03PacketPlayerAnalyser());
		analysers.add(new C04PacketPlayerPositionAnalyser());
		analysers.add(new C05PacketPlayerLookAnalyser());
		analysers.add(new C06PacketPlayerPosLookAnalyser());
		analysers.add(new C07PacketPlayerDiggingAnalyser());
		analysers.add(new C08PacketPlayerBlockPlacementAnalyser());
		analysers.add(new C09PacketHeldItemChangeAnalyser());
		analysers.add(new C0APacketAnimationAnalyser());
		analysers.add(new C0BPacketEntityActionAnalyser());
		analysers.add(new C0CPacketInputAnalyser());
		analysers.add(new C0DPacketCloseWindowAnalyser());
		analysers.add(new C0EPacketClickWindowAnalyser());
		analysers.add(new C0FPacketConfirmTransactionAnalyser());
		analysers.add(new C10PacketCreativeInventoryActionAnalyser());
		analysers.add(new C11PacketEnchantItemAnalyser());
		analysers.add(new C12PacketUpdateSignAnalyser());
		analysers.add(new C13PacketPlayerAbilitiesAnalyser());
		analysers.add(new C14PacketTabCompleteAnalyser());
		analysers.add(new ClientStateAnalyser());
		analysers.add(new C15PacketClientSettingsAnalyser());
		analysers.add(new C16PacketClientStatusAnalyser());
		analysers.add(new C17PacketCustomPayloadAnalyser());
		// play - server
		analysers.add(new S00PacketKeepAliveAnalyser());
		analysers.add(new S01PacketJoinGameAnalyser());
		analysers.add(new S02PacketChatAnalyser());
		analysers.add(new S03PacketTimeUpdateAnalyser());
		analysers.add(new S04PacketEntityEquipmentAnalyser());
		analysers.add(new S05PacketSpawnPositionAnalyser());
		analysers.add(new S06PacketUpdateHealthAnalyser());
		analysers.add(new S07PacketRespawnAnalyser());
		analysers.add(new S08PacketPlayerPosLookAnalyser());
		analysers.add(new S09PacketHeldItemChangeAnalyser());
		analysers.add(new S0APacketUseBedAnalyser());
		analysers.add(new S0BPacketAnimationAnalyser());
		analysers.add(new S0CPacketSpawnPlayerAnalyser());
		analysers.add(new S0DPacketCollectItemAnalyser());
		analysers.add(new S0EPacketSpawnObjectAnalyser());
		analysers.add(new S0FPacketSpawnMobAnalyser());
		analysers.add(new S10PacketSpawnPaintingAnalyser());
		analysers.add(new S11PacketSpawnExperienceOrbAnalyser());
		analysers.add(new S12PacketEntityVelocityAnalyser());
		analysers.add(new S13PacketDestroyEntitiesAnalyser());
		analysers.add(new S14PacketEntityAnalyser());
		analysers.add(new S15PacketEntityRelMoveAnalyser());
		analysers.add(new S16PacketEntityLookAnalyser());
		analysers.add(new S17PacketEntityLookMoveAnalyser());
		analysers.add(new S18PacketEntityTeleportAnalyser());
		analysers.add(new S19PacketEntityHeadLookAnalyser());
		analysers.add(new S1APacketEntityStatusAnalyser());
		analysers.add(new S1BPacketEntityAttachAnalyser());
		analysers.add(new S1CPacketEntityMetadataAnalyser());
		analysers.add(new S1DPacketEntityEffectAnalyser());
		analysers.add(new S1EPacketRemoveEntityEffectAnalyser());
		analysers.add(new S1FPacketSetExperienceAnalyser());
		analysers.add(new S20PacketEntityPropertiesAnalyser());
		analysers.add(new S21PacketChunkDataAnalyser());
		analysers.add(new S22PacketMultiBlockChangeAnalyser());
		analysers.add(new ChunkCoordIntPairAnalyser());
		analysers.add(new S23PacketBlockChangeAnalyser());
		analysers.add(new S24PacketBlockActionAnalyser());
		analysers.add(new S25PacketBlockBreakAnimAnalyser());
		analysers.add(new S26PacketMapChunkBulkAnalyser());
		analysers.add(new S27PacketExplosionAnalyser());
		analysers.add(new S28PacketEffectAnalyser());
		analysers.add(new S29PacketSoundEffectAnalyser());
		analysers.add(new S2APacketParticlesAnalyser());
		analysers.add(new S2BPacketChangeGameStateAnalyser());
		analysers.add(new S2CPacketSpawnGlobalEntityAnalyser());
		analysers.add(new S2DPacketOpenWindowAnalyser());
		analysers.add(new S2EPacketCloseWindowAnalyser());
		analysers.add(new S2FPacketSetSlotAnalyser());
		analysers.add(new S30PacketWindowItemsAnalyser());
		analysers.add(new S31PacketWindowPropertyAnalyser());
		analysers.add(new S32PacketConfirmTransactionAnalyser());
		analysers.add(new S33PacketUpdateSignAnalyser());
		analysers.add(new S34PacketMapsAnalyser());
		analysers.add(new S35PacketUpdateTileEntityAnalyser());
		analysers.add(new S36PacketSignEditorOpenAnalyser());
		analysers.add(new S37PacketStatisticsAnalyser());
		analysers.add(new S38PacketPlayerListItemAnalyser());
		analysers.add(new S39PacketPlayerAbilitiesAnalyser());
		analysers.add(new S3APacketTabCompleteAnalyser());
		analysers.add(new S3BPacketScoreboardObjectiveAnalyser());
		analysers.add(new S3CPacketUpdateScoreAnalyser());
		analysers.add(new S3DPacketDisplayScoreboardAnalyser());
		analysers.add(new S3FPacketCustomPayloadAnalyser());
		analysers.add(new S3EPacketTeamsAnalyser());
		analysers.add(new S3FPacketCustomPayloadAnalyser());
		analysers.add(new S40PacketDisconnectAnalyser());
		
		analysers.add(new ShutdownHookAnalyser());
		return analysers;
	}
	
	@Override
	public HookMap createMap() {
		return new HookMap();
	}
}