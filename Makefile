CLASSPATH =
TESTCLASSPATH = $(CLASSPATH):Test/:Test/junit4.jar
JFLAGS = -g -cp $(TESTCLASSPATH)
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	JaggedArray.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm *.class Test/*.class

test-create: classes Test/TestJaggedArray.class Test/TestRunner.class
	java -cp $(TESTCLASSPATH) TestRunner TestJaggedArray#testCreate

test-add: classes Test/TestJaggedArray.class Test/TestRunner.class
	java -cp $(TESTCLASSPATH) TestRunner TestJaggedArray#testAdd

test-remove: classes Test/TestJaggedArray.class Test/TestRunner.class
	java -cp $(TESTCLASSPATH) TestRunner TestJaggedArray#testRemove

test-pack: classes Test/TestJaggedArray.class Test/TestRunner.class
	java -cp $(TESTCLASSPATH) TestRunner TestJaggedArray#testPack

test-unpack: classes Test/TestJaggedArray.class Test/TestRunner.class
	java -cp $(TESTCLASSPATH) TestRunner TestJaggedArray#testUnpack

test-invalid: classes Test/TestJaggedArray.class Test/TestRunner.class
	java -cp $(TESTCLASSPATH) TestRunner TestJaggedArray#testInvalid

test: test-create test-add test-remove test-pack test-unpack test-invalid
