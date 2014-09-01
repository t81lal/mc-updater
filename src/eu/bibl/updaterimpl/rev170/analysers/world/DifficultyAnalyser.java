package eu.bibl.updaterimpl.rev170.analysers.world;
public class DifficultyAnalyser extends Analyser{
	public DifficultyAnalyser(ClassContainer container, HookMap hookMap) {
		super("Difficulty", container, hookMap);
	}
	@Override
public boolean accept() {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "options.difficulty.peaceful");
	}
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IDifficulty"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[10].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
	}
}
