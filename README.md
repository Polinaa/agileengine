Run precompiled executable

	java -jar analyzer.jar <origin_file_path> <sample_file_path>

(All provided files are stored in ./samples)


OR


Compile
    
    mvn clean install    

and then run

    java -jar target/analyzer.jar <origin_file_path> <sample_file_path>
    
Example

    java -jar target/analyzer.jar ./samples/sample-0-origin.html ./samples/sample-1-evil-gemini.html 
    
Simple output

	[main] INFO com.agileengine.analyzer.App - Attempt to find a similar element in a sample file [./samples/sample-1-evil-gemini.html] to an element with [id=make-everything-ok-button] in the origin file [./samples/sample-0-origin.html]
	[main] INFO com.agileengine.analyzer.Analyzer - Found an element by attribute [id=make-everything-ok-button]
	[main] INFO com.agileengine.analyzer.Analyzer - Found a similar element by the following attributes: [class="btn btn-success", title="Make-Button", onclick="javascript:window.okDone(); return false;"]
	[main] INFO com.agileengine.analyzer.Analyzer - Element path: #root > html > body > div > div > div[3] > div[1] > div > div[2] > a[2]
