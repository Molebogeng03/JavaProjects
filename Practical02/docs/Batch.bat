@ECHO OFF
REM Computer Science 2A 2023
REM Set path for JDK
SET JAVA_HOME="C:\jdk-17"
SET PATH=%JAVA_HOME%\bin;%PATH%;

REM Set Variables
cd ..
set PRAC_BIN=.\bin
set PRAC_DOCS=.\docs
set PRAC_SRC=.\src

REM Clean old class files in bin
del %PRAC_BIN%\*.class

REM Compiling code
ECHO Trying to compile
javac -sourcepath %PRAC_SRC% -cp "%PRAC_BIN%;%PRAC_LIB%" -d %PRAC_BIN% %PRAC_SRC%\Main.java

REM Generate JavaDoc for project for only acsse subpackage.
:JAVADOC
echo ~~~ Generate JavaDoc for project ~~~
javadoc %JAVAFX_ARGS% -sourcepath %PRAC_SRC% -classpath %PRAC_BIN%;%PRAC_LIB% -use -version -author -d %PRAC_DOCS%\%PRAC_JDOC% -subpackages acsse
PAUSE

REM Running code
ECHO Trying to run
java -classpath %PRAC_BIN%;%PRAC_LIB% Main
PAUSE

REM Dissasembling class
ECHO Trying to dissasemble
javap -c %PRAC_BIN%\Main.class > %PRAC_DOCS%\ByteCode.txt

REM Go back to docs folder
cd %PRAC_DOCS%

PAUSE
