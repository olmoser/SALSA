package at.ac.tuwien.dsg.cloud.salsa.knowledge.basic;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import at.ac.tuwien.dsg.cloud.salsa.knowledge.process.DeploymentObject;

public class ListOfClass {
	
	/**
	 * The list of classes that aren't count into the list
	 */
	static List<Class<? extends Object>> exceptLst = Arrays.asList(
			ListOfClass.class,
			Main.class
			);	
	
	public static List<Class<? extends DeploymentObject>> getClassListManual(){		
		List<Class<? extends DeploymentObject>> lst = Arrays.asList(
				DeploymentObjectIaaS.class,
				DeploymentObjectOS.class,
				DeploymentObjectTomcat.class,
				DeploymentObjectJvm.class,
				DeploymentObjectSh.class,
				DeploymentObjectJar.class
		);
		return lst;
	}


	/**
     * Attempts to list all the classes in the specified package as determined
     * by the context class loader
     * 
     * @param pckgname
     *            the package name to search
     * @return a list of classes that exist within that package
     * @throws ClassNotFoundException
     *             if something went wrong
     */
	public static List<Class<? extends DeploymentObject>> getClassList() throws ClassNotFoundException {
    // This will hold a list of directories matching the pckgname. There may be more than one if a package is split over multiple jars/paths
	String pckgname = ListOfClass.class.getPackage().getName();
    ArrayList<File> directories = new ArrayList<File>();
    try {
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        if (cld == null) {
            throw new ClassNotFoundException("Can't get class loader.");
        }
        String path = pckgname.replace('.', '/');
        // Ask for all resources for the path
        Enumeration<URL> resources = cld.getResources(path);
        while (resources.hasMoreElements()) {
            directories.add(new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8")));
        }
    } catch (NullPointerException x) {
        throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)");
    } catch (UnsupportedEncodingException encex) {
        throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)");
    } catch (IOException ioex) {
        throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname);
    }

    ArrayList<Class<? extends DeploymentObject>> classes = new ArrayList<Class<? extends DeploymentObject>>();
    // For every directory identified capture all the .class files
    for (File directory : directories) {
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (String file : files) {
                // we are only interested in .class files
                if (file.endsWith(".class")) {
                    // removes the .class extension
                  try
                  {
                	  String className = pckgname + '.' + file.substring(0, file.length() - 6);
                	  if (!exceptLst.contains(Class.forName(className))){
                		  classes.add((Class<? extends DeploymentObject>)Class.forName(className));
                	  }
                  }
                  catch (NoClassDefFoundError e)
                  {
                    // do nothing. this class hasn't been found by the loader, and we don't care.
                  }
                }
            }
        } else {
            throw new ClassNotFoundException(pckgname + " (" + directory.getPath() + ") does not appear to be a valid package");
        }
    }
    return classes;
}  
	
}	// end class