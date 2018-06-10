#!/bin/sh

###
# File     : install.sh
# License  :
#   Copyright (c) 2018 kolektif Contributors
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
###

# Use default Gradle binaries (no source)
# ~~~~~~
# `sed` is used rather than replacing the whole file as the file is
# automatically updated every time the version changed.
sed -i -e 's/-all.zip/-bin.zip/g' ./gradle/wrapper/gradle-wrapper.properties

# Use CI-specific Gradle configuration
# ~~~~~~
cp -rf ./scripts/travis/gradle.properties ./gradle.properties

# Run the minimal set of tasks to prepare the project for testing
# ~~~~~~
# Travis by default runs `assemble` which generates full distributable
# artifacts (e.g. Javadocs).
./gradlew clean testClasses
