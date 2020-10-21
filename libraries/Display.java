/*
Library to display and edit file contents in a GUI.
Usage: library Display file [filename]
@author Abir Haque
@version 1.0.1
@since CSVOL 21_0.0.0
*/
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Display
{
  public static BufferedReader inEdit;
  public static int columns;
  public static int rows;
  public static boolean disposed = false;

  public static void main(ArrayList<String> args, ArrayList<String> originalArgs) 

throws Exception
  {
    ArrayList<String> arguments = args;
    System.out.println("Please wait. Loading GUI . . . ");
    if ((arguments.get(2)).equalsIgnoreCase("FILE"))  
    {
      file(arguments);
    }
  }
  public static void file(ArrayList<String> args) throws Exception
  {
    ArrayList<String> arguments = args;
    Main.currentFile = new File((arguments.get(3)) + ".csv");

    inEdit = new BufferedReader(new FileReader((Main.currentFile).getName())); 
    ArrayList<String> fileLines = new ArrayList<String>();
    columns = 0;
    rows = 0;
    while(inEdit.ready())
    {
      fileLines.add(inEdit.readLine());
    }
    inEdit.close();

    String column_names = fileLines.get(0);
    StringTokenizer tokenized_column_names= new StringTokenizer(column_names, 

Main.delimeter);
    while(tokenized_column_names.hasMoreTokens())
    {
	columns++;
	tokenized_column_names.nextToken();
    }
    columns = columns;
    rows = fileLines.size();

    JFrame frame = new JFrame(arguments.get(3));
    JTextField[][] sheet = new JTextField[rows][columns];
    int xCoord = 0;
    int yCoord = 0;

    for (int i = 0; i < rows; i++)
    {
      String rowString = fileLines.get(i);
      StringTokenizer rowItems = new StringTokenizer(rowString, Main.delimeter);
      xCoord = 0;
      int j = 0;
      while(rowItems.hasMoreTokens())
      {
        sheet[i][j] = new JTextField();
        sheet[i][j].setBounds(xCoord,yCoord,100,20);
        sheet[i][j].setText(rowItems.nextToken());
        frame.add(sheet[i][j]);
        xCoord+=100;
        j++;
      }
      yCoord+=20;
    }
    JButton saveButton = new JButton("Save");
    saveButton.setBounds(0,yCoord,100,20);

    frame.add(saveButton);

    frame.setSize(xCoord+100,yCoord+100);
    frame.setLayout(null);
    frame.setVisible(true);

    Terminal.done();

    saveButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
            PrintWriter outEdit = new PrintWriter(new FileWriter

((Main.currentFile).getName()));
            int rows = Display.rows;
            for (int k = 0; k < rows; k++)
            {
              int columns = Display.columns;
              for (int l = 0; l < columns; l++)
              {
                if((((sheet[k][l]).getText()).toString()).equals("") == false)
                {
                  if(l==0)
                  {
                    outEdit.print((((sheet[k][l]).getText()).toString()));
                  }
                  else
                  {
                    outEdit.print(Main.delimeter+(((sheet[k][l]).getText()).toString

()));
                  }
                }
                else
                {
                  if(l==0)
                  {
                    outEdit.print("0");
                  }
                  else
                  {
                    outEdit.print(Main.delimeter+"0");
                  }
                }
              }
              outEdit.println();
            }
            outEdit.close();
            frame.dispose();
            disposed = true;
            System.out.println("Exited successfully.");
        }
        catch(Exception x)
        {
          System.out.println(x);
          System.out.println("Error Column: " + Display.columns);
          System.out.println("Error Row: " + Display.rows);
        }
      }
    });
    while(disposed == false)
    {
      System.out.print("");
    }
    Display.columns = 0;
    Display.rows = 0;
    Display.disposed = false;
  }
}