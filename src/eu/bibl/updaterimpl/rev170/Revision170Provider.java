package eu.bibl.updaterimpl.rev170;

import eu.bibl.banalysis.analyse.MassAnalyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.analysis.api.AnalysisProvider;
import eu.bibl.updater.analysis.api.ProviderInfo;
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
	
	private MassAnalyser cachedMassAnalyser;
	
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
	public MassAnalyser register(ClassContainer container, HookMap hookMap) {
		if (cachedMassAnalyser != null)
			return cachedMassAnalyser;
		cachedMassAnalyser = new MassAnalyser();
		
		StatusPacketAnalyser.clientBoundPacketCache.clear();
		StatusPacketAnalyser.serverBoundPacketCache.clear();
		LoginPacketAnalyser.clientBoundPacketCache.clear();
		LoginPacketAnalyser.serverBoundPacketCache.clear();
		PlayPacketAnalyser.clientBoundPacketCache.clear();
		PlayPacketAnalyser.serverBoundPacketCache.clear();
		
		cachedMassAnalyser.add(new MinecraftAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new NBTBaseAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagEndAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagByteAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagByteArrayAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagCompoundAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagDoubleAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagFloatAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagIntAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagIntArrayAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagListAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagLongAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagShortAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTTagStringAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NBTPrimitiveAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new FontRendererAnalyser(container, hookMap));
		cachedMassAnalyser.add(new TranslatedChatComponentAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ChatComponentStyleAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ChatComponentAnalyser(container, hookMap));
		cachedMassAnalyser.add(new GuiIngameAnalyser(container, hookMap));
		cachedMassAnalyser.add(new GuiNewChatAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new WorldClientAnalyser(container, hookMap));
		cachedMassAnalyser.add(new WorldAnalyser(container, hookMap));
		cachedMassAnalyser.add(new WorldInfoAnalyser(container, hookMap));
		cachedMassAnalyser.add(new GameRulesAnalyser(container, hookMap));
		cachedMassAnalyser.add(new GameRuleValueAnalyser(container, hookMap));
		cachedMassAnalyser.add(new WorldTypeAnalyser(container, hookMap));
		cachedMassAnalyser.add(new GameModeAnalyser(container, hookMap));
		cachedMassAnalyser.add(new DifficultyAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new WorldProviderHellAnalyser(container, hookMap));
		cachedMassAnalyser.add(new WorldProviderAnalyser(container, hookMap));
		cachedMassAnalyser.add(new WorldProviderEndAnalyser(container, hookMap));
		cachedMassAnalyser.add(new WorldProviderOverworldAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new ProfilerAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new AxisAlignedBBAnalyser(container, hookMap));
		cachedMassAnalyser.add(new PlayerControllerMPAnalyser(container, hookMap));
		cachedMassAnalyser.add(new DataWatcherAnalyser(container, hookMap));
		cachedMassAnalyser.add(new EntityClientPlayerMPAnalyser(container, hookMap));
		cachedMassAnalyser.add(new EntityPlayerSPAnalyser(container, hookMap));
		cachedMassAnalyser.add(new AbstractClientPlayerAnalyser(container, hookMap));
		cachedMassAnalyser.add(new EntityPlayerAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ChatVisibilityAnalyser(container, hookMap));
		cachedMassAnalyser.add(new EntityLivingBaseAnalyser(container, hookMap));
		cachedMassAnalyser.add(new DamageSourceAnalyser(container, hookMap));
		cachedMassAnalyser.add(new CombatTrackerAnalyser(container, hookMap));
		cachedMassAnalyser.add(new CombatEntryAnalyser(container, hookMap));
		cachedMassAnalyser.add(new EntityAnalyser(container, hookMap));
		cachedMassAnalyser.add(new MovementInputAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new PlayerInventoryAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ContainerAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ItemStackAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ItemAnalyser(container, hookMap));
		cachedMassAnalyser.add(new SlotAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new EnumConnectionStateAnalyser(container, hookMap));
		cachedMassAnalyser.add(new PacketBufferAnalyser(container, hookMap));
		cachedMassAnalyser.add(new PacketAnalyser(container, hookMap));
		cachedMassAnalyser.add(new NetworkManagerAnalyser(container, hookMap));
		// cachedMassAnalyser.add(new NetHandlerLoginClientAnalyser(container, hookMap));
		// cachedMassAnalyser.add(new NetHandlerPlayClientAnalyser(container, hookMap));
		// cachedMassAnalyser.add(new INetHandlerLoginClientAnalyser(container, hookMap));
		// cachedMassAnalyser.add(new INetHandlerPlayClientAnalyser(container, hookMap));
		
		// handshake - client
		cachedMassAnalyser.add(new C00HandshakePacketAnalyser(container, hookMap));
		
		// status - client
		cachedMassAnalyser.add(new C00PacketServerQueryAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C01PacketPingAnalyser(container, hookMap));
		// status - server
		cachedMassAnalyser.add(new S00PacketServerInfoAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S01PacketPongAnalyser(container, hookMap));
		
		// login - client
		cachedMassAnalyser.add(new C00PacketLoginStartAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C01PacketEncryptionResponseAnalyser(container, hookMap));
		// login - sever
		cachedMassAnalyser.add(new S00PacketDisconnectAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S01PacketEncryptionRequestAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S02PacketLoginSuccessAnalyser(container, hookMap));
		
		// play - client
		cachedMassAnalyser.add(new C00PacketKeepAliveAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C01PacketChatMessageAnalyser(container, hookMap));
		cachedMassAnalyser.add(new UseEntityActionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C02PacketUseEntityAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C03PacketPlayerAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C04PacketPlayerPositionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C05PacketPlayerLookAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C06PacketPlayerPosLookAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C07PacketPlayerDiggingAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C08PacketPlayerBlockPlacementAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C09PacketHeldItemChangeAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C0APacketAnimationAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C0BPacketEntityActionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C0CPacketInputAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C0DPacketCloseWindowAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C0EPacketClickWindowAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C0FPacketConfirmTransactionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C10PacketCreativeInventoryActionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C11PacketEnchantItemAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C12PacketUpdateSignAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C13PacketPlayerAbilitiesAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C14PacketTabCompleteAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ClientStateAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C15PacketClientSettingsAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C16PacketClientStatusAnalyser(container, hookMap));
		cachedMassAnalyser.add(new C17PacketCustomPayloadAnalyser(container, hookMap));
		// play - server
		cachedMassAnalyser.add(new S00PacketKeepAliveAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S01PacketJoinGameAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S02PacketChatAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S03PacketTimeUpdateAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S04PacketEntityEquipmentAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S05PacketSpawnPositionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S06PacketUpdateHealthAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S07PacketRespawnAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S08PacketPlayerPosLookAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S09PacketHeldItemChangeAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S0APacketUseBedAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S0BPacketAnimationAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S0CPacketSpawnPlayerAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S0DPacketCollectItemAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S0EPacketSpawnObjectAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S0FPacketSpawnMobAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S10PacketSpawnPaintingAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S11PacketSpawnExperienceOrbAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S12PacketEntityVelocityAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S13PacketDestroyEntitiesAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S14PacketEntityAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S15PacketEntityRelMoveAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S16PacketEntityLookAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S17PacketEntityLookMoveAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S18PacketEntityTeleportAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S19PacketEntityHeadLookAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S1APacketEntityStatusAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S1BPacketEntityAttachAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S1CPacketEntityMetadataAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S1DPacketEntityEffectAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S1EPacketRemoveEntityEffectAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S1FPacketSetExperienceAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S20PacketEntityPropertiesAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S21PacketChunkDataAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S22PacketMultiBlockChangeAnalyser(container, hookMap));
		cachedMassAnalyser.add(new ChunkCoordIntPairAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S23PacketBlockChangeAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S24PacketBlockActionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S25PacketBlockBreakAnimAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S26PacketMapChunkBulkAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S27PacketExplosionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S28PacketEffectAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S29PacketSoundEffectAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S2APacketParticlesAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S2BPacketChangeGameStateAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S2CPacketSpawnGlobalEntityAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S2DPacketOpenWindowAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S2EPacketCloseWindowAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S2FPacketSetSlotAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S30PacketWindowItemsAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S31PacketWindowPropertyAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S32PacketConfirmTransactionAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S33PacketUpdateSignAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S34PacketMapsAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S35PacketUpdateTileEntityAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S36PacketSignEditorOpenAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S37PacketStatisticsAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S38PacketPlayerListItemAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S39PacketPlayerAbilitiesAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S3APacketTabCompleteAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S3BPacketScoreboardObjectiveAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S3CPacketUpdateScoreAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S3DPacketDisplayScoreboardAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S3FPacketCustomPayloadAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S3EPacketTeamsAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S3FPacketCustomPayloadAnalyser(container, hookMap));
		cachedMassAnalyser.add(new S40PacketDisconnectAnalyser(container, hookMap));
		
		cachedMassAnalyser.add(new ShutdownHookAnalyser(container, hookMap));
		return cachedMassAnalyser;
	}
}