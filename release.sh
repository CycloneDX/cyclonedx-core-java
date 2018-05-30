#!/usr/bin/env bash
read -p "Really deploy to Maven Central repository (Y/N)? "
if ( [ "$REPLY" == "Y" ] ) then

read -p "Specify release version number in Maven Central (i.e. 1.0.0): "

mvn clean
mvn versions:set -DnewVersion=$REPLY
mvn clean deploy -Prelease

echo -e "\n\nIf the deploy was successful, artifact will be staged in Maven Central\n"
echo -e "Proceed to https://oss.sonatype.org\n\n"
read -n1 -r -p "Press 'Y' to continue with release or any other key to exit" key

if [ "$key" == "Y" ]; then
  mvn versions:revert
  mvn release:clean release:prepare release:perform -Prelease -X -e | tee release.log
else
    echo -e "Exit without deploy"
fi

else
  echo -e "Exit without deploy"
fi