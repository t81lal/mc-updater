package eu.bibl.updaterimpl.rev170.analysers.world;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class DifficultyAnalyser extends Analyser{

	public DifficultyAnalyser() {
		super("Difficulty");
	}

	@Override
	public boolean accept(ClassNode cn) {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "options.difficulty.peaceful");
	}

	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/IDifficulty"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[10].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
	}
}