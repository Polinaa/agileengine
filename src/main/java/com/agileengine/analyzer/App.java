package com.agileengine.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        if (args.length != 2) {
            LOGGER.info("The program expects to have two arguments: <input_origin_file_path> <input_other_sample_file_path>");
            return;
        }

        File originFile = new File(args[0]);
        File sampleFile = new File(args[1]);

        String attrKey = "id";
        String targetElementId = "make-everything-ok-button";

        Analyzer analyzer = new Analyzer();

        try {
            LOGGER.info("Attempt to find a similar element in a sample file [{}] to an element with [{}={}] in the origin file [{}]", args[1], attrKey, targetElementId, args[0]);
            analyzer.analyze(originFile, sampleFile, attrKey, targetElementId);
        } catch (AnalyzerException e) {
            LOGGER.error("Something went wrong. See previous logs for more info");
        }
    }
}
