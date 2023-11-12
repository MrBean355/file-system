package com.github.mrbean355.fs

internal class FileSystemImpl(
    private val delimiter: Char,
    private val root: FsNode.Dir,
) : FileSystem {

    constructor(delimiter: Char) : this(
        delimiter = delimiter,
        root = FsNode.Dir(delimiter.toString())
    )

    override fun mkdir(path: String) {
        if (isRoot(path)) {
            return
        }
        traverse(path, createDirs = true)
    }

    override fun addContentToFile(path: String, content: String) {
        if (isRoot(path)) {
            error("Cannot add content to the root directory")
        }
        val (parentPath, name) = getParentAndChild(path)
        traverse(parentPath, createDirs = true)
            .getOrCreateFile(name).content += content
    }

    override fun readContentFromFile(path: String): String {
        if (isRoot(path)) {
            error("Cannot read content from the root directory")
        }
        val (parentPath, name) = getParentAndChild(path)
        return traverse(parentPath, createDirs = false)
            .findFile(name)?.content.orEmpty()
    }

    override fun ls(path: String): List<String> {
        return traverse(path, createDirs = false)
            .list()
    }

    override fun rm(path: String) {
        if (isRoot(path)) {
            error("Cannot delete the root directory!")
        }
        val (parentPath, name) = getParentAndChild(path)
        traverse(parentPath, createDirs = false)
            .delete(name)
    }

    override fun print() {
        root.print()
    }

    /**
     * Returns true if the [path] points to the root directory.
     */
    private fun isRoot(path: String): Boolean {
        return path == delimiter.toString()
    }

    /**
     * Traverse from the root directory along the [path]. The entire [path] is assumed to contain directories only.
     * Returns the [FsNode.Dir] of the last element in the path.
     * If [createDirs] is true, creates missing directories, otherwise throws an exception.
     * Throws an exception if a non-directory [FsNode] is encountered.
     */
    private fun traverse(path: String, createDirs: Boolean): FsNode.Dir {
        if (isRoot(path)) {
            return root
        }
        var node = root
        val action = if (createDirs) {
            FsNode.Dir::getOrCreateDir
        } else {
            FsNode.Dir::getDir
        }
        path.split(delimiter).drop(1).forEach { dir ->
            node = action(node, dir)
        }
        return node
    }

    /**
     * Separate the [path] into the path to the parent directory, and the name of the last element.
     * For example, `"/a/b/c"` will result in the pair: `"/a/b"` to `"c"`
     */
    private fun getParentAndChild(path: String): Pair<String, String> {
        val lastDelimiter = path.indexOfLast { it == delimiter }
        val parentDir = path.substring(0, lastDelimiter)
        val lastElement = path.substring(lastDelimiter + 1)

        return parentDir to lastElement
    }
}