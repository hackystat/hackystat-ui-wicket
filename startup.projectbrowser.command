echo -n -e "\033]0;ProjectBrowser $HACKYSTAT_VERSION\007"; 
java -Xmx512M -jar $HACKYSTAT_SERVICE_DIST/hackystat-ui-wicket/projectbrowser.jar
