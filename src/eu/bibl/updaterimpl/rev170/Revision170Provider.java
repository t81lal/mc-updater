package eu.bibl.updaterimpl.rev170;

import eu.bibl.banalysis.analyse.MassAnalyser;
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
import eu.bibl.updaterimpl.rev170.analysers.entity.*;
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
import eu.bibl.updaterimpl.rev170.analysers.nbt.tags.*;
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
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client.*;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever.*;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.StatusPacketAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.client.C00PacketServerQueryAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.client.C01PacketPingAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever.S00PacketServerInfoAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever.S01PacketPongAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.system.ShutdownHookAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.ui.FontRendererAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.ui.GuiIngameAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.ui.GuiNewChatAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.world.*;
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
	public MassAnalyser register() {
        if (cachedMassAnalyser != null)
            return cachedMassAnalyser;
        cachedMassAnalyser = new MassAnalyser();
        
		StatusPacketAnalyser.clientBoundPacketCache.clear();
		StatusPacketAnalyser.serverBoundPacketCache.clear();
		LoginPacketAnalyser.clientBoundPacketCache.clear();
		LoginPacketAnalyser.serverBoundPacketCache.clear();
		PlayPacketAnalyser.clientBoundPacketCache.clear();
		PlayPacketAnalyser.serverBoundPacketCache.clear();
        
		cachedMassAnalyser.addSingle(new MinecraftAnalyser());
		
		cachedMassAnalyser.addSingle(new NBTBaseAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagEndAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagByteAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagByteArrayAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagCompoundAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagDoubleAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagFloatAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagIntAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagIntArrayAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagListAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagLongAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagShortAnalyser());
		cachedMassAnalyser.addSingle(new NBTTagStringAnalyser());
		cachedMassAnalyser.addSingle(new NBTPrimitiveAnalyser());
		
		cachedMassAnalyser.addSingle(new FontRendererAnalyser());
		cachedMassAnalyser.addSingle(new TranslatedChatComponentAnalyser());
		cachedMassAnalyser.addSingle(new ChatComponentStyleAnalyser());
		cachedMassAnalyser.addSingle(new ChatComponentAnalyser());
		cachedMassAnalyser.addSingle(new GuiIngameAnalyser());
		cachedMassAnalyser.addSingle(new GuiNewChatAnalyser());
		
		cachedMassAnalyser.addSingle(new WorldClientAnalyser());
		cachedMassAnalyser.addSingle(new WorldAnalyser());
		cachedMassAnalyser.addSingle(new WorldInfoAnalyser());
		cachedMassAnalyser.addSingle(new GameRulesAnalyser());
		cachedMassAnalyser.addSingle(new GameRuleValueAnalyser());
		cachedMassAnalyser.addSingle(new WorldTypeAnalyser());
		cachedMassAnalyser.addSingle(new GameModeAnalyser());
		cachedMassAnalyser.addSingle(new DifficultyAnalyser());
		
		cachedMassAnalyser.addSingle(new WorldProviderHellAnalyser());
		cachedMassAnalyser.addSingle(new WorldProviderAnalyser());
		cachedMassAnalyser.addSingle(new WorldProviderEndAnalyser());
		cachedMassAnalyser.addSingle(new WorldProviderOverworldAnalyser());
		
		cachedMassAnalyser.addSingle(new ProfilerAnalyser());
		
		cachedMassAnalyser.addSingle(new AxisAlignedBBAnalyser());
		cachedMassAnalyser.addSingle(new PlayerControllerMPAnalyser());
		cachedMassAnalyser.addSingle(new DataWatcherAnalyser());
		cachedMassAnalyser.addSingle(new EntityClientPlayerMPAnalyser());
		cachedMassAnalyser.addSingle(new EntityPlayerSPAnalyser());
		cachedMassAnalyser.addSingle(new AbstractClientPlayerAnalyser());
		cachedMassAnalyser.addSingle(new EntityPlayerAnalyser());
		cachedMassAnalyser.addSingle(new ChatVisibilityAnalyser());
		cachedMassAnalyser.addSingle(new EntityLivingBaseAnalyser());
		cachedMassAnalyser.addSingle(new DamageSourceAnalyser());
		cachedMassAnalyser.addSingle(new CombatTrackerAnalyser());
		cachedMassAnalyser.addSingle(new CombatEntryAnalyser());
		cachedMassAnalyser.addSingle(new EntityAnalyser());
		cachedMassAnalyser.addSingle(new MovementInputAnalyser());
		
		cachedMassAnalyser.addSingle(new PlayerInventoryAnalyser());
		cachedMassAnalyser.addSingle(new ContainerAnalyser());
		cachedMassAnalyser.addSingle(new ItemStackAnalyser());
		cachedMassAnalyser.addSingle(new ItemAnalyser());
		cachedMassAnalyser.addSingle(new SlotAnalyser());
		
		cachedMassAnalyser.addSingle(new EnumConnectionStateAnalyser());
		cachedMassAnalyser.addSingle(new PacketBufferAnalyser());
		cachedMassAnalyser.addSingle(new PacketAnalyser());
		cachedMassAnalyser.addSingle(new NetworkManagerAnalyser());
		// cachedMassAnalyser.addSingle(new NetHandlerLoginClientAnalyser());
		// cachedMassAnalyser.addSingle(new NetHandlerPlayClientAnalyser());
		// cachedMassAnalyser.addSingle(new INetHandlerLoginClientAnalyser());
		// cachedMassAnalyser.addSingle(new INetHandlerPlayClientAnalyser());
		
		// handshake - client
		cachedMassAnalyser.addSingle(new C00HandshakePacketAnalyser());
		
		// status - client
		cachedMassAnalyser.addSingle(new C00PacketServerQueryAnalyser());
		cachedMassAnalyser.addSingle(new C01PacketPingAnalyser());
		// status - server
		cachedMassAnalyser.addSingle(new S00PacketServerInfoAnalyser());
		cachedMassAnalyser.addSingle(new S01PacketPongAnalyser());
		
		// login - client
		cachedMassAnalyser.addSingle(new C00PacketLoginStartAnalyser());
		cachedMassAnalyser.addSingle(new C01PacketEncryptionResponseAnalyser());
		// login - sever
		cachedMassAnalyser.addSingle(new S00PacketDisconnectAnalyser());
		cachedMassAnalyser.addSingle(new S01PacketEncryptionRequestAnalyser());
		cachedMassAnalyser.addSingle(new S02PacketLoginSuccessAnalyser());
		
		// play - client
		cachedMassAnalyser.addSingle(new C00PacketKeepAliveAnalyser());
		cachedMassAnalyser.addSingle(new C01PacketChatMessageAnalyser());
		cachedMassAnalyser.addSingle(new UseEntityActionAnalyser());
		cachedMassAnalyser.addSingle(new C02PacketUseEntityAnalyser());
		cachedMassAnalyser.addSingle(new C03PacketPlayerAnalyser());
		cachedMassAnalyser.addSingle(new C04PacketPlayerPositionAnalyser());
		cachedMassAnalyser.addSingle(new C05PacketPlayerLookAnalyser());
		cachedMassAnalyser.addSingle(new C06PacketPlayerPosLookAnalyser());
		cachedMassAnalyser.addSingle(new C07PacketPlayerDiggingAnalyser());
		cachedMassAnalyser.addSingle(new C08PacketPlayerBlockPlacementAnalyser());
		cachedMassAnalyser.addSingle(new C09PacketHeldItemChangeAnalyser());
		cachedMassAnalyser.addSingle(new C0APacketAnimationAnalyser());
		cachedMassAnalyser.addSingle(new C0BPacketEntityActionAnalyser());
		cachedMassAnalyser.addSingle(new C0CPacketInputAnalyser());
		cachedMassAnalyser.addSingle(new C0DPacketCloseWindowAnalyser());
		cachedMassAnalyser.addSingle(new C0EPacketClickWindowAnalyser());
		cachedMassAnalyser.addSingle(new C0FPacketConfirmTransactionAnalyser());
		cachedMassAnalyser.addSingle(new C10PacketCreativeInventoryActionAnalyser());
		cachedMassAnalyser.addSingle(new C11PacketEnchantItemAnalyser());
		cachedMassAnalyser.addSingle(new C12PacketUpdateSignAnalyser());
		cachedMassAnalyser.addSingle(new C13PacketPlayerAbilitiesAnalyser());
		cachedMassAnalyser.addSingle(new C14PacketTabCompleteAnalyser());
		cachedMassAnalyser.addSingle(new ClientStateAnalyser());
		cachedMassAnalyser.addSingle(new C15PacketClientSettingsAnalyser());
		cachedMassAnalyser.addSingle(new C16PacketClientStatusAnalyser());
		cachedMassAnalyser.addSingle(new C17PacketCustomPayloadAnalyser());
		// play - server
		cachedMassAnalyser.addSingle(new S00PacketKeepAliveAnalyser());
		cachedMassAnalyser.addSingle(new S01PacketJoinGameAnalyser());
		cachedMassAnalyser.addSingle(new S02PacketChatAnalyser());
		cachedMassAnalyser.addSingle(new S03PacketTimeUpdateAnalyser());
		cachedMassAnalyser.addSingle(new S04PacketEntityEquipmentAnalyser());
		cachedMassAnalyser.addSingle(new S05PacketSpawnPositionAnalyser());
		cachedMassAnalyser.addSingle(new S06PacketUpdateHealthAnalyser());
		cachedMassAnalyser.addSingle(new S07PacketRespawnAnalyser());
		cachedMassAnalyser.addSingle(new S08PacketPlayerPosLookAnalyser());
		cachedMassAnalyser.addSingle(new S09PacketHeldItemChangeAnalyser());
		cachedMassAnalyser.addSingle(new S0APacketUseBedAnalyser());
		cachedMassAnalyser.addSingle(new S0BPacketAnimationAnalyser());
		cachedMassAnalyser.addSingle(new S0CPacketSpawnPlayerAnalyser());
		cachedMassAnalyser.addSingle(new S0DPacketCollectItemAnalyser());
		cachedMassAnalyser.addSingle(new S0EPacketSpawnObjectAnalyser());
		cachedMassAnalyser.addSingle(new S0FPacketSpawnMobAnalyser());
		cachedMassAnalyser.addSingle(new S10PacketSpawnPaintingAnalyser());
		cachedMassAnalyser.addSingle(new S11PacketSpawnExperienceOrbAnalyser());
		cachedMassAnalyser.addSingle(new S12PacketEntityVelocityAnalyser());
		cachedMassAnalyser.addSingle(new S13PacketDestroyEntitiesAnalyser());
		cachedMassAnalyser.addSingle(new S14PacketEntityAnalyser());
		cachedMassAnalyser.addSingle(new S15PacketEntityRelMoveAnalyser());
		cachedMassAnalyser.addSingle(new S16PacketEntityLookAnalyser());
		cachedMassAnalyser.addSingle(new S17PacketEntityLookMoveAnalyser());
		cachedMassAnalyser.addSingle(new S18PacketEntityTeleportAnalyser());
		cachedMassAnalyser.addSingle(new S19PacketEntityHeadLookAnalyser());
		cachedMassAnalyser.addSingle(new S1APacketEntityStatusAnalyser());
		cachedMassAnalyser.addSingle(new S1BPacketEntityAttachAnalyser());
		cachedMassAnalyser.addSingle(new S1CPacketEntityMetadataAnalyser());
		cachedMassAnalyser.addSingle(new S1DPacketEntityEffectAnalyser());
		cachedMassAnalyser.addSingle(new S1EPacketRemoveEntityEffectAnalyser());
		cachedMassAnalyser.addSingle(new S1FPacketSetExperienceAnalyser());
		cachedMassAnalyser.addSingle(new S20PacketEntityPropertiesAnalyser());
		cachedMassAnalyser.addSingle(new S21PacketChunkDataAnalyser());
		cachedMassAnalyser.addSingle(new S22PacketMultiBlockChangeAnalyser());
		cachedMassAnalyser.addSingle(new ChunkCoordIntPairAnalyser());
		cachedMassAnalyser.addSingle(new S23PacketBlockChangeAnalyser());
		cachedMassAnalyser.addSingle(new S24PacketBlockActionAnalyser());
		cachedMassAnalyser.addSingle(new S25PacketBlockBreakAnimAnalyser());
		cachedMassAnalyser.addSingle(new S26PacketMapChunkBulkAnalyser());
		cachedMassAnalyser.addSingle(new S27PacketExplosionAnalyser());
		cachedMassAnalyser.addSingle(new S28PacketEffectAnalyser());
		cachedMassAnalyser.addSingle(new S29PacketSoundEffectAnalyser());
		cachedMassAnalyser.addSingle(new S2APacketParticlesAnalyser());
		cachedMassAnalyser.addSingle(new S2BPacketChangeGameStateAnalyser());
		cachedMassAnalyser.addSingle(new S2CPacketSpawnGlobalEntityAnalyser());
		cachedMassAnalyser.addSingle(new S2DPacketOpenWindowAnalyser());
		cachedMassAnalyser.addSingle(new S2EPacketCloseWindowAnalyser());
		cachedMassAnalyser.addSingle(new S2FPacketSetSlotAnalyser());
		cachedMassAnalyser.addSingle(new S30PacketWindowItemsAnalyser());
		cachedMassAnalyser.addSingle(new S31PacketWindowPropertyAnalyser());
		cachedMassAnalyser.addSingle(new S32PacketConfirmTransactionAnalyser());
		cachedMassAnalyser.addSingle(new S33PacketUpdateSignAnalyser());
		cachedMassAnalyser.addSingle(new S34PacketMapsAnalyser());
		cachedMassAnalyser.addSingle(new S35PacketUpdateTileEntityAnalyser());
		cachedMassAnalyser.addSingle(new S36PacketSignEditorOpenAnalyser());
		cachedMassAnalyser.addSingle(new S37PacketStatisticsAnalyser());
		cachedMassAnalyser.addSingle(new S38PacketPlayerListItemAnalyser());
		cachedMassAnalyser.addSingle(new S39PacketPlayerAbilitiesAnalyser());
		cachedMassAnalyser.addSingle(new S3APacketTabCompleteAnalyser());
		cachedMassAnalyser.addSingle(new S3BPacketScoreboardObjectiveAnalyser());
		cachedMassAnalyser.addSingle(new S3CPacketUpdateScoreAnalyser());
		cachedMassAnalyser.addSingle(new S3DPacketDisplayScoreboardAnalyser());
		cachedMassAnalyser.addSingle(new S3FPacketCustomPayloadAnalyser());
		cachedMassAnalyser.addSingle(new S3EPacketTeamsAnalyser());
		cachedMassAnalyser.addSingle(new S3FPacketCustomPayloadAnalyser());
		cachedMassAnalyser.addSingle(new S40PacketDisconnectAnalyser());
		
		cachedMassAnalyser.addSingle(new ShutdownHookAnalyser());
		return cachedMassAnalyser;
	}
}