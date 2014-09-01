package eu.bibl.updaterimpl.rev170.analysers.network;
public class NetworkManagerAnalyser extends Analyser {
	
	public NetworkManagerAnalyser(ClassContainer container, HookMap hookMap) {
		super("NetworkManager", container, hookMap);
	}
	
	@Override
public boolean accept() {
		return cn.superName.equals("io/netty/channel/SimpleChannelInboundHandler") && containsLdc(cn, "NETWORK_PACKETS");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/INetworkManager"));
	}
}
