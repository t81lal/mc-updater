package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S35PacketUpdateTileEntityAnalyser extends PlayPacketAnalyser {
	
	public S35PacketUpdateTileEntityAnalyser(ClassContainer container, HookMap hookMap) {
		super("S35PacketUpdateTileEntity", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getAction", "I", "I"),
				new FieldMappingData("getNBTData", "L" + MinecraftAnalyser.INTERFACES + "nbt/tags/INBTCompound;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
	}
}
