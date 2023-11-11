package com.github.mrbean355.fs

fun main() {
    FileSystem().apply {
        mkdir("/")
        mkdir("/a/b/c")
        mkdir("/a/b/c/1")
        mkdir("/a/b/c/2")
        mkdir("/a/x/y")
        mkdir("/z")
        addContentToFile("/a/b/t.txt", "Allo")

        print()
        println()

        rm("/a/b/c")
        rm("/a/b/t.txt")
        print()
    }
}