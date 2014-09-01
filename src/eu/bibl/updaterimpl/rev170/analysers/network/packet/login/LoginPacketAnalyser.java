package eu.bibl.updaterimpl.rev170.analysers.network.packet.login;
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
public boolean accept() {
		boolean b = hookMap.getClassByRefactoredName("Packet").getObfuscatedName().equals(cn.superName);
		if (!b)
			return false;
		ClassMappingData hook = hookMap.getClassByRefactoredName(name);
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		String pck = realServerBoundPacketCache.containsValue(name) ? "client" : "server";
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/login/" + pck + "/I" + name, MinecraftAnalyser.INTERFACES + "network/packet/IPacket"));
		run1();
	}
	
	public abstract void run1();
	
	@Override
	public boolean accept1(ClassNode cn) {
		return true;
	}
}
