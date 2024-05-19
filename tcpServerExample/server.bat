@echo on

rem Compile Java files
javac  tftpexample/*.java

rem compile the data structures
javac C:\Users\marco\Desktop\proyecto2 (1)\proyecto2\tcpServerExample\tftpexample\*.java

rem Run the compiled Java program
java tftpexample.tftpServer 8888

