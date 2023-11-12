package com.github.mrbean355.fs

sealed class FsNode(
    val name: String,
) {

    abstract fun print(indent: String = "")

    class File(name: String) : FsNode(name) {

        var content: String = ""

        override fun print(indent: String) {
            println(indent + "File: $name -> \"$content\"¡¡")
        }
    }

    class Dir(
        name: String,
        private val children: MutableMap<String, FsNode> = mutableMapOf(),
    ) : FsNode(name) {

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
        fun getOrCreateDir(name: String): Dir {
            return children.getOrPut(name) { Dir(name) } as Dir
        }

        /**
         * Get the directory with the given [name], throwing an exception if it doesn't exist.
         * Throws an exception if a file exists with the same name.
         */
        fun getDir(name: String): Dir {
            return children.getValue(name) as Dir
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