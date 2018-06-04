package com.agileengine.analyzer;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Analyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Analyzer.class);
    private static final String CHARSET_NAME = "utf8";
    private static final String TAG_PATH_SEPARATOR = " > ";

    public void analyze(File originFile, File sampleFile, String attrKey, String targetElementId) throws AnalyzerException {
        Document originDoc = getDocument(originFile)
                .orElseThrow(AnalyzerException::new);
        Document sampleDoc = getDocument(sampleFile)
                .orElseThrow(AnalyzerException::new);
        Element elementToFound = findElementByAttr(originDoc, new Attribute(attrKey, targetElementId))
                .orElseThrow(AnalyzerException::new);
        Pair<Element, List<Attribute>> similarElementAndCommonAttrs = findSimilarElement(sampleDoc, elementToFound)
                .orElseThrow(AnalyzerException::new);
        StringBuilder path = getPath(similarElementAndCommonAttrs.getKey(), new StringBuilder());
        LOGGER.info("Found a similar element by the following attributes: " + similarElementAndCommonAttrs.getValue());
        LOGGER.info("Element path: " + path.toString());
    }

    private StringBuilder getPath(Element element, StringBuilder path) {
        if (element != null) {
            String curTag = element.tagName();
            Element parent = element.parent();
            if (parent != null) {
                curTag = TAG_PATH_SEPARATOR + curTag;
                List<Element> children = parent.children().stream()
                        .filter(e -> e.tagName().equals(element.tagName()))
                        .collect(Collectors.toList());
                if (children.size() > 1) {
                    OptionalInt position = IntStream.range(0, children.size())
                            .filter(i -> children.get(i).equals(element))
                            .findFirst();
                    curTag += "[" + (position.getAsInt() + 1) + "]";
                }
                getPath(parent, path);
            }
            path.append(curTag);
        }
        return path;
    }

    private Optional<Pair<Element, List<Attribute>>> findSimilarElement(Element doc, Element elementToFind) {
        return doc.getAllElements().stream()
                .filter(element -> element.tagName().equalsIgnoreCase(elementToFind.tagName()))
                .map(element -> new Pair<>(element, findCommonAttrs(element, elementToFind)))
                .max(Comparator.comparing(pair -> pair.getValue().size()));
    }

    private List<Attribute> findCommonAttrs(Element firstElement, Element secondElement) {
        List<Attribute> attributes1 = firstElement.attributes().asList();
        List<Attribute> attributes2 = secondElement.attributes().asList();
        return attributes1.stream()
                .filter(attributes2::contains)
                .collect(Collectors.toList());
    }

    private Optional<Document> getDocument(File htmlFile) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            return Optional.of(doc);
        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    private Optional<Element> findElementByAttr(Element root, Attribute attribute) {
        if (root == null) {
            LOGGER.info("Couldn't make a search for null element");
            return Optional.empty();
        }
        if (isAttrPresent(root, attribute)) {
            LOGGER.info("Found an element by attribute [{}={}]", attribute.getKey(), attribute.getValue());
            return Optional.of(root);
        }
        for (Element childElement : root.children()) {
            Optional<Element> elementInChild = findElementByAttr(childElement, attribute);
            if (elementInChild.isPresent())
                return elementInChild;
        }
        return Optional.empty();
    }

    private boolean isAttrPresent(Element element, Attribute attribute) {
        return element.attributes().asList().stream()
                .anyMatch(attr -> attr.equals(attribute));
    }
}
