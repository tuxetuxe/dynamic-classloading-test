package com.eyefreight.classloader.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import com.eyefreight.tests.classloader.api.Transformer;

public class ContentProcessor {

	public void process(File folder, String contents) {
		try {
			process(folder.toURI().toURL().toString(), contents);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Class location url is malformed:" + e.getMessage(), e);
		}
	}

	public void process(String transformerLocation, String contents) {

		Transformer transformer = getTransformer(transformerLocation);
		
		System.out.println("=======================================================");
		System.out.println("Transformer Class: " + transformer.getClass());
		System.out.println("Transformer Classloader: " + transformer.getClass().getClassLoader().toString());
		System.out.println("Input: " + contents);
		System.out.println("Output: " + transformer.doTransform(contents));
		
		transformer = null;
		System.gc();
	}

	private Transformer getTransformer(String transformerLocation) {
		ClassLoader classloader = getClassloader(transformerLocation);
		Class<? extends Transformer> transformerClass = getTransformerClass(classloader);

		try {
			return transformerClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Could not instanciate the transformer", e);
		}
	}

	private ClassLoader getClassloader(String transformerLocation) {
		URL[] urls = new URL[1];
		try {
			urls[0] = new URL(transformerLocation);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Class location url is malformed:" + e.getMessage(), e);
		}
		return new URLClassLoader(urls, this.getClass().getClassLoader());
	}

	private Class<? extends Transformer> getTransformerClass(ClassLoader classloader) {
		Configuration configuration = ConfigurationBuilder.build("com", classloader, new SubTypesScanner());
		Reflections reflections = new Reflections(configuration);
		Set<Class<? extends Transformer>> implementingTypes = reflections.getSubTypesOf(Transformer.class);

		if (implementingTypes.size() != 1) {
			throw new RuntimeException("Invalid number of transformer classes found in the classloader: " + implementingTypes.size());
		}

		return implementingTypes.iterator().next();

	}

}
