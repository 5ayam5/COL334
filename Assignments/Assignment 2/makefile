runServer: Server
	java Server

Server: Server.class

Server.class: Server.java
	javac Server.java

runClient: Client
	java Client $(username)

Client: Client.class

Client.class: Client.java
	javac Client.java

report: report.pdf

report.pdf: report.tex
	latexmk -pdf --shell-escape report.tex

clean:
	latexmk -c
	rm -rf _minted-report *.class
