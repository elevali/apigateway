package io.eleva.apigateway.gateway.spi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import io.eleva.apigateway.api.spi.Service;
import io.eleva.apigateway.gateway.exception.SpiKeyDuplicationException;

public class ServiceBootstrap {	
	  private static final String SERVICES_DIRECTORY = "META-INF/services/";
	  private final Class type = Service.class;
	  
	  private Map<String, Class<?>> services;
	  private ServiceBootstrap() {
		  services = loadExtensionClasses();
	  }
	  
	  private final static ServiceBootstrap instance = new ServiceBootstrap();
	  
	  public static ServiceBootstrap getInstance() {
		  return instance;
	  }
	  
	  public Map<String, Class<?>> loadServiceClasses(){
		  return services;
	  }
	  
	  private ClassLoader findClassLoader() {
	        return ServiceBootstrap.class.getClassLoader();
	  }
	  private Map<String, Class<?>> loadExtensionClasses() {	        
	        Map<String, Class<?>> extensionClasses = new HashMap<String, Class<?>>();	        
	        loadDirectory(extensionClasses, SERVICES_DIRECTORY);
	        return extensionClasses;
	  }
	  private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir) {
	        String fileName = dir + type.getName();
	        try {
	            Enumeration<java.net.URL> urls;
	            ClassLoader classLoader = findClassLoader();
	            if (classLoader != null) {
	                urls = classLoader.getResources(fileName);
	            } else {
	                urls = ClassLoader.getSystemResources(fileName);
	            }
	            if (urls != null) {
	                while (urls.hasMoreElements()) {
	                    java.net.URL resourceURL = urls.nextElement();
	                    loadResource(extensionClasses, classLoader, resourceURL);
	                }
	            }
	        } catch (Throwable t) {
	            t.printStackTrace();
	        }
	    }
	  private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "utf-8"));
	            try {
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    final int ci = line.indexOf('#');
	                    if (ci >= 0) line = line.substring(0, ci);
	                    line = line.trim();
	                    if (line.length() > 0) {
	                        try {
	                            String name = null;
	                            int i = line.indexOf('=');
	                            if (i > 0) {
	                                name = line.substring(0, i).trim();
	                                line = line.substring(i + 1).trim();
	                                if(extensionClasses.containsKey(name)) {
	                                	String existClass = extensionClasses.get(name).getName();
	                                	throw new SpiKeyDuplicationException(line ,existClass);
	                                }
	                                extensionClasses.put(name, Class.forName(line));
	                            }
	                            
	                        } catch (Throwable t) {
	                           t.printStackTrace();
	                        }
	                    }
	                }
	            } finally {
	                reader.close();
	            }
	        } catch (Throwable t) {
	            t.printStackTrace();
	        }
	    }
}
