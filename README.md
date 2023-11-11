# File System Solution

My Kotlin solution for simulating a basic file system.

## Question

You are tasked with implementing a data structure that simulates a simple file system. The system should be able to
handle files and directories, allowing you to navigate, create, and delete them. You should create a class
called `FileSystem`.

Your tasks are as follows:

1. Implement a `FileSystem` class with the following methods:
    - `mkdir(path: string)`: Create a new directory at the given path.
    - `addContentToFile(filePath: string, content: string)`: Add content to the file at the given path. If the file does
      not exist, it should be created.
    - `readContentFromFile(filePath: string)`: Read and return the content of the file at the given path. If the file
      does not exist, return an empty string.
    - `ls(path: string)`: List the files and directories at the given path. The output should be a string array in
      lexicographical order.
    - `rm(path: string)`: Remove the file or directory at the given path.

2. You need to maintain the structure of directories and files in your data structure. The path is a string with a '/'
   delimiter, where each component represents a directory or file name. For example, "/root/folder/file.txt" represents
   a file named "file.txt" in a folder named "folder" under the root directory.

3. You should support creating, deleting, and reading files and directories within the system.

4. You should also handle cases where directories or files have the same name.

5. You need to ensure that your implementation is efficient, as the file system can potentially contain a large number
   of directories and files.

Here's a template to get you started:

```typescript
class FileSystem {
    constructor() {
        // Initialize your data structure here
    }

    mkdir(path: string): void {
        // Implement mkdir method
    }

    addContentToFile(filePath: string, content: string): void {
        // Implement addContentToFile method
    }

    readContentFromFile(filePath: string): string {
        // Implement readContentFromFile method
    }

    ls(path: string): string[] {
        // Implement ls method
    }

    rm(path: string): void {
        // Implement rm method
    }
}

// Example usage:
const fs = new FileSystem();
fs.mkdir("/root");
fs.addContentToFile("/root/file.txt", "Hello, world!");
console.log(fs.readContentFromFile("/root/file.txt")); // Should print "Hello, world!"
fs.mkdir("/root/folder");
fs.ls("/root"); // Should list files and directories under /root
fs.rm("/root/file.txt");
fs.ls("/root"); // Should list files and directories under /root after removing file.txt
```

You need to complete the `FileSystem` class by implementing the required methods, ensuring they work correctly and
efficiently.