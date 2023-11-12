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
         * Throws an [IllegalStateException] if a file exists with the same name.
         */
        fun getOrCreateDir(name: String): Directory {
            val child = children[name]
            if (child != null) {
                check(child is Directory) { "Not a directory: $name" }
                return child
            }
            val newDirectory = Directory(name)
            children[name] = newDirectory
            return newDirectory
        }

        /**
         * Get the directory with the given [name], throwing an [IllegalStateException] if it doesn't exist.
         * Throws an [IllegalStateException] if a file exists with the same name.
         */
        fun getDir(name: String): Directory {
            val child = children[name]
            check(child != null) { "No such directory: $name" }
            check(child is Directory) { "Not a directory: $name" }
            return child
        }

        /**
         * Get the file with the given [name], creating it if it doesn't exist.
         * Throws an [IllegalStateException] if a directory exists with the same name.
         */
        fun getOrCreateFile(name: String): File {
            val child = children[name]
            if (child != null) {
                check(child is File) { "Not a file: $name" }
                return child
            }
            val newFile = File(name)
            children[name] = newFile
            return newFile
        }

        /**
         * Get the file with the given [name], returning null if it doesn't exist.
         * Throws an [IllegalStateException] if a directory exists with the same name.
         */
        fun findFile(name: String): File? {
            val child = children[name] ?: return null
            check(child is File) { "Not a file: $name" }
            return child
        }

        /**
         * Delete the file or directory with the given [name], throwing an [IllegalStateException] if it doesn't exist.
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