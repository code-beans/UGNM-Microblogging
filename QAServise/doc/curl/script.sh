#!/bin/bash
 URL='http://localhost:8080/ugnmMicro'
# URL='http://buche.informatik.rwth-aachen.de:8080/ugnmMicro'
echo "curl test script for las2peer service"

echo "test authentication with test user alice"
curl -v -X GET $URL/validate --user alice:pwalice
echo 
echo "PRESS RETURN TO CONTINUE..."
read

echo "get a hashtag anonymous"
curl -v -X GET $URL/hashtag/3
echo
echo "PRESS RETURN TO CONTINUE..."
read

echo "get a hashtag"
curl -v -X GET $URL/hashtag/3 --user alice:pwalice
echo
echo "PRESS RETURN TO CONTINUE..."
read

echo "get a user"
curl -v -X GET $URL/user/3 --user alice:pwalice
echo
echo "PRESS RETURN TO CONTINUE..."
read

echo "more curl commandlines..."


