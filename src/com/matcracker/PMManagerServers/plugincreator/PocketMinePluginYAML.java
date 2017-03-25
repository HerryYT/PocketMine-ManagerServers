package com.matcracker.PMManagerServers.plugincreator;

import java.io.File;

import com.matcracker.PMManagerServers.utility.Utility;

public class PocketMinePluginYAML {
	/**
	 * plugin.yml
	 */
	private String pluginName = "PluginByPM-MS";
	private String className = "Main";
	private String version = "1.0";
	private String author = "PocketMine-ManagerServers";
	private String apiVersion = "3.0.0";
	private String namespace = "src\\" + author;
	
	public PocketMinePluginYAML(){}
	
	public PocketMinePluginYAML(String pluginName, String className, String version, String author, String apiVersion, String namespace){
		setPluginName(pluginName);
		setClassName(className);
		setVersion(version);
		setAuthor(author);
		setAPIVersion(apiVersion);
		setNamespace(namespace);
	}
	
	public String getPluginName(){
		return pluginName;
	}
	
	public void setPluginName(String pluginName){
		this.pluginName = pluginName;
	}
	
	public String getClassName(){
		return className;
	}
	
	public void setClassName(String className){
		this.className = className;
	}
	
	public String getVersion(){
		return version;
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public String getAPIVersion(){
		return apiVersion;
	}
	
	public void setAPIVersion(String apiVersion){
		this.apiVersion = apiVersion;
	}
	
	public String getNamespace(){
		return namespace;
	}
	
	public void setNamespace(String namespace){
		this.namespace = namespace;
	}
	
	/**
	 * Get informations about plugin.yml
	 */
	public void requestYAMLData(){
		String pluginName = Utility.readString("Name of plugin: ", null);
		if(!pluginName.isEmpty()) this.pluginName = pluginName;
		
		String namespace = Utility.readString("Namespace of plugin: ", "[Example: src\\author\\pluginname]");
		if(!namespace.isEmpty()) this.namespace = namespace;
		
		String className = Utility.readString("Main class name: ", "[Example: Main]");
		if(!className.isEmpty()) this.className = className;
		
		String version = Utility.readString("Version of plugin: ", null);
		if(!version.isEmpty()) this.version = version;
		
		String author = Utility.readString("Author of plugin: ", null);
		if(!author.isEmpty()) this.author = author;
		
		String apiVersion = Utility.readString("API Version: ", "[Recommended 3.0.0]");
		if(!apiVersion.isEmpty()) this.apiVersion = apiVersion;
	}
	
	/**
	 * Create the file plugin.yml (It's a good thing use getPluginYAML() method first)
	 */
	public void createPluginYAML(){
		File plcr = new File("PluginsCreator" + File.separator + pluginName);
		if(!plcr.exists()) plcr.mkdirs();
		
		Utility.writeStringData(new File(plcr + File.separator + "plugin.yml"), buildPluginYAML());
	}
	
	protected String buildPluginYAML(){
		String temp = namespace.replaceAll("src/", "");
		return "name: " + pluginName + "\n" + 
			   "author: " + author + "\n" +
			   "version: " + version + "\n" +
			   "api: [" + apiVersion + "]\n" +
			   "main: " + temp + "\\" + className;
	}

}