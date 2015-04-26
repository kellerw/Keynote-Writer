# Set the file name of your jar package:
JAR_PKG = KeynoteWriter

#Set name of class to run
MAIN_CLASS = Main

JAVAC = javac
JAVA = java
JFLAGS = -encoding UTF-8
CLASSPATH = .

all: compile jar clean

compile:
	$(JAVAC) -cp $(CLASSPATH) $(JFLAGS) *java

jar:
	jar cvfe $(JAR_PKG).jar $(MAIN_CLASS) *class lists.xml
	jar cvfe $(JAR_PKG)-GUI.jar $(MAIN_CLASS)_GUI *class lists.xml

clean:
	rm *class
