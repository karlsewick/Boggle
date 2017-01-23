import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

// DO NOT IMPORT JAVA.LANG

public class Boggle
{
    JFrame window;  // the main window which contains everything
    Container content ;
    //JTextField expression;
    JButton[][] chars = new JButton[10][10];
    JButton start,end;
    JTextArea words;
    //String[] opCodes = { "+", "-", "*", "/" };
    //string infileName = args[0];
    String wordPrint = "";
    int count = 0;
    long startTime = System.nanoTime();
    // row and col will be on the 2nd line of input file;
    //String infileName = args[0];
    
    public Boggle(String infileName) throws Exception
    {
        String[][] board = loadBoard(infileName);
        int rows = board.length; int cols = board[0].length;
        
        window = new JFrame ("Boggle");
        content = window.getContentPane();
        content.setLayout(new GridLayout(3,1));
        ButtonListener listener = new ButtonListener();
        
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(rows, cols));//4 row, 4 col
        
        for (int i =0; i<rows;i++)
        {
            for(int j =0; j<cols;j++)
            {
                chars[i][j] = new JButton("" + board[i][j]);
                chars[i][j].setBackground(Color.WHITE);
                chars[i][j].setOpaque(true);
                boardPanel.add(chars[i][j]);
              //  chars[i+j].addActionListener(listener);
                
            }
        }
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new GridLayout(1,1));
        
        words = new JTextArea();
        words.setFont(new Font("verdana",Font.BOLD, 10));
        words.setText("");
        
        midPanel.add(words);
        
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1,1));
        
        start = new JButton("Start with " + infileName);
        start.setFont(new Font("verdana", Font.BOLD, 24));
        start.addActionListener(listener);
        
        end = new JButton("End");
        end.setFont(new Font("verdana", Font.BOLD, 24));
        end.addActionListener(listener);
        
        bottomPanel.add(start);
        bottomPanel.add(end);
        
       // chars[rows + cols -2].setBackground(Color.BLACK);

        content.add(boardPanel);
        content.add(midPanel);
        content.add(bottomPanel);
        
        window.setSize(720,900);
        window.setVisible(true);
        
           }
    public void highlight(int r, int c)
    {
        chars[r][c].setBackground(Color.BLACK);
    }
    public  void unHighlight(int r, int c)
    {
        chars[r][c].setBackground(Color.WHITE);
    }

    
    
    
    public static void main(String[] args) throws Exception
	{
        
        //long startTime = System.nanoTime();
		 // row and col will be on the 2nd line of input file;
        String infileName = args[0];
        new Boggle(infileName);
        
        

		// YOUR CODE HERE. DECLARE WHATEVER OBJECTS AND VARIABLES NEEDED
		// CALL YOUR METHOD(s) TO PRINT ALL ESCAPE wordS

	} // END MAIN
    private void run(String infileName) throws Exception
    {
        String [][] board = loadBoard(infileName);
        TreeSet<String> dictionary = new TreeSet<String>();
        BufferedReader dictionaryFile = new BufferedReader(new FileReader("dictionary.txt"));
        
        while (dictionaryFile.ready())
        {
            dictionary.add(dictionaryFile.readLine());
            
        }
        dictionaryFile.close();
        
        String word = "";
        for(int i = 0; i<board.length;i++)
        {   for(int j =0; j<board[i].length;j++)
            {
                dfs(i,j,word,board, dictionary);
                //chars[i][j].setBackground(Color.WHITE);
            }
        }
        long endTime = System.nanoTime();
        double duration = (double)(endTime - startTime);
        System.out.println(duration/1000000000);
    }

    private void dfs(int r,int c,String word ,String[][] board, TreeSet<String>dictionary)
    {
        word += board[r][c];
        int rows = board.length;
        int cols = board[0].length;
        String nWord = new String(word);    //Heuristic
        for(int i = 0; i < rows*cols;i++)   
            nWord+= "z";
        
       // chars[r][c].setBackground(Color.BLACK);
        //chars[r][c].setOpaque(true);
      /*  try {
            Thread.sleep(1);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/
        
        if (dictionary.contains(word) && word.length() >=3)
        {
            System.out.println(word);
            dictionary.remove(word);
            //chars[r][c].setBackground(Color.WHITE);
            wordPrint = wordPrint + " " + word;
            if (count > 20)
            {
                wordPrint += " \n ";
                count = 0;
                
            }
            words.setText(wordPrint);
            count++;
        }
        if(dictionary.subSet(word,nWord).size() == 0)//Heuristic
            return;
            
        
        if(isSafe(r-1,c,board))
        {
           chars[r][c].setBackground(Color.BLACK);
           //chars[r][c].setOpaque(true);
          /*  try {
                Thread.sleep(1);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/

            board[r][c] = board[r][c].toUpperCase() ;
           
            dfs(r-1, c, word, board, dictionary);
            board[r][c] = board[r][c].toLowerCase();
            

            //chars[r][c].setBackground(Color.WHITE);
          // chars[r][c].setOpaque(true);

        }
        if(isSafe(r-1,c+1,board))
        {
            board[r][c] = board[r][c].toUpperCase();
           chars[r][c].setBackground(Color.BLACK);
           //chars[r][c].setOpaque(true);
           /* try {
                Thread.sleep(1);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
            dfs(r-1,c+1,word, board,dictionary);
            
            board[r][c] = board[r][c].toLowerCase();
           // chars[r][c].setBackground(Color.WHITE);
           // chars[r][c].setOpaque(true);

        }
        if(isSafe(r,c+1,board))
        {
            board[r][c] =  board[r][c].toUpperCase();
            chars[r][c].setBackground(Color.BLACK);
           // chars[r][c].setOpaque(true);
            
            dfs(r,c+1,word,board, dictionary);
            /*try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
            board[r][c] = board[r][c].toLowerCase();
           // chars[r][c].setBackground(Color.WHITE);
           // chars[r][c].setOpaque(true);

        }
        if(isSafe(r+1,c+1,board))
        {
            board[r][c] = board[r][c].toUpperCase();
            chars[r][c].setBackground(Color.BLACK);
           // chars[r][c].setOpaque(true);
            dfs(r+1, c+1,word, board, dictionary);
            board[r][c] = board[r][c].toLowerCase();
          /*  try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
            //chars[r][c].setBackground(Color.WHITE);
          //  chars[r][c].setOpaque(true);

        }
        if(isSafe(r+1,c,board))
        {
            board[r][c] = board[r][c].toUpperCase();
            chars[r][c].setBackground(Color.BLACK);
          //  chars[r][c].setOpaque(true);
            
            dfs(r+1,c,word, board,dictionary);
            board[r][c] = board[r][c].toLowerCase();
            /*try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
           // chars[r][c].setBackground(Color.WHITE);
          //  chars[r][c].setOpaque(true);

        }
        if(isSafe(r+1,c-1,board))
        {
            board[r][c] = board[r][c].toUpperCase();
            chars[r][c].setBackground(Color.BLACK);
           // chars[r][c].setOpaque(true);
            
            dfs(r+1,c-1,word, board,dictionary);
            board[r][c] = board[r][c].toLowerCase();
           /* try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
          //  chars[r][c].setBackground(Color.WHITE);
            //chars[r][c].setOpaque(true);

        }
        if(isSafe(r,c-1,board))
        {
            board[r][c] = board[r][c].toUpperCase();
             chars[r][c].setBackground(Color.BLACK);
          //  chars[r][c].setOpaque(true);
            
            dfs(r,c-1,word, board, dictionary);
            board[r][c] = board[r][c].toLowerCase();
           /* try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
          //  chars[r][c].setBackground(Color.WHITE);
           // chars[r][c].setOpaque(true);

        }
        if(isSafe(r-1,c-1,board))
        {
            board[r][c] = board[r][c].toUpperCase();
            chars[r][c].setBackground(Color.BLACK);
          //  chars[r][c].setOpaque(true);
            
            dfs(r-1,c-1,word, board, dictionary);
            board[r][c] = board[r][c].toLowerCase();
        /*    try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
          //  chars[r][c].setBackground(Color.WHITE);
          //  chars[r][c].setOpaque(true);

        }
         //chars[r][c].setBackground(Color.YELLOW);
        //chars[r][c].setOpaque(true);
        
        //chars[r][c].setBackground(Color.WHITE);

        return;
        
    }
    private static boolean isSafe(int row, int col, String[][] board)
    {
        boolean safe;
        try
        {
            safe = Character.isLowerCase(board[row][col].charAt(0));
        }
        catch (Exception e)
        {
            return false;
        }
        
        return safe;
    }
    
    
    
    
    private static String[][] loadBoard( String infileName) throws Exception
	{
		Scanner infile = new Scanner( new File(infileName) );
		  		// ASSUME A SQUARE GRID
        int rows=0;int cols=0;
        
            rows=infile.nextInt();
            cols = rows;
            String[][] board = new String[rows][cols];
            for(int r = 0; r < rows ; r++)
            {   for(int c = 0; c < cols; c++)
                board[r][c] = infile.next();
            }
        
            
        
		infile.close();
        return board;
    }
    
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                Component whichButton = (Component) e.getSource();
                if(whichButton == end)
                    System.exit(0);
                run(start.getText().substring(11));
            }
            catch(Exception a)
            {
            
            }
        }
            
        
    }
}
