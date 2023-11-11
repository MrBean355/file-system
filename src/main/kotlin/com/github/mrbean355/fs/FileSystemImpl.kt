package com.github.mrbean355.fs

private const val DELIMITER = "/"

class FileSystemImpl : FileSystem {
    private val root = FsNode.Dir("/")

    override fun mkdir(path: String) {
        if (path == DELIMITER) {
            return
        }
        var node = root
        path.split(DELIMITER).drop(1).forEach {
            node = node.getOrCreateDir(it)
        }
    }

    override fun addContentToFile(path: String, content: String) {
        val lastDelimiter = path.indexOfLast { it == '/' }
        val parentDir = path.substring(0, lastDelimiter)
        val name = path.substring(lastDelimiter + 1)

        mkdir(parentDir)

        var node = root
        parentDir.split(DELIMITER).drop(1).forEach {
            node = node.getDir(it)
        }
        node.getOrCreateFile(name).content += content
    }

    override fun readContentFromFile(path: String): String {
        val lastDelimiter = path.indexOfLast { it == '/' }
        val parentDir = path.substring(0, lastDelimiter)
        val name = path.substring(lastDelimiter + 1)

        var node = root
        parentDir.split(DELIMITER).drop(1).forEach {
            node = node.getDir(it)
        }
        return node.getFile(name).content
    }

    override fun ls(path: String): List<String> {
        if (path == DELIMITER) {
            return root.list()
        }
        var node = root
        path.split(DELIMITER).drop(1).forEach {
            node = node.getDir(it)
        }
        return node.list()
    }

    override fun rm(path: String) {
        if (path == DELIMITER) {
            error("Cannot delete the root directory!")
        }
        val lastDelimiter = path.indexOfLast { it == '/' }
        val parentDir = path.substring(0, lastDelimiter)
        val name = path.substring(lastDelimiter + 1)

        var node = root
        parentDir.split(DELIMITER).drop(1).forEach {
            node = node.getDir(it)
        }
        node.delete(name)
    }

    override fun print() {
        root.print()
    }
}