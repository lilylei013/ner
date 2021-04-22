package com.advs.ner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import edu.stanford.nlp.ie.ClassifierCombiner;
import edu.stanford.nlp.ie.NERClassifierCombiner;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.util.StringUtils;

public class CombineNERDemo {
	public static void main(String[] args) throws Exception {
		    String[] parameters = new String[6];
		    parameters[0] = "-textFile";
		    parameters[1] = "mytest.txt";
		    parameters[2] = "-ner.model";
//		    parameters[3] = "classifiers/english.all.3class.distsim.crf.ser.gz,classifiers/english.conll.4class.distsim.crf.ser.gz,classifiers/english.muc.7class.distsim.crf.ser.gz";
		    parameters[3] = "classifiers/myadvs.ser.gz,classifiers/myibm.ser.gz,classifiers/myapple.ser.gz";
		    parameters[4] = "-useSUTime";
		    parameters[5] = "true";
		    
		    Properties props = StringUtils.argsToProperties(parameters);
		    SeqClassifierFlags flags = new SeqClassifierFlags(props, false); // false for print probs as printed in next code block

		    String loadPath = props.getProperty("loadClassifier");
		    NERClassifierCombiner ncc;
		    if (loadPath != null) {
		      // note that when loading a serialized classifier, the philosophy is override
		      // any settings in props with those given in the commandline
		      // so if you dumped it with useSUTime = false, and you say -useSUTime at
		      // the commandline, the commandline takes precedence
		      ncc = NERClassifierCombiner.getClassifier(loadPath,props);
		    } else {
		      // pass null for passDownProperties to let all props go through
		      ncc = NERClassifierCombiner.createNERClassifierCombiner("ner", null, props);
		    }

		    // write the NERClassifierCombiner to the given path on disk
		    String serializeTo = props.getProperty("serializeTo");
		    if (serializeTo != null) {
		      ncc.serializeClassifier(serializeTo);
		    }

		    String textFile = props.getProperty("textFile");
		    if (textFile != null) {
		      ncc.classifyAndWriteAnswers(textFile);
		    }

		    // run on multiple textFiles , based off CRFClassifier code
		    String textFiles = props.getProperty("textFiles");
		    if (textFiles != null) {
		      List<File> files = new ArrayList<>();
		      for (String filename : textFiles.split(",")) {
		        files.add(new File(filename));
		      }
		      ncc.classifyFilesAndWriteAnswers(files);
		    }

		    // options for run the NERClassifierCombiner on a testFile or testFiles
		    String testFile = props.getProperty("testFile");
		    String testFiles = props.getProperty("testFiles");
		    String crfToExamine = props.getProperty("crfToExamine");
		    DocumentReaderAndWriter<CoreLabel> readerAndWriter = ncc.defaultReaderAndWriter();
		    if (testFile != null || testFiles != null) {
		      // check if there is not a crf specific request
		      if (crfToExamine == null) {
		        // in this case there is no crfToExamine
		        if (testFile != null) {
		          ncc.classifyAndWriteAnswers(testFile, readerAndWriter, true);
		        } else {
		          List<File> files = Arrays.stream(testFiles.split(",")).map(File::new).collect(Collectors.toList());
		          ncc.classifyFilesAndWriteAnswers(files, ncc.defaultReaderAndWriter(), true);
		        }
		      } else {
		        ClassifierCombiner.examineCRF(ncc, crfToExamine, flags, testFile, testFiles, readerAndWriter);
		      }
		    }

		    // option for showing info about the NERClassifierCombiner
		    String showNCCInfo = props.getProperty("showNCCInfo");
		    if (showNCCInfo != null) {
		    	NERClassifierCombiner.showNCCInfo(ncc);
		    }

		    // option for reading in from stdin
		    if (flags.readStdin) {
		      ncc.classifyStdin();
		    }
	}

}
