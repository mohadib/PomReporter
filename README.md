A Spring REST app to track information from pom files.

This app will checkout pom files from subversion repositories (just the pom file).
It will then run an xpath expression on the pom file to pull out some piece of 
information. In addition it will report the last 10 days of logs and current revision.

This app is just the backed REST piece. A frontend SPA written with React and Redux
can be found here: https://github.com/mohadib/ReactPomReporter