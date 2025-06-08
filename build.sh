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
    jpackage --input . \
        --name Tetris \
        --main-jar Tetris.jar \
        --main-class src.main.java.Main \
        --type dmg \
        --icon icon.icns \
        --resource-dir src/main/resources \
        --mac-package-identifier xyz.reed920.Tetris \
        --mac-sign
elif [[ "$OS" == MINGW* || "$OS" == MSYS* || "$OS" == CYGWIN* ]]; then
    jpackage --input . \
        --type exe \
        --name Tetris \
        --main-jar Tetris.jar \
        --main-class src.main.java.Main \
        --icon icon.ico \
        --win-menu \
        --win-shortcut 
else
    echo "Unsupported OS: $OS"
    exit 1
fi

# Use jpackage to create an executable using resources from src/main/resources
