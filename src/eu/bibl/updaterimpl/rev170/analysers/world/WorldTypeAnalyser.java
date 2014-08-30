package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class WorldTypeAnalyser extends Analyser{

	public WorldTypeAnalyser() {
		super("WorldType");
		hooks = new FieldHook[]{
			//mc hooks
			//new FieldHook("getWorldTypes", "[L" + INTERFACES + "IWorldType;"),
			//new FieldHook("getDefaultWorldType", "L" + INTERFACES + "IWorldType;"),
			//new FieldHook("getFlatWorldType", "L" + INTERFACES + "IWorldType;"),
			//new FieldHook("getLargeBiomesWorldType", "L" + INTERFACES + "IWorldType;"),
			//new FieldHook("getAmplifiedWorldType", "L" + INTERFACES + "IWorldType;"),
			//new FieldHook("getDefault1_1WorldType", "L" + INTERFACES + "IWorldType;"),
			//instance
			new FieldHook("getWorldTypeID", "I", "I"),
			new FieldHook("getWorldType", "Ljava/lang/String;", "Ljava/lang/String;"),
			new FieldHook("getGeneratorVersion", "I", "I"),
			new FieldHook("canBeGenerated", "Z", "Z")
		};
	}

	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "default_1_1");
	}

	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/IWorldType"));
		
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
					addHook(hooks[0].buildObfFin(worldType));
					FieldInsnNode generatorversion = (FieldInsnNode) getNext(worldType.getNext(), PUTFIELD);
					addHook(hooks[1].buildObfFin(generatorversion));
					FieldInsnNode canbegenerated = (FieldInsnNode) getNext(generatorversion.getNext(), PUTFIELD);
					addHook(hooks[2].buildObfFin(canbegenerated));
					FieldInsnNode worldtypeid = (FieldInsnNode) getNext(canbegenerated.getNext(), PUTFIELD);
					addHook(hooks[3].buildObfFin(worldtypeid));
				}
			}
		}
	}
}