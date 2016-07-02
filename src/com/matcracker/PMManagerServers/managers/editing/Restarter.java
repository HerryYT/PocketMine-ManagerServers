package com.matcracker.PMManagerServers.managers.editing;

import java.io.File;
import java.io.IOException;

import com.matcracker.PMManagerServers.API.UtilityServersAPI;
import com.matcracker.PMManagerServers.lang.BaseLang;
import com.matcracker.PMManagerServers.managers.Manager;
import com.matcracker.PMManagerServers.utility.ProcessManager;
import com.matcracker.PMManagerServers.utility.Utility;
import com.matcracker.PMManagerServers.utility.UtilityColor;

public class Restarter {
	static int time;
	static String path;
	static BackgroudTask task;
	
	public static void restarterMenu() throws IOException{
		Utility.cleanScreen();
		System.out.println(Utility.softwareName);
		System.out.println(Utility.setTitle("&c", BaseLang.translate("pm.title.restarter")));
		System.out.println("1- " + BaseLang.translate("pm.restarter.setupTimer"));
		System.out.println("2- " + BaseLang.translate("pm.restarter.restartNow"));
		System.out.println("3- " + BaseLang.translate("pm.restarter.kill"));
		System.out.println("4- " + BaseLang.translate("pm.standard.back"));
		int option = Utility.readInt(BaseLang.translate("pm.choice.option") + " ", null);
		
		if(option == 1 || option == 2){
			Utility.showServers();
			int server = Utility.readInt(BaseLang.translate("pm.choice.server") + " ", null);
			
			if(option == 1)
				setupRestarter(server);
			
			if(option == 2)
				restartServer(server);
		}
		
		if(option == 3)
			killRestarter();
		
		if(option == 4)
			Manager.managerMenu();
		
		restarterMenu();
	}

	public static void restartServer(int server) throws IOException {
		String proc = "mintty.exe";
		if(UtilityServersAPI.checkServersFile("Path", "path_", server)){
			String pathContent = UtilityServersAPI.getPath(server);
			if(pathContent != null){
				if(ProcessManager.isProcessEnabled(proc)){
					int pid = ProcessManager.getPIDByName(proc);
					ProcessManager.killProcess(pid);
					ProcessManager.startProcess(UtilityServersAPI.getPath(server));
					Utility.waitConfirm(BaseLang.translate("pm.restarter.restarted") + " " + UtilityServersAPI.getNameServer(server));
				}else
					Utility.waitConfirm(BaseLang.translate("pm.restarter.serverNotRunning"));
			}else
				Utility.waitConfirm(UtilityColor.COLOR_RED + BaseLang.translate("pm.errors.pathNull"));
		}else
			Utility.waitConfirm(UtilityColor.COLOR_RED + BaseLang.translate("pm.errors.pathNotFound"));
	}

	public static BackgroudTask getTask(){
		return task;
	}

	private static void setupRestarter(int server) throws IOException {
		if(server == -1){
		
		}else{
			if(UtilityServersAPI.checkServersFile("Path", "path_", server)){
				String pathContent = UtilityServersAPI.getPath(server);
				if(pathContent != null){
					System.out.println("1- " + BaseLang.translate("pm.restarter.days"));
					System.out.println("2- " + BaseLang.translate("pm.restarter.hours"));
					System.out.println("3- " + BaseLang.translate("pm.restarter.minutes"));
					int opt = Utility.readInt(BaseLang.translate("pm.restarter.selectTypeTime") + " ", null);
					
					int timer = Utility.readInt(BaseLang.translate("pm.restarter.selectTime") + " ", "[" + BaseLang.translate("pm.restarter.defaultTime") + "]");
					
					if(timer > 0){
						if(opt == 1)
							time = timer * 24 * 3600; //Days
						else if(opt == 2)
							time = timer * 3600; //Hour
						else if(opt == 3)
							time = timer * 60; //Minutes
					}else
						time = 3;
					
					time = time * 1000; //Milliseconds
					
					path = pathContent;
					
					task = new BackgroudTask(time, path, true);
					task.start();
					
				}else
					Utility.waitConfirm(UtilityColor.COLOR_RED + BaseLang.translate("pm.errors.pathNull"));
			}else
				Utility.waitConfirm(UtilityColor.COLOR_RED + BaseLang.translate("pm.errors.pathNotFound"));
		}
	}
	
	private static void killRestarter(){
		if(task != null){
			if(!task.isInterrupted() && task.isRunning()){
				task.interrupt();
				Utility.waitConfirm(BaseLang.translate("pm.restarter.killed"));
			}else
				Utility.waitConfirm(BaseLang.translate("pm.restarter.restarterNotRunning"));
		}else
			Utility.waitConfirm(BaseLang.translate("pm.restarter.restarterNotRunning"));
	}
}

class BackgroudTask extends Thread{
	private int time;
	private String serverPath;
	private boolean isRunning = false;
		
	public BackgroudTask(int time, String serverPath){
		this.time = time;
		this.serverPath = serverPath;
	}
	
	public BackgroudTask(int time, String serverPath, boolean isRunning){
		this.time = time;
		this.serverPath = serverPath;
		this.isRunning = isRunning;
	}
	
	public int getTime(){
		return this.time;
	}
	
	public boolean isRunning(){
		return this.isRunning;
	}
	
	public void setRunning(boolean isRunning){
		this.isRunning = isRunning;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
	public String getPath(){
		return this.serverPath;
	}
	
	public void setPath(String path){
		this.serverPath = path;
	}
	
	public void run(){
			String proc = "mintty.exe";
			try{
				while(isRunning){
					if(ProcessManager.isProcessEnabled(proc)){
						int pid = ProcessManager.getPIDByName(proc);
						sleep(getTime());
						System.out.println("\n" + BaseLang.translate("pm.task.stopping"));
						ProcessManager.killProcess(pid);
						System.out.println("\n" + BaseLang.translate("pm.task.restarting"));
						sleep(3000);
						ProcessManager.startProcess(getPath() + File.separator + "start.cmd");

						run();
					}else{
						System.out.println("\n" + BaseLang.translate("pm.task.serverNotRunning"));
						setRunning(false);
					}
				}
			}catch(IOException | InterruptedException e){
				System.out.println("\n"  + BaseLang.translate("pm.task.error"));
				setRunning(false);
			}
	}
}
