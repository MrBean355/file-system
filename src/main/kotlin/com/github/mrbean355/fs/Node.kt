package com.github.mrbean355.fs

sealed class Node(
    val name: String,
) {

    abstract fun print(indent: String = "")

    class File(name: String) : Node(name) {

        var content: String = ""

        override fun print(indent: String) {
            println(indent + "File: $name -> \"$content\"")
        }
    }

    class Directory(
        name: String,
        private val children: MutableMap<String, Node> = mutableMapOf(),
    ) : Node(name) {

        override fun print(indent: String) {
            println(indent + "Dir: $name")
            children.forEach { (_, child) ->
                child.print("$indent  ")
            }
        }

        /**
         * Get the directory with the given [name], creating it if it doesn't exist.
         * Throws an exception if a file exists with the same name.
         */
        fun getOrCreateDir(name: String): Directory {
            return children.getOrPut(name) { Directory(name) } as Directory
        }

        /**
         * Get the directory with the given [name], throwing an exception if it doesn't exist.
         * Throws an exception if a file exists with the same name.
         */
        fun getDir(name: String): Directory {
            return children.getValue(name) as Directory
        }

        /**
         * Get the file with the given [name], creating it if it doesn't exist.
         * Throws an exception if a directory exists with the same name.
         */
        fun getOrCreateFile(name: String): File {
            return children.getOrPut(name) { File(name) } as File
        }

        /**
         * Get the file with the given [name], returning null if it doesn't exist.
         * Throws an exception if a directory exists with the same name.
         */
        fun findFile(name: String): File? {
            return children[name] as File?
        }

        /**
         * Delete the file or directory with the given [name], throwing an exception if it doesn't exist.
         */
        fun delete(name: String) {
            check(children.remove(name) != null) {
                "No such file or directory: $name"
            }
        }

        /**
         * Get the names of the contained files and directories, sorted alphabetically.
         */
        fun list(): List<String> {
            return children.keys.sorted()
        }
    }
}