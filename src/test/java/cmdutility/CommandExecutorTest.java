package cmdutility;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cmdutility.CommandExecutor;

public class CommandExecutorTest
{
	static CommandExecutor cmdExec;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new File("C:\\TestCommandExecutor").mkdir();
		new File("C:\\TestCommandExecutor\\input").mkdir();
		new File("C:\\TestCommandExecutor\\output").mkdir();
		new File("C:\\TestCommandExecutor\\input\\testfile.txt").createNewFile();
		cmdExec = new CommandExecutor();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		new File("C:\\TestCommandExecutor\\input\\testfile.txt").delete();
		new File("C:\\TestCommandExecutor\\output\\testfile.txt").delete();
		new File("C:\\TestCommandExecutor\\input").delete();
		new File("C:\\TestCommandExecutor\\output").delete();
		new File("C:\\TestCommandExecutor").delete();
	}

	@Test
	public void testListCommand()
	{
		int exitCode = cmdExec.run(new ByteArrayInputStream("list\nC:\\TestCommandExecutor\\input".getBytes()));
		assertEquals("Failed to list directory", 0, exitCode);
	}
	
	@Test
	public void testMoveCommand()
	{
		int exitCode = cmdExec.run(new ByteArrayInputStream("move\nC:\\TestCommandExecutor\\input\\testfile.txt\nC:\\TestCommandExecutor\\output".getBytes()));
		if(exitCode!=0)
		{
			fail("Failed to move file. Exit code <> 0");
			return;
		}
		//check the existence of file in output directory
		File out = new File("C:\\TestCommandExecutor\\output\\testfile.txt");
		if(!out.exists())
		{
			fail("Failed to move file. File does not exists in output directory");
			return;
		}
		//check non-existence of file in input directory
		File in = new File("C:\\TestCommandExecutor\\input\\testfile.txt");
		if(in.exists())
		{
			fail("Failed to move file. File exists in input directory");
			return;
		}
	}

	@Test
	public void testCopyCommand()
	{
		int exitCode = cmdExec.run(new ByteArrayInputStream("copy\nC:\\TestCommandExecutor\\output\\testfile.txt\nC:\\TestCommandExecutor\\input".getBytes()));
		if(exitCode!=0)
		{
			fail("Failed to copy file. Exit code <> 0");
			return;
		}
		//check existence of file in input directory
		File in = new File("C:\\TestCommandExecutor\\input\\testfile.txt");
		if(!in.exists())
		{
			fail("Failed to copy file. File exists in input directory");
			return;
		}
	}
}
