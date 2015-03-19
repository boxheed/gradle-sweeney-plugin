[![Build Status](https://api.shippable.com/projects/54fcdb365ab6cc13529548b3/badge?branchName=master)](https://app.shippable.com/projects/54fcdb365ab6cc13529548b3/builds/latest)
# Gradle Layout Plugin
A Gradle plugin providing the ability to specify the folder and file layout of the project, optionally including the contents of file.

# Usage
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        //The gitignore plugin.
        classpath 'com.fizzpod:gradle-layout-plugin:0+'
    }
}

apply plugin: 'com.fizzpod.layout'

layout {
    // Create an empty file
    file 'filename1.txt'
    // Create an empty file in subfolders
    file 'folder1/folder1_1/filename2.txt'
    // Create a file in a subfolder with some content
    file ('folder1/folder1_1/filename3.txt') {
        "I am some content"
    }
    // Create a file in a subfolder with a multiline content
    file ('folder1/folder1_1/filename4.txt') {
        """I am some content
Split over 
multiple lines"""
    }
    path 'folder2/folder2_1'
}

```

# Tasks
The plugin exposes a single task `createLayout` which creates the specified layout. If a file exists it will not overwrite it.