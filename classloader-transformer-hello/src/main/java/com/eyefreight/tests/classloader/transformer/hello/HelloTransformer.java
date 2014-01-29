package com.eyefreight.tests.classloader.transformer.hello;

import com.eyefreight.tests.classloader.api.Transformer;

public class HelloTransformer implements Transformer {

	public String doTransform(String input) {
		return "Hello world!";
	}

}
