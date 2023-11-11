package com.github.mrbean355.fs

private const val DELIMITER = "/"

class FileSystemImpl(
    private val root: FsNode.Dir = FsNode.Dir("/"),
) : FileSystem {

    override fun mkdir(path: String) {
        if (path == DELIMITER) {
            return
        }
        traverse(path, createDirs = true)
    }

    override fun addContentToFile(path: String, content: String) {
        if (path == DELIMITER) {
            error("Cannot add content to the root directory")
        }
        val (parentPath, name) = getParentAndChild(path)
        traverse(parentPath, createDirs = true)
            .getOrCreateFile(name).content += content
    }

    override fun readContentFromFile(path: String): String {
        if (path == DELIMITER) {
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
        if (path == DELIMITER) {
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
     * Traverse from the root directory along the [path]. The entire [path] is assumed to contain directories only.
     * Returns the [FsNode.Dir] of the last element in the path.
     * If [createDirs] is true, creates missing directories, otherwise throws an exception.
     * Throws an exception if a non-directory [FsNode] is encountered.
     */
    private fun traverse(path: String, createDirs: Boolean): FsNode.Dir {
        if (path == "/") {
            return root
        }
        var node = root
        val action = if (createDirs) {
            FsNode.Dir::getOrCreateDir
        } else {
            FsNode.Dir::getDir
        }
        path.split(DELIMITER).drop(1).forEach { dir ->
            node = action(node, dir)
        }
        return node
    }

    /**
     * Separate the [path] into the path to the parent directory, and the name of the last element.
     * For example, `"/a/b/c"` will result in the pair: `"/a/b"` to `"c"`
     */
    private fun getParentAndChild(path: String): Pair<String, String> {
        val lastDelimiter = path.indexOfLast { it == '/' }
        val parentDir = path.substring(0, lastDelimiter)
        val lastElement = path.substring(lastDelimiter + 1)

        return parentDir to lastElement
    }
}