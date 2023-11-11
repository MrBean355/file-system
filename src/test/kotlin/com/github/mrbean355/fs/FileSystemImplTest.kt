package com.github.mrbean355.fs

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FileSystemImplTest {
    private lateinit var root: FsNode.Dir
    private lateinit var fs: FileSystemImpl

    @BeforeEach
    fun setUp() {
        root = FsNode.Dir("/")
        fs = FileSystemImpl(root)
    }

    @Test
    fun testMkdir_WithRootPath_DoesNothing() {
        fs.mkdir("/")

        assertTrue(root.list().isEmpty())
    }

    @Test
    fun testMkdir_SingleLevel_CreatesDirectory() {
        fs.mkdir("/foo")

        root.verify("/", children = 1)
        root.getDir("foo").verify("foo")
    }

    @Test
    fun testMkdir_MultipleLevels_CreatesDirectoryStructure() {
        fs.mkdir("/foo/bar/baz")

        assertEquals(1, root.list().size)

        root.verify("/", children = 1)

        val foo = root.getDir("foo")
        foo.verify("foo", children = 1)

        val bar = foo.getDir("bar")
        bar.verify("bar", children = 1)

        val baz = bar.getDir("baz")
        baz.verify("baz")
    }

    @Test
    fun testMkdir_SingleExistingDir_DoesNotCreateNewDir() {
        val existing = root.getOrCreateDir("foo")

        fs.mkdir("/foo")

        root.verify("/", children = 1)
        root.getDir("foo").verify(existing)
    }

    @Test
    fun testMkdir_MultipleExistingDirs_DoesNotCreateNewDirs() {
        val foo = root.getOrCreateDir("foo")
        val bar = foo.getOrCreateDir("bar")
        val baz = bar.getOrCreateDir("baz")

        fs.mkdir("/foo/bar/baz")

        root.verify("/", children = 1)

        root.getDir("foo").verify(foo)
        foo.verify("foo", children = 1)

        foo.getDir("bar").verify(bar)
        bar.verify("bar", children = 1)

        bar.getDir("baz").verify(baz)
        baz.verify("baz")
    }

    @Test
    fun testMkdir_SomeExistingDirs_CreateOneNewDir() {
        val foo = root.getOrCreateDir("foo")
        val bar = foo.getOrCreateDir("bar")

        fs.mkdir("/foo/bar/baz")

        root.verify("/", children = 1)

        root.getDir("foo").verify(foo)
        foo.verify("foo", children = 1)

        foo.getDir("bar").verify(bar)
        bar.verify("bar", children = 1)

        bar.getDir("baz").verify("baz")
    }

    @Test
    fun testMkdir_ExistingFilePath_ThrowsException() {
        root.getOrCreateFile("test.txt")

        assertThrows<ClassCastException> {
            fs.mkdir("/test.txt")
        }
    }

    @Test
    fun testMkdir_ExistingNestedFilePath_ThrowsException() {
        root.getOrCreateDir("foo")
            .getOrCreateFile("test.txt")

        assertThrows<ClassCastException> {
            fs.mkdir("/foo/test.txt")
        }
    }

    private fun FsNode.Dir.verify(name: String, children: Int = 0) {
        assertEquals(name, this.name)
        assertEquals(children, this.list().size)
    }

    private fun FsNode.Dir.verify(expected: FsNode.Dir) {
        assertSame(expected, this)
    }
}