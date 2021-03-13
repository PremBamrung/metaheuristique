java -jar build/libs/JSP.jar --solver basic --instance ft06
echo
java -jar build/libs/JSP.jar --solver Descent --instance ft06
echo
java -jar build/libs/JSP.jar --solver Tabou --instance ft06
echo
java -jar build/libs/JSP.jar --solver random --instance ft06
echo
java -jar build/libs/JSP.jar --solver SPT --instance ft06
echo
java -jar build/libs/JSP.jar --solver LRPT --instance ft06
echo
java -jar build/libs/JSP.jar --solver EST_LRPT --instance ft06
echo
java -jar build/libs/JSP.jar --solver EST_SPT --instance ft06
echo


