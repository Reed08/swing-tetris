#!/bin/bash

# Clean previous builds
rm -rf target
mkdir -p target

# Compile your Java code from src/main/java
javac -d target src/main/java/Main.java

# Copy resources from src/main/resources into target for inclusion in the JAR
cp -R src/main/resources/* target/

# Create a JAR file with the compiled classes and resources
jar cfe Tetris.jar src.main.java.Main -C target .

# Detect the operating system and set jpackage parameters
OS=$(uname)
if [[ "$OS" == "Darwin" ]]; then
    PACKAGE_TYPE="dmg"
    ICON="icon.icns"
elif [[ "$OS" == MINGW* || "$OS" == MSYS* || "$OS" == CYGWIN* ]]; then
    PACKAGE_TYPE="exe"
    ICON="icon.ico"
else
    echo "Unsupported OS: $OS"
    exit 1
fi

# Use jpackage to create an executable using resources from src/main/resources
jpackage --input . \
    --name Tetris \
    --main-jar Tetris.jar \
    --main-class src.main.java.Main \
    --type "$PACKAGE_TYPE" \
    --icon "$ICON" \
    --resource-dir src/main/resources