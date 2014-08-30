package eu.bibl.updaterimpl.rev170.analysers.network.packet.login;

import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.PacketBaseAnalyser;

public abstract class LoginPacketAnalyser extends PacketBaseAnalyser {
	
	public static HashMap<Integer, String> realServerBoundPacketCache = new HashMap<Integer, String>();
	public static HashMap<Integer, String> realClientBoundPacketCache = new HashMap<Integer, String>();
	
	public static HashMap<Integer, String> serverBoundPacketCache = new HashMap<Integer, String>();
	public static HashMap<Integer, String> clientBoundPacketCache = new HashMap<Integer, String>();
	
	static {
		realClientBoundPacketCache.put(0, "S00PacketDisconnect");
		realClientBoundPacketCache.put(1, "S01PacketEncryptionRequest");
		realClientBoundPacketCache.put(2, "S02PacketLoginSuccess");
		realServerBoundPacketCache.put(0, "C00PacketLoginStart");
		realServerBoundPacketCache.put(1, "C01PacketEncryptionResponse");
	}
	
	public LoginPacketAnalyser(String name) {
		super(name);
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		boolean b = map.getClassByRefactoredName("Packet").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		ClassHook hook = map.getClassByRefactoredName(name);
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		String pck = realServerBoundPacketCache.containsValue(name) ? "client" : "server";
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/packet/login/" + pck + "/I" + name, INTERFACES + "network/packet/IPacket"));
		run1();
	}
	
	public abstract void run1();
	
	@Override
	public boolean accept1(ClassNode cn) {
		return true;
	}
}