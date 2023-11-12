package com.github.mrbean355.fs

interface FileSystem {

    /**
     * Create a new directory at the given path.
     */
    fun mkdir(path: String)

    /**
     * Add content to the file at the given path.
     * If the file does not exist, it will be created.
     */
    fun addContentToFile(path: String, content: String)

    /**
     * Read and return the content of the file at the given path.
     * If the file does not exist, an empty string is returned.
     */
    fun readContentFromFile(path: String): String

    /**
     * List the files and directories at the given path.
     * The output will be in lexicographical order.
     */
    fun ls(path: String): List<String>

    /**
     * Remove the file or directory at the given path.
     */
    fun rm(path: String)

    /**
     * Print the entire file system in a tree structure.
     */
    fun print()

}

fun FileSystem(): FileSystem {
    return FileSystemImpl(delimiter = '/')
}
