package eu.bibl.updaterimpl.rev170.analysers.network;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class NetworkManagerAnalyser extends Analyser {
	
	public NetworkManagerAnalyser() {
		super("NetworkManager");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return cn.superName.equals("io/netty/channel/SimpleChannelInboundHandler") && containsLdc(cn, "NETWORK_PACKETS");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/INetworkManager"));
	}
}