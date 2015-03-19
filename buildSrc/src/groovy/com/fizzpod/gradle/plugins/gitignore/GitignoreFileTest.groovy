package com.fizzpod.gradle.plugins.gitignore

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class GitignoreFileTest {

	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	@Test
	public void testReadMissingGitignoreFile() {
		def contents = new GitignoreFile().getContents(folder.root);
		Assert.assertNull(contents);
	}
	
	@Test
	public void testReadEmptyGitignoreFile() {
		writeGitignore();
		String contents = new GitignoreFile().getContents(folder.root);
		Assert.assertNotNull(contents);
		Assert.assertEquals("", contents);
	}
	
	@Test
	public void testReadGitignoreFileWithContent() {
		writeGitignore("abc", "def");
		def contents = new GitignoreFile().getContents(folder.root);
		Assert.assertNotNull(contents);
		Assert.assertEquals("abc" + System.lineSeparator + "def" + System.lineSeparator, contents);
	}
	
	@Test
	public void writeGitignoreFile() {
		new GitignoreFile().writeContents(folder.getRoot(), ["123", "abc"]);
		String content = new File(folder.getRoot(), ".gitignore").text;
		Assert.assertEquals("123" + System.lineSeparator + "abc" + System.lineSeparator, content);
	}
	
	private void writeGitignore(String[] lines) {
		new File(folder.getRoot(), ".gitignore").withWriter { out ->
			lines.each { out.println it }
		}
	}
	
}
