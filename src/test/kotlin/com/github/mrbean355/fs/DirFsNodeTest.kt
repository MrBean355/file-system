package com.github.mrbean355.fs

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DirFsNodeTest {
    private lateinit var children: MutableMap<String, FsNode>
    private lateinit var dir: FsNode.Dir

    @BeforeEach
    fun setUp() {
        children = mutableMapOf()
        dir = FsNode.Dir("test", children)
    }

    @Test
    fun testGetOrCreateDir_DoesNotExist_CreatesNewDir() {
        dir.getOrCreateDir("foo")

        val result = children["foo"]
        assertNotNull(result)
        assertTrue(result is FsNode.Dir)
        assertEquals("foo", result.name)
    }

    @Test
    fun testGetOrCreateDir_DirExists_ReturnsSameDir() {
        val existing = FsNode.Dir("foo")
        children["foo"] = existing

        dir.getOrCreateDir("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testGetOrCreateDir_FileExists_ThrowsException() {
        children["foo"] = FsNode.File("foo")

        assertThrows<ClassCastException> {
            dir.getOrCreateDir("foo")
        }
    }

    @Test
    fun testGetDir_DoesNotExist_ThrowsException() {
        assertThrows<NoSuchElementException> {
            dir.getDir("foo")
        }
    }

    @Test
    fun testGetDir_DirExists_ReturnsSameDir() {
        val existing = FsNode.Dir("foo")
        children["foo"] = existing

        dir.getDir("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testGetDir_FileExists_ThrowsException() {
        children["foo"] = FsNode.File("foo")

        assertThrows<ClassCastException> {
            dir.getDir("foo")
        }
    }

    @Test
    fun testGetOrCreateFile_DoesNotExist_CreatesNewFile() {
        dir.getOrCreateFile("foo")

        val result = children["foo"]
        assertNotNull(result)
        assertTrue(result is FsNode.File)
        assertEquals("foo", result.name)
        assertEquals("", result.content)
    }

    @Test
    fun testGetOrCreateFile_FileExists_ReturnsSameFile() {
        val existing = FsNode.File("foo")
        children["foo"] = existing

        dir.getOrCreateFile("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testGetOrCreateFile_DirExists_ThrowsException() {
        children["foo"] = FsNode.Dir("foo")

        assertThrows<ClassCastException> {
            dir.getOrCreateFile("foo")
        }
    }

    @Test
    fun testGetFile_DoesNotExist_ThrowsException() {
        assertThrows<NoSuchElementException> {
            dir.getFile("foo")
        }
    }

    @Test
    fun testGetFile_FileExists_ReturnsSameFile() {
        val existing = FsNode.File("foo")
        children["foo"] = existing

        dir.getFile("foo")

        assertSame(existing, children["foo"])
    }

    @Test
    fun testGetFile_DirExists_ThrowsException() {
        children["foo"] = FsNode.Dir("foo")

        assertThrows<ClassCastException> {
            dir.getFile("foo")
        }
    }

    @Test
    fun testDelete_DirExists_RemovesFromChildren() {
        children["foo"] = FsNode.Dir("foo")

        dir.delete("foo")

        assertTrue("foo" !in children)
    }

    @Test
    fun testDelete_FileExists_RemovesFromChildren() {
        children["foo"] = FsNode.File("foo")

        dir.delete("foo")

        assertTrue("foo" !in children)
    }

    @Test
    fun testDelete_ChildDoesNotExist_ThrowsException() {
        assertThrows<IllegalStateException> {
            dir.delete("foo")
        }
    }

    @Test
    fun testList_ReturnsSortedChildrenNames() {
        children["qwerty"] = FsNode.File("qwerty")
        children["foo"] = FsNode.File("foo")
        children["bar"] = FsNode.Dir("bar")

        val result = dir.list()

        assertEquals(3, result.size)
        assertEquals("bar", result[0])
        assertEquals("foo", result[1])
        assertEquals("qwerty", result[2])
    }
}