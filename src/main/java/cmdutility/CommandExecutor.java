package cmdutility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommandExecutor
{
	private ApplicationContext context;
	public CommandExecutor()
	{
		context = new ClassPathXmlApplicationContext(
				getOS()+"command.xml");
	}
	
	private String getOS()
	{
		String os = System.getProperty("os.name");
		if (os.startsWith("Windows"))
		{
			os = "windows";
		}
		return os;
	}
	private HashSet<String> getPermissibleCommands()
	{
		String[] commandNamesOS = context.getBeanDefinitionNames();
		HashSet<String> commandSet = new HashSet<String>();

		for (int i = 0; i < commandNamesOS.length; i++)
		{
			String command = commandNamesOS[i];
			commandSet.add(command);
		}
		return commandSet;
	}
	
	private void printPermissibleCommands(HashSet<String> commandSet)
	{
		System.out.print("Enter Command. Valid commands are [");

		Iterator<String> iCmdSet = commandSet.iterator();
		int cmdSize = commandSet.size();
		int loopCnt = 0;
		while (iCmdSet.hasNext())
		{
			loopCnt++;
			System.out.print(iCmdSet.next());
			if(loopCnt < cmdSize)
			{
				System.out.print(", ");
			}
		}
		System.out.println("]");
	}
	
	private String getExecutableCommand(InputStream in)
	{
		Scanner scan = null;
		try
		{
			scan = new Scanner(in);
			String commandString = scan.next();
	
			Command command = (Command) context.getBean(commandString);
			System.out.println(command.getHelp());
			String execCommand = command.getName();
			List<String> arguments = command.getArguments();
			for (String arg : arguments)
			{
				System.out.println(arg);
				execCommand += " " + scan.next();
			}
			return execCommand;
		}
		finally
		{
			if(scan!=null)
			{
				scan.close();
			}
		}
	}
	
	private int executeCommand(String execCommand)
	{
		int exitVal = -1;
		System.out.println("Executing command " + execCommand);
		Runtime rt = Runtime.getRuntime();
		try
		{
			Process pr = rt.exec(execCommand);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null)
			{
				System.out.println(line);
			}
			exitVal = pr.waitFor();
			System.out.println("Exited with code " + exitVal);
	
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return exitVal;
	}
	public int run(InputStream in)
	{
		HashSet<String> commandSet = getPermissibleCommands();
		printPermissibleCommands(commandSet);
		String execCommand = getExecutableCommand(in);
		int exitCode = executeCommand(execCommand);
		System.out.println("Done");
		return exitCode;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		CommandExecutor cmdExec = new CommandExecutor();
		System.out.println("Exit Code :"+cmdExec.run(System.in));
	}
}
