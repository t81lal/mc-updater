package eu.bibl.updater;

import eu.bibl.bytetools.jar.ClassParser;
import eu.bibl.updater.loader.Loader;

public class Updater {
	
	public Updater() {
		Loader loader = new Loader();
		loader.load();
		loader.run();
	}
	
	public static void main(String[] args) {
		ClassParser.addToClassLoader = false;
		new Updater();
	}
}