package eu.bibl.updaterimpl.rev170.analysers.network.packet.status;

import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updaterimpl.rev170.analysers.network.packet.PacketBaseAnalyser;

public abstract class StatusPacketAnalyser extends PacketBaseAnalyser {
	
	public static HashMap<Integer, String> realServerBoundPacketCache = new HashMap<Integer, String>();
	public static HashMap<Integer, String> realClientBoundPacketCache = new HashMap<Integer, String>();
	
	public static HashMap<Integer, String> serverBoundPacketCache = new HashMap<Integer, String>();
	public static HashMap<Integer, String> clientBoundPacketCache = new HashMap<Integer, String>();
	
	static {
		realServerBoundPacketCache.put(0, "C00PacketServerQuery");
		realServerBoundPacketCache.put(1, "C01PacketPing");
		realClientBoundPacketCache.put(0, "S00PacketServerInfo");
		realClientBoundPacketCache.put(1, "S01PacketPong");
	}
	
	public StatusPacketAnalyser(String name) {
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
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "network/packet/status/" + pck + "/I" + name, INTERFACES + "network/packet/IPacket"));
		run1();
	}
	
	public abstract void run1();
	
	@Override
	public boolean accept1(ClassNode cn) {
		return true;
	}
}