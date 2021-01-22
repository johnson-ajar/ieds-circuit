package com.soa.circuit.model.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class FileResource {
	
	private static FileResource readFile = null; 
	
	private FileResource(){ 
		
	}
	
	public static FileResource getInstance(){
		if(readFile == null){
			readFile = new FileResource();
			return readFile;
		}
			return readFile;
	}
	//This can be used only if the file is located in the child package of this class.
	public URL getFileURL(String fileName){
		return FileResource.class.getResource(fileName);
	}
	
	private InputStream getFileInputStream(String folderName, String fileName){
		InputStream is = FileResource.class.getClassLoader().getResourceAsStream(folderName+"/"+fileName);
		if(is == null){
			String name = "/"+(folderName.length()>0? folderName+"/"+fileName:fileName);
			URL iniURL = this.getFileURL(name);
			if(iniURL.getProtocol().equals("jar")){
				is= getClass().getResourceAsStream(name);
			}
		}
		return is;
	}
	
	public BufferedWriter getBufferedWriter(String bundleID,String location, String fileName){
		try {
			Bundle bundle = Platform.getBundle(bundleID);
			URL fileURL = bundle.getEntry(location+"/"+fileName);
		    String fileLocation = FileLocator.resolve(fileURL).getFile();
		    BufferedWriter in = new BufferedWriter(new FileWriter(new File(fileLocation)));
		   return in;
			
		}catch (IOException e) {
		    e.printStackTrace();
		}
		return null;
	}
	
	public BufferedReader getBufferedReader(String bundleID,String folder, String fileName){
			InputStream file = getFile(bundleID, folder, fileName);
		    BufferedReader in = new BufferedReader(new InputStreamReader(file));
		   return in;
	}
	
	//This function can be used in OSGI environment
	public InputStream getFile(String bundleID, String location, String fileName){
		URL fileURL = null;
		InputStream ips = null;
		try {
		Bundle bundle = Platform.getBundle(bundleID);
		if(bundle == null){
			ips = getFileInputStream(location,fileName);
		}else{
         fileURL = bundle.getResource(location+"/"+fileName);
         File file = new File(FileLocator.toFileURL(fileURL).toURI());  
         ips = new FileInputStream(file);
		}
        }catch(MalformedURLException e){
        	e.printStackTrace();
        }  catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
		return ips;
	}
	
	//Use this when reading files from jar or a folder
	private List<InputStream> getFiles(String folderName, String extension){
		List<InputStream> iStreams = new ArrayList<InputStream>();
		final File jarFile = new File(FileResource.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		try{
			
			if(jarFile.isFile()){
				JarFile jar  = new JarFile(jarFile);
				final Enumeration<JarEntry> entries = jar.entries();
				while(entries.hasMoreElements()){
					JarEntry jarEntry = entries.nextElement();
					final String name  = jarEntry.getName();
					if(name.startsWith(folderName)&&name.endsWith(extension)){
						iStreams.add(getFileInputStream("", name));
					}
				}
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return iStreams;
	}
	
	public List<InputStream> getFiles(String bundleID, String folderName, String fileExtension){
		List<InputStream> files = new ArrayList<InputStream>();
		Bundle bundle = Platform.getBundle(bundleID);
		File dir = null;
		URL iniURL = null;
		
		try{
			if(bundle != null){
				iniURL = bundle.getEntry(folderName);
				dir = new File(FileLocator.resolve(iniURL).toURI());
				if(dir.isDirectory()){
					Enumeration<URL> urls = bundle.findEntries("/"+folderName, "*", false);
						while(urls.hasMoreElements()){
							URL url = urls.nextElement();
							File file = new File(FileLocator.resolve(url).toURI());
							files.add(new FileInputStream(file));
						}
					
				}else{
					 files.add(new FileInputStream(dir));
				}
			}else{
				files.addAll(getFiles(folderName, fileExtension));
			}
		}catch(URISyntaxException e){
			e.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}
		return files;
	}
	
}
