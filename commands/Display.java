/*
Command to display and edit file contents in a GUI.
Usage: IMPORTED Display FILE [filename]
@author Abir Haque
@version 1.0.3
@since CSVOL 0.8.0.
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

  public static void main(ArrayList<String> args, ArrayList<String> originalArgs) throws Exception
  {
    ArrayList<String> arguments = args;
    System.out.println("Please wait. Loading GUI . . . ");
    if ((arguments.get(2)).equals("FILE"))  
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
      columns++;
    }
    inEdit.close();
    rows = fileLines.size();

    JFrame frame = new JFrame(arguments.get(3));
    JTextField[][] sheet = new JTextField[columns][rows];

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

    JButton saveButton = new JButton("Exit");
    saveButton.setBounds(xCoord,0,100,20);

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
          if(rows<=2)
          {
            PrintWriter outEdit = new PrintWriter(new FileWriter((Main.currentFile).getName()));
            int rows = Display.rows;
            for (int i = 0; i < rows; i++)
            {
              int columns = Display.columns/2;
              for (int j = 0; j < columns; j++)
              {
                if((((sheet[i][j]).getText()).toString()).equals("") == false)
                {
                  if(j==0)
                  {
                    outEdit.print((((sheet[i][j]).getText()).toString()));
                  }
                  else
                  {
                    outEdit.print(Main.delimeter+(((sheet[i][j]).getText()).toString()));
                  }
                }
                else
                {
                  if(j==0)
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
          else
          {
            frame.dispose();
            disposed = true;
            System.out.println("Exited successfully.");
          }
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