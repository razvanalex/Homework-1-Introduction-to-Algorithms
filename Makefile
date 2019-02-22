build:
	javac Frati.java 
	javac Ursi.java 
	javac Planificare.java
	javac Numaratoare.java

run-p1: 
	java Frati

run-p2: 
	java Ursi

run-p3: 
	java Planificare

run-p4: 
	java Numaratoare

clean:
	rm *.class *.out *.in