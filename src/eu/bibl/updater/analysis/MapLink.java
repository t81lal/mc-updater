package eu.bibl.updater.analysis;

import eu.bibl.bytetools.jar.ClassParser;
import eu.bibl.bytetools.jar.JarDownloader;
import eu.bibl.bytetools.jar.JarType;

public class MapLink {

	static{
		ClassParser.addToClassLoader = false;
	}
	
	public static final String OUT_DIR = "res/";
	
	private JarDownloader jar;
	private int version;
	
	public MapLink(int version){
		this.version = version;
		jar = new JarDownloader(OUT_DIR + version + "/" + version + ".jar", JarType.FILE);
	}
	
	public MapLink(String jarLocation){
		jar = new JarDownloader(jarLocation, JarType.FILE);
	}

	public boolean successful(){
		return jar.wasSuccessful();
	}
	
	public JarDownloader getDownloader(){
		return jar;
	}
	
	public int getVersion(){
		return version;
	}
}