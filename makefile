JCC = javac

default: TracerouteParser.class

TracerouteParser.class: TracerouteParser.java
		$(JCC) -g TracerouteParser.java

run:
		java TracerouteParser

clean:
		$(RM) *.class