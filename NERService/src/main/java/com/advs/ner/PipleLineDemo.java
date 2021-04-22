package com.advs.ner;

import java.io.IOException;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.util.SystemUtils;

public class PipleLineDemo extends StanfordCoreNLP{
	public static void main(String[] args) throws IOException {
		Properties props = new Properties();
	    if (args.length > 0) {
	      props = StringUtils.argsToProperties(args);
	      String helpValue = props.getProperty("h", props.getProperty("help"));
	      if (helpValue != null) {
	    	  printHelp(System.err, helpValue);
	        return;
	      }
	    }
	    // Run the pipeline
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    if (PropertiesUtils.getBool(props, "memoryUsage", false)) {
	      System.gc();
	      System.gc();
	      System.out.println("Finished loading pipeline.  Current memory usage: " +
	                  SystemUtils.getMemoryInUse() + "mb");
	    }
	    pipeline.run(true);
	  
	}

}
