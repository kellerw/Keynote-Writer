# Set the file name of your jar package:
JAR_PKG = KeynoteWriter.jar

#Set name of class to run
MAIN_CLASS = KeynoteWriter

JAVAC = javac
JAVA = java
JFLAGS = -encoding UTF-8
CLASSPATH = .

all:
  compile jar clean

compile:
  $(JAVAC) -cp $(CLASSPATH) $(JFLAGS) *java

jar:
  jar cvfe $(JAR_PKG) $(MAIN_CLASS) *class

clean:
  rm *class
