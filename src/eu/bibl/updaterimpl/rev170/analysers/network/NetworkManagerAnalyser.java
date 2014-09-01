package eu.bibl.updaterimpl.rev170.analysers.network;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NetworkManagerAnalyser extends Analyser {
	
	public NetworkManagerAnalyser(ClassContainer container, HookMap hookMap) {
		super("NetworkManager", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return cn.superName.equals("io/netty/channel/SimpleChannelInboundHandler") && InsnUtil.containsLdc(cn, "NETWORK_PACKETS");
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/INetworkManager");
	}
}
