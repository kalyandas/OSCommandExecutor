package cmdutility;

import java.util.List;


public class Command
{
	private String name;
	private String help;
	private List<String> arguments;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getHelp()
	{
		return help;
	}
	public void setHelp(String help)
	{
		this.help = help;
	}
	public List<String> getArguments()
	{
		return arguments;
	}
	public void setArguments(List<String> arguments)
	{
		this.arguments = arguments;
	}


}
