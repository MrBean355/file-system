package com.github.mrbean355.fs

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class DirectoryTest {
    private lateinit var children: MutableMap<String, Node>
    private lateinit var directory: Node.Directory

    @BeforeEach
    fun setUp() {
        children = mutableMapOf()
        directory = Node.Directory("test", children)
    }

    @Test
    fun testGetOrCreateDir_DoesNotExist_CreatesNewDir() {
        directory.getOrCreateDir("foo")

        val result = children["foo"]
        assertNotNull(result)
        assertTrue(result is Node.Directory)
        assertEquals("foo", result.name)
    }

    @Test
    fun testGetOrCreateDir_DirExists_ReturnsSameDir() {
        val existing = Node.Directory("foo")
        children["foo"] = existing

        directory.getOrCreateDir("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testGetOrCreateDir_FileExists_ThrowsException() {
        children["foo"] = Node.File("foo")

        assertThrows<IllegalStateException> {
            directory.getOrCreateDir("foo")
        }
    }

    @Test
    fun testGetDir_DoesNotExist_ThrowsException() {
        assertThrows<IllegalStateException> {
            directory.getDir("foo")
        }
    }

    @Test
    fun testGetDir_DirExists_ReturnsSameDir() {
        val existing = Node.Directory("foo")
        children["foo"] = existing

        directory.getDir("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testGetDir_FileExists_ThrowsException() {
        children["foo"] = Node.File("foo")

        assertThrows<IllegalStateException> {
            directory.getDir("foo")
        }
    }

    @Test
    fun testGetOrCreateFile_DoesNotExist_CreatesNewFile() {
        directory.getOrCreateFile("foo")

        val result = children["foo"]
        assertNotNull(result)
        assertTrue(result is Node.File)
        assertEquals("foo", result.name)
        assertEquals("", result.content)
    }

    @Test
    fun testGetOrCreateFile_FileExists_ReturnsSameFile() {
        val existing = Node.File("foo")
        children["foo"] = existing

        directory.getOrCreateFile("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testGetOrCreateFile_DirExists_ThrowsException() {
        children["foo"] = Node.Directory("foo")

        assertThrows<IllegalStateException> {
            directory.getOrCreateFile("foo")
        }
    }

    @Test
    fun testFindFile_DoesNotExist_ReturnsNull() {
        val result = directory.findFile("foo")

        assertNull(result)
    }

    @Test
    fun testFindFile_FileExists_ReturnsSameFile() {
        val existing = Node.File("foo")
        children["foo"] = existing

        directory.findFile("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testFindFile_DirExists_ThrowsException() {
        children["foo"] = Node.Directory("foo")

        assertThrows<IllegalStateException> {
            directory.findFile("foo")
        }
    }

    @Test
    fun testDelete_DirExists_RemovesFromChildren() {
        children["foo"] = Node.Directory("foo")

        directory.delete("foo")

        assertTrue("foo" !in children)
    }

    @Test
    fun testDelete_FileExists_RemovesFromChildren() {
        children["foo"] = Node.File("foo")

        directory.delete("foo")

        assertTrue("foo" !in children)
    }

    @Test
    fun testDelete_ChildDoesNotExist_ThrowsException() {
        assertThrows<IllegalStateException> {
            directory.delete("foo")
        }
    }

    @Test
    fun testList_ReturnsSortedChildrenNames() {
        children["qwerty"] = Node.File("qwerty")
        children["foo"] = Node.File("foo")
        children["bar"] = Node.Directory("bar")

        val result = directory.list()

        assertEquals(3, result.size)
        assertEquals("bar", result[0])
        assertEquals("foo", result[1])
        assertEquals("qwerty", result[2])
    }
}