package com.github.mrbean355.fs

sealed class FsNode(
    val name: String,
) {

    abstract fun print(indent: String = "")

    class File(name: String) : FsNode(name) {
        var content: String = ""

        override fun print(indent: String) {
            println(indent + "File: $name -> $content")
        }
    }

    class Dir(name: String) : FsNode(name) {
        private val children: MutableMap<String, FsNode> = mutableMapOf()

        override fun print(indent: String) {
            println(indent + "Dir: $name")
            children.forEach { (_, child) ->
                child.print("$indent  ")
            }
        }

        fun getOrCreateDir(name: String): Dir {
            return children.getOrPut(name) { Dir(name) } as Dir
        }

        fun getDir(name: String): Dir {
            return children.getValue(name) as Dir
        }

        fun getOrCreateFile(name: String): File {
            return children.getOrPut(name) { File(name) } as File
        }

        fun getFile(name: String): File {
            return children.getValue(name) as File
        }

        fun delete(name: String) {
            check(children.remove(name) != null) {
                "No such file or directory: $name"
            }
        }

        fun list(): List<String> {
            return children.keys.toList()
        }
    }
}