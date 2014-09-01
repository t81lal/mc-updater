package eu.bibl.updaterimpl.rev170.analysers.network.packet.status;

import java.util.HashMap;

import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;
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
	
	public StatusPacketAnalyser(String name, ClassContainer container, HookMap hookMap) {
		super(name, container, hookMap);
	}
	
	@Override
	public boolean accept() {
		boolean b = hookMap.getClassByRefactoredName("Packet").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		ClassMappingData hook = (ClassMappingData) hookMap.getClassByRefactoredName(name);
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		String pck = realServerBoundPacketCache.containsValue(name) ? "client" : "server";
		run1();
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/status/" + pck + "/I" + name);
	}
	
	public abstract void run1();
	
	@Override
	public boolean accept1() {
		return true;
	}
}
