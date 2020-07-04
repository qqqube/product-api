#! /bin/bash

chmod +x test-scripts/test1.sh;
./test-scripts/test1.sh > output/test1.txt;

result=$(diff test-output/test1-output.txt output/test1.txt);
[[ ! -z $result ]] && echo "Failed Test 1" || echo "Passed Test 1"

chmod +x test-scripts/test2.sh;
./test-scripts/test2.sh > output/test2.txt;

result=$(diff test-output/test2-output.txt output/test2.txt);
[[ ! -z $result ]] && echo "Failed Test 2" || echo "Passed Test 2"

chmod +x test-scripts/test3.sh;
./test-scripts/test3.sh > output/test3.txt;

result=$(diff test-output/test3-output.txt output/test3.txt);
[[ ! -z $result ]] && echo "Failed Test 3" || echo "Passed Test 3"

chmod +x test-scripts/test4.sh;
./test-scripts/test4.sh > output/test4.txt;

result=$(diff test-output/test4-output.txt output/test4.txt);
[[ ! -z $result ]] && echo "Failed Test 4" || echo "Passed Test 4"

chmod +x test-scripts/test5.sh;
./test-scripts/test5.sh > output/test5.txt;

result=$(diff test-output/test5-output.txt output/test5.txt);
[[ ! -z $result ]] && echo "Failed Test 5" || echo "Passed Test 5"

chmod +x test-scripts/test6.sh;
./test-scripts/test6.sh > output/test6.txt;

result=$(diff test-output/test6-output.txt output/test6.txt);
[[ ! -z $result ]] && echo "Failed Test 6" || echo "Passed Test 6"
