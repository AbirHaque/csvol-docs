/*
Command to set custom delimiters.
Usage: IMPORTED Delimiter [character]
@author Abir Haque
@version 1.0.1
@since CSVOL 0.8.0.
*/
import java.util.*;
public class Delimiter
{
  public static void main(ArrayList<String> args, ArrayList<String> originalArgs) throws Exception
  {
    ArrayList<String> arguments = originalArgs;
    fallBack(arguments);
  }
  public static void fallBack(ArrayList<String> args) throws Exception
  {
    ArrayList<String> arguments = args;
    Main.delimeter = arguments.get(2);
  }
}