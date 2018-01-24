import java.util.*;
import java.io.*;
public class helperAddBook
{
    public void addingTheEntry( File file, String fileToAdd, Scanner keyboard ) throws Exception
    {
        if( file.exists() == true )
        {
            FileWriter fileWriter = new FileWriter( fileToAdd, true );// to append the data to the end of the file
            PrintWriter printWriter = new PrintWriter( fileWriter );

            Scanner scan = new Scanner( fileToAdd );

            writeToFile( keyboard, printWriter );//so we do not have to repeat whole porcess

            closeWriter( printWriter, fileToAdd );
        }
        else
        {
            System.out.print( "\nThere is no such file. Please try again.\n" );
        }
    }
    
    public void movingAnEntry( Scanner keyboard, int i, ArrayList<addBook> book ) throws Exception
    {
         System.out.println( "\nEnter the file name to which you want move: " );
         String whereToMove = keyboard.nextLine();
            
         FileWriter fileWriter = new FileWriter( whereToMove, true );// to append the data to the end of the file
         PrintWriter printWriter = new PrintWriter( fileWriter );
            
         printWriter.println( book.get(i).firstName );
         printWriter.println( book.get(i).lastName ); 
         printWriter.println( book.get(i).phone );
         printWriter.println( book.get(i).address );
         printWriter.println( book.get(i).email ); 
            
         printWriter.println();
            
         closeWriter( printWriter, whereToMove );
    }
    
    public void writeToFile( Scanner keyboard, PrintWriter printWriter )
    {
        String[] arr = takeEntries( keyboard );//returns an array by taking entries
            
        printWriter.println( arr[0] );
        printWriter.println( arr[1] ); 
        printWriter.println( arr[2] );
        printWriter.println( arr[3] );
        printWriter.println( arr[4] ); 
            
        printWriter.println();
    }
    
    public String[] takeEntries( Scanner keyboard )
    {
        String[] arr = new String[5];
        
        System.out.print( "\nEnter First Name: " );
        arr[0] = keyboard.nextLine();
        System.out.println();
            
        System.out.print( "Enter Last Name: " );
        arr[1] = keyboard.nextLine();
        System.out.println();
            
        System.out.print( "Enter Phone No: " );
        arr[2] = keyboard.nextLine();
        System.out.println();
             
        System.out.print( "Enter Address: " );
        arr[3] = keyboard.nextLine();
        System.out.println();
            
        System.out.print( "Enter Email Id: " );
        arr[4] = keyboard.nextLine();
        System.out.println();
            
        return arr;
    }
    
    public void toReadFromFile( Scanner scan, ArrayList<addBook> book )
    { 
        book.add( new addBook( scan.nextLine(), scan.nextLine(), scan.nextLine(), scan.nextLine(), scan.nextLine() ) );
        scan.nextLine();
    }
    
    public void closeWriter( PrintWriter printWriter, String fileName )
    {
        printWriter.close();
        System.out.println( "\nDone writing to " + fileName );
        System.out.print( "\nFile Saved.\n" );
    }
    
    public void menuItem()
    {
        System.out.println("---------------------------------------------------------------------" );
        System.out.println("|                                MENU                               |" );
        System.out.println("---------------------------------------------------------------------\n" );
        System.out.println("1) Load from file");
        System.out.println("2) Save to a file");
        System.out.println("3) Add an entry");
        System.out.println("4) Remove an entry");
        System.out.println("5) Edit an existing entry");
        System.out.println("6) Sort the address book");
        System.out.println("7) Search for a specific entry");
        System.out.println("8) Move an entry to another file");
        System.out.println("9) Quit");
    }
    
    public String toGetFileName( Scanner keyboard )
    {
        System.out.print("\nEnter name of the file: " );
        String fileName = keyboard.next();
        keyboard.nextLine();
        
        return fileName;
    }
    
    public void writingRecordToFile( File file, ArrayList<addBook> book ) throws Exception
    {
        FileWriter fileWriter = new FileWriter( file );
        PrintWriter printWriter = new PrintWriter( fileWriter );
            
        //to print to the file after the deletion of the entry.
        for( int i = 0; i < book.size(); i++ )
        {
             printWriter.println( book.get(i).firstName );
             printWriter.println( book.get(i).lastName );
             printWriter.println( book.get(i).phone );
             printWriter.println( book.get(i).email );
             printWriter.println( book.get(i).address );
             printWriter.println();
        }
        
        printWriter.close();
    }
    //using quicksort to soryt in ascending or descending ordeer
    public void quickSort( ArrayList<addBook> book, int low, int high, String sortBy, String sortOrder )
    {
        //check for empty or null array
        if (book == null || book.size() == 0){
            return;
        }
         
        if (low >= high){
            return;
        }
 
        //Get the pivot element from the middle of the list
        int middle = low + (high - low) / 2;
        addBook pivot = book.get(middle);
        
        swap( book, middle, high );//make middle element the last one

        // make left < pivot and right > pivot
        int i = low, j = high;
        while ( i <= j )
        {
            //to sort in ascending order
            if( sortOrder.equalsIgnoreCase( "a" ) )
            {
                while ( iSortByWhat( book, i, pivot, sortBy ) )
                {
                    i++;
                }
                //Check until all values on left side array are greater than pivot
                while ( jSortByWhat( book, j, pivot, sortBy ) )
                {
                    j--;
                }
            }
            //for decreasing sorting order i becomes j and j becomes i while sending to *sortByWhat() method.
            //to sort in descending order
            if( sortOrder.equalsIgnoreCase( "d" ) )
            {
                while ( jSortByWhat( book, i, pivot, sortBy ) )
                {
                    i++;
                }
                //Check until all values on left side array are greater than pivot
                while ( iSortByWhat( book, j, pivot, sortBy ) )
                {
                    j--;
                }
            }
            
            //Check until all values on right side array are lower than pivot
            //Now compare values from both side of lists to see if they need swapping
            //After swapping move the iterator on both lists
            if (i <= j)
            {
                swap ( book, i, j );
                i++; // imp
                j--; //imp
            }
        }
        //Do same operation as above recursively to sort two sub arrays
        if (low < j){
            quickSort( book, low, j, sortBy, sortOrder );
        }
        if (high > i){
            quickSort( book, i, high, sortBy, sortOrder );
        }
    }
     
    public static void swap (ArrayList<addBook> book, int x, int y)
    {
        addBook temp = book.get(x);
        book.set( x, book.get(y) );
        book.set( y, temp );
    }
    //for ascending order it is called first
    public boolean iSortByWhat( ArrayList<addBook> book, int i, addBook pivot, String sortBy )
    {
        if( sortBy.equalsIgnoreCase( "FirstName" ) )
        return book.get(i).firstName.compareTo( pivot.firstName) < 0;
        if( sortBy.equalsIgnoreCase( "LastName" ) )
        return book.get(i).lastName.compareTo( pivot.lastName) < 0;
        if( sortBy.equalsIgnoreCase( "Phone" ) )
        return book.get(i).phone.compareTo( pivot.phone) < 0;
        if( sortBy.equalsIgnoreCase( "Email" ) )
        return book.get(i).email.compareTo( pivot.email) < 0;
        else 
        return false;
    }
    //for descending order it is called first
    public boolean jSortByWhat( ArrayList<addBook> book, int j, addBook pivot, String sortBy )
    {
        if( sortBy.equalsIgnoreCase( "FirstName" ) )
        return book.get(j).firstName.compareTo( pivot.firstName) > 0;
        if( sortBy.equalsIgnoreCase( "LastName" ) )
        return book.get(j).lastName.compareTo( pivot.lastName) > 0;
        if( sortBy.equalsIgnoreCase( "Phone" ) )
        return book.get(j).phone.compareTo( pivot.phone) > 0;
        if( sortBy.equalsIgnoreCase( "Email" ) )
        return book.get(j).email.compareTo( pivot.email) > 0;
        else 
        return false;
    }
    
    public ArrayList searching( String searchBy, String modifier, String searchTerm, ArrayList<addBook> book )
    {
        boolean isMatch;
        ArrayList<addBook> searchContainer = new ArrayList<addBook>();
        
        for( int i = 0; i < book.size(); i++ )
        {
            isMatch = false;
            
            switch( searchBy.toLowerCase() )
            {
                case "firstname":
                {
                    if( modifier.equals( "startsWith" ) )
                    {
                        isMatch = book.get(i).firstName.startsWith(searchTerm) == true;
                        break;
                    }else  if( modifier.equals( "endsWith" ) )
                    {
                        isMatch = book.get(i).firstName.endsWith(searchTerm) == true;
                        break;
                    }
                }
                
                case "lastname":
                {
                    if( modifier.equals( "startsWith" ) )
                    {
                        isMatch = book.get(i).lastName.startsWith(searchTerm) == true;
                        break;
                    }else if(  modifier.equals( "endsWith" )  )
                    {
                        isMatch = book.get(i).lastName.endsWith(searchTerm) == true;
                        break;
                    }
                }
                
                case "phone":
                {
                    if( modifier.equals( "startsWith" ) )
                    {
                        isMatch = book.get(i).phone.startsWith(searchTerm) == true;
                        break;
                    }else if( modifier.equals( "endsWith" )  )
                    {
                        isMatch = book.get(i).phone.endsWith(searchTerm) == true;
                        break;
                    }
                }
                
                case "email":
                {
                    if( modifier.equals( "startsWith" ) )
                    {
                        isMatch = book.get(i).email.startsWith(searchTerm) == true;
                        break;
                    }else  if( modifier.equals( "endsWith" ) )
                    {
                        isMatch = book.get(i).email.endsWith(searchTerm) == true;
                        break;
                    }
                }
                
                case "address":
                {
                    if( modifier.equals( "startsWith" ) )
                    {
                        isMatch = book.get(i).address.startsWith(searchTerm) == true;
                        break;
                    }else if(  modifier.equals( "endsWith" ) )
                    {
                        isMatch = book.get(i).address.endsWith(searchTerm) == true;
                        break;
                    }
                }
            }
            
            if( isMatch == true ) 
            searchContainer.add( (book.get(i)) );
        }
        
        return searchContainer;
    }
}