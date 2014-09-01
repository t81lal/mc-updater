package eu.bibl.updaterimpl.rev170.analysers.world;
public class WorldTypeAnalyser extends Analyser{
	public WorldTypeAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldType", container, hookMap);
		fieldHooks = new FieldMappingData[]{
			//mc hooks
			//new FieldMappingData("getWorldTypes", "[L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
			//new FieldMappingData("getDefaultWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
			//new FieldMappingData("getFlatWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
			//new FieldMappingData("getLargeBiomesWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
			//new FieldMappingData("getAmplifiedWorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
			//new FieldMappingData("getDefault1_1WorldType", "L" + MinecraftAnalyser.INTERFACES + "IWorldType;"),
			//instance
			new FieldMappingData("getWorldTypeID", "I", "I"),
			new FieldMappingData("getWorldType", "Ljava/lang/String;", "Ljava/lang/String;"),
			new FieldMappingData("getGeneratorVersion", "I", "I"),
			new FieldMappingData("canBeGenerated", "Z", "Z")
		};
	}
	@Override
public boolean accept() {
		return containsLdc(cn, "default_1_1");
	}
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorldType"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[2].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
		
		for(MethodNode m : methods(cn)){
			if(m.name.equals("<clinit>")){
				ListIterator<?> it = m.instructions.iterator();
				while(it.hasNext()){
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if(ain instanceof LdcInsnNode){
						LdcInsnNode ldc = (LdcInsnNode) ain;
						FieldInsnNode fin = (FieldInsnNode) getNext(ldc, PUTSTATIC);
						String cst = (String) ldc.cst;
						if(cst.equals("default")){
							addMinecraftHook(analyser.getHooks()[3].buildObfFin(fin));
						}else if(cst.equals("flat")){
							addMinecraftHook(analyser.getHooks()[4].buildObfFin(fin));
						}else if(cst.equals("largeBiomes")){
							addMinecraftHook(analyser.getHooks()[5].buildObfFin(fin));
						}else if(cst.equals("amplified")){
							addMinecraftHook(analyser.getHooks()[6].buildObfFin(fin));
						}else if(cst.equals("default_1_1")){
							addMinecraftHook(analyser.getHooks()[7].buildObfFin(fin));
						}
					}
				}
			}else if(m.name.equals("<init>")){
				FieldInsnNode worldType = (FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD);
				if(worldType != null){
					addFieldHook(fieldHooks[0].buildObfFin(worldType));
					FieldInsnNode generatorversion = (FieldInsnNode) getNext(worldType.getNext(), PUTFIELD);
					addFieldHook(fieldHooks[1].buildObfFin(generatorversion));
					FieldInsnNode canbegenerated = (FieldInsnNode) getNext(generatorversion.getNext(), PUTFIELD);
					addFieldHook(fieldHooks[2].buildObfFin(canbegenerated));
					FieldInsnNode worldtypeid = (FieldInsnNode) getNext(canbegenerated.getNext(), PUTFIELD);
					addFieldHook(fieldHooks[3].buildObfFin(worldtypeid));
				}
			}
		}
	}
}
