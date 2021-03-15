workdir=$(pwd)
#all ft and la instances
# java -jar build/libs/JSP.jar --solver basic Descent Tabou random SPT LRPT EST_LRPT EST_SPT --instance ft06 ft10 ft20 la01 la02 la03 la04 la05 la06 la07 la08 la09 la10 la11 la12 la13 la14 la15 la16 la17 la18 la19 la20 la21 la22 la23 la24 la25 la26 la27 la28 la29 la30 la31 la32 la33 la34 la35 la36 la37 la38 la39 la40 > benchmark.txt
# mv benchmark.txt $workdir/data/benchmark.txt

# descent ft and la instances
# java -jar build/libs/JSP.jar --solver basic Descent --instance ft06 ft10 ft20 la01 la02 la03 la04 la05 la06 la07 la08 la09 la10 la11 la12 la13 la14 la15 la16 la17 la18 la19 la20 la21 la22 la23 la24 la25 la26 la27 la28 la29 la30 la31 la32 la33 la34 la35 la36 la37 la38 la39 la40 > descent.txt
# mv descent.txt $workdir/data/descent.txt

#subsample
#java -jar build/libs/JSP.jar --solver basic Descent Tabou random SPT LRPT EST_LRPT EST_SPT --instance ft06 ft10 la01 la02 la03 la04 > benchmark.txt


# all instances
java -jar build/libs/JSP.jar --solver basic Descent Tabou random SPT LRPT EST_LRPT EST_SPT --instance aaa1 la02 la12 la22 la32  orb02 swv01 swv11 ta01 ta11 ta21 ta31 ta41 ta51 ta61 yn1 abz5 la03 la13 la23 la33  orb03 swv02 swv12 ta02 ta12 ta22 ta32 ta42 ta52 ta62 yn2 abz6 la04 la14 la24 la34  orb04 swv03 swv13 ta03 ta13 ta23 ta33 ta43 ta53 ta63 yn3 abz7 la05 la15 la25 la35  orb05 swv04 swv14 ta04 ta14 ta24 ta34 ta44 ta54 ta64 yn4 abz8 la06 la16 la26 la36  orb06 swv05 swv15 ta05 ta15 ta25 ta35 ta45 ta55 ta65 abz9 la07 la17 la27 la37  orb07 swv06 swv16 ta06 ta16 ta26 ta36 ta46 ta56 ta66 ft06 la08 la18 la28 la38  orb08 swv07 swv17 ta07 ta17 ta27 ta37 ta47 ta57 ta67 ft10 la09 la19 la29 la39  orb09 swv08 swv18 ta08 ta18 ta28 ta38 ta48 ta58 ta68 ft20 la10 la20 la30 la40  orb10 swv09 swv19 ta09 ta19 ta29 ta39 ta49 ta59 ta69 la01 la11 la21 la31 orb01 swv10 swv20 ta10 ta20 ta30 ta40 ta50 ta60 > benchmark_full.txt
mv benchmark_full.txt $workdir/data/benchmark_full.txt