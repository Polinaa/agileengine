Run precompiled executable

	java -jar analyzer.jar <origin_file_path> <sample_file_path>

(All provided files are stored in ./samples)


OR


Compile
    
    mvn clean install    

Run

    java -jar target/analyzer.jar <origin_file_path> <sample_file_path>
    
Example

    java -jar target/analyzer.jar ./samples/sample-0-origin.html ./samples/sample-1-evil-gemini.html 
    
