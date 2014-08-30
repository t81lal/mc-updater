package eu.bibl.updater;

import eu.bibl.updater.loader.Loader;

public class Updater {
	
	public Updater() {
		Loader loader = new Loader(179);
		loader.load();
		loader.run();
	}
	
	public static void main(String[] args) {
		new Updater();
	}
}