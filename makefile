c&r:
	make c r
c:
	javac -s src/ -d bin/ src/tp/*.java
r:
	java -cp bin/ tp.Application
clean:
	rm bin/ -r
	rm cpu*.txt
