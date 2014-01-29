package com.eyefreight.tests.classloader.transformer.appender;

import com.eyefreight.tests.classloader.api.Transformer;

public class AppenderTransformer implements Transformer {

	public String doTransform(String input) {
		return "===stuff===" + input + "===more stuff===";
	}

}
