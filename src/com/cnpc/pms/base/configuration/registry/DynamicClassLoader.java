package com.cnpc.pms.base.configuration.registry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicClassLoader extends ClassLoader {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final Map<String, File> classNameAndFile;
	private final Map<String, Class<?>> classNameAndClass;
	private final Map<File, Long> fileAndTime;
	private final Map<File, String> fileAndClassName;
	private boolean destroyed = false;

	public DynamicClassLoader(ClassLoader parent) {
		super(parent);
		this.classNameAndFile = new HashMap<String, File>();
		this.classNameAndClass = new HashMap<String, Class<?>>();
		this.fileAndTime = new HashMap<File, Long>();
		this.fileAndClassName = new HashMap<File, String>();
	}

	public DynamicClassLoader(DynamicClassLoader parent) {
		super(parent.getParent());
		this.classNameAndFile = parent.getClassNameAndFile();
		this.classNameAndClass = parent.getClassNameAndClass();
		this.fileAndTime = parent.getFileAndTime();
		this.fileAndClassName = parent.getFileAndClassName();
	}

	public void registerClass(String fullClassName, File file) throws IOException {
		if (classNameAndFile.containsKey(fullClassName) == false) {
			log.debug("Register Class[{}] with {}", fullClassName, file.getName());
			classNameAndFile.put(fullClassName, file);
			fileAndClassName.put(file, fullClassName);
			fileAndTime.put(file, file.lastModified());
		}
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (destroyed) {
			throw new RuntimeException("ClassLoader destroyed!");
		}

		String baseClassName = name;
		if (name.indexOf("$") > -1) {
			baseClassName = name.substring(0, name.indexOf("$"));
		}
		if (!classNameAndFile.keySet().contains(baseClassName)) {
			log.debug("CL@{} load [{}] ", new Object[] {Integer.toHexString(super.hashCode()), name });
			return super.loadClass(name);
		}

		Class<?> loadedClass = classNameAndClass.get(name);
		if (loadedClass != null) {
			log.debug("MC@{} get  [{}] as {} ",
				new Object[] {Integer.toHexString(super.hashCode()), name, loadedClass });
			return loadedClass;
		}
		log.debug("MC@{} load [{}] from: {}", new Object[] {Integer.toHexString(this.hashCode()), name,
				classNameAndFile.get(name) });

		try {
			URLConnection connection = classNameAndFile.get(name).toURL().openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			Class<?> clazz = defineClass(name, classData, 0, classData.length);
			// resolveClass(clazz);
			classNameAndClass.put(name, clazz);
			return clazz;
		} catch (MalformedURLException e) {
			log.error("Fail to load {}", name);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Fail to load {}", name);
			e.printStackTrace();
		} catch (LinkageError e) {
			log.error("Fail to load {}", name);
			e.printStackTrace();
		}
		return null;
	}

	public void destroy() {
		destroyed = true;
	}

	public Map<File, Long> getFileAndTime() {
		return fileAndTime;
	}

	public Map<File, String> getFileAndClassName() {
		return fileAndClassName;
	}

	public Map<String, File> getClassNameAndFile() {
		return classNameAndFile;
	}

	public Map<String, Class<?>> getClassNameAndClass() {
		return classNameAndClass;
	}

}