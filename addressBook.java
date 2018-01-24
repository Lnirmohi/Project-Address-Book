import java.util.*;
import java.io.*;

public class addressBook
{
    public static void main( String [] args ) throws Exception
    {
        addressBook addressBook1 = new addressBook();

        addressBook1.showMenu();

        System.out.println( "\nAll changes are successfuly saved." );
    }

    //---------------------LOAD METHOD-------------------------//

    void load( Scanner scan, int counter, ArrayList<addBook> book ) throws Exception
    {
        helperAddBook helper = new helperAddBook();

        while( scan.hasNext() == true )
        {
            helper.toReadFromFile( scan, book );
            counter++;
        }

        showContent( scan, counter, book );
    }

    //--------------------SAVE TO FILE------------------------//

    void save( Scanner keyboard ) throws Exception
    {
        System.out.print( "\nEnter the name of the file to which you want to save(.txt): " );
        String fileToSave = keyboard.next();

        File file = new File( fileToSave );
        if( file.exists() == true )
        {
            System.out.print( "\nThis name is already taken by other file." );
            save( keyboard );
        }

        System.out.print( "\n\nHow many entries do you want to add? " );
        int noOfEntries = keyboard.nextInt();
        keyboard.nextLine(); //consumes "\n" of last nextInt
        
        FileWriter fileWriter = new FileWriter( file );
        PrintWriter printWriter = new PrintWriter( fileWriter );
        
        helperAddBook helper = new helperAddBook();
        for( int i = 0; i < noOfEntries; i++ )
        {
            helper.writeToFile( keyboard, printWriter );
        }

        helper.closeWriter( printWriter, fileToSave );
    }

    //---------------------ADD ENTRY-----------------------//

    void addAnEntry( Scanner keyboard, ArrayList<addBook> book ) throws Exception
    {
        helperAddBook helper = new helperAddBook();

        String fileToAdd = helper.toGetFileName( keyboard );

        File file = new File( fileToAdd );
        
        helper.addingTheEntry( file, fileToAdd, keyboard );
    }

    //---------------------REMOVE AN ENTRY-----------------------//

    void removeAnEntry( Scanner keyboard, ArrayList<addBook> book ) throws Exception
    {
        /*
         * We will remove the entry by first loading it in
         * an ArrayList and after removing the chosen element
         * by matching first name and then we will re-write
         * the whole ArrayList to the same file.
         */
        helperAddBook helper = new helperAddBook();

        File file = new File( helper.toGetFileName( keyboard ) );//sending scanner object keyboard

        //to check if the file exists or not
        if( file.exists() == true )
        {
            Scanner scan = new Scanner( file );

            //to load the file in a ArrayList
            while( scan.hasNext() == true )
            {
                helper.toReadFromFile( scan, book );
            }

            
            System.out.print( "\nEnter the first name of the entry which you want to remove: " );
            String fnameToRemove = keyboard.nextLine();
            
            boolean match = false;
            //matching with firstName and removing the entry 
            for( int i = 0; i < book.size(); i++ )
            {
                if( book.get(i).firstName.equalsIgnoreCase(fnameToRemove) == true )
                {
                    book.remove(i);
                    match = true;
                    i = 0;
                }
            }

            //runs if file does not contains that entry.
            if( match == false )
            {
                System.out.println( "\nThere is no such entry in " + file );
                book = new ArrayList<addBook>();//to ensure that empty book is passed to removeAnEntry method
                removeAnEntry( keyboard, book );
            }

            helper.writingRecordToFile( file, book );
            showMenu();
        }

        noSuchFile();
    }

    //---------------------EDIT ENTRY----------------------------//

    public void editEntry( Scanner keyboard, ArrayList<addBook> book ) throws Exception
    {
        helperAddBook helper = new helperAddBook();
        File file = new File( helper.toGetFileName( keyboard ) );

        if( file.exists() == true )
        {
            Scanner scan = new Scanner( file );

            //to write to a file method to load the file in a ArrayList
            while( scan.hasNext() == true )
            {
                helper.toReadFromFile( scan, book );
            }


            System.out.print( "\nEnter the first name of the entry which you want to edit: " );
            String fnameToEdit = keyboard.nextLine();
            String[] arr = null;
            
            boolean match = false;
            //matching with firstName and removing the entry 
            for( int i = 0; i < book.size(); i++ )
            {
                if( book.get(i).firstName.equalsIgnoreCase(fnameToEdit) == true )
                {
                    book.remove(i);
                    match = true;

                    arr = helper.takeEntries( keyboard );

                    System.out.print( "Enter new entry: " );
                    book.add( i, new addBook( arr[0], arr[1], arr[2], arr[3], arr[4] ) );
                    break;
                }
            }

            if( match == false )
            {
                System.out.println( "\nThere is no such entry in " + file );
                showMenu();
            }

            System.out.print( "\n" );
            helper.writingRecordToFile( file, book );
            System.out.println( "\nEntry with first name " + arr[0] + " is added to " + file + "." );
            showMenu();  
        }
    }

    //----------------SORT THE BOOK-----------------//

    public void sortTheBook( Scanner keyboard, ArrayList<addBook> book ) throws Exception
    {
        helperAddBook helper = new helperAddBook();
        File file = new File( helper.toGetFileName( keyboard ) );

        if( file.exists() == true )
        {
            Scanner scan = new Scanner( file );

            //to write to a file method to load the file in a ArrayList
            while( scan.hasNext() == true )
            {
                helper.toReadFromFile( scan, book );
            }

            System.out.print( "\nSort the book by: " );
            String sortBy = keyboard.nextLine();

            System.out.print( "\nSort in ascending/descending order(a/d): " );
            String sortOrder = keyboard.nextLine().toLowerCase();

            if( !sortOrder.equals( "a" ) && !sortOrder.equals( "d" )   ) 
            {
                System.out.println( "\nInvalid choice. Try again!" );
                showMenu();
            }

            switch( sortBy.toLowerCase() )
            {
                case "firstname":
                case "lastname":
                case "phone":
                case "email":
                {
                    helper.quickSort( book, 0, book.size()-1, sortBy, sortOrder );
                    break;
                }
                
                default:
                System.out.println( "\nThere is no such field of" + sortBy + " in record." );
            }

            helper.writingRecordToFile( file, book );
            System.out.println( "\n" + file + " is sorted by " + sortBy + "." );
            showMenu();
        }

        noSuchFile(); 
    }

    //--------------------SEARCH FOR AN ENTRY--------------------------//

    public void searchForSpecificEntry( Scanner keyboard, ArrayList<addBook> book ) throws Exception
    {
        helperAddBook helper= new helperAddBook();

        System.out.print( "\nEnter the name of the file from which you want to search for the entry: " );
        File file = new File( keyboard.nextLine() );

        if( file.exists() == true )
        {
            Scanner scan = new Scanner( file );

            //to write to a file method to load the file in a ArrayList
            while( scan.hasNext() == true )
            {
                helper.toReadFromFile( scan, book );
            }

            System.out.print( "\nSearch the record by(firstName/lastName/phone/email/addreess): " );
            String searchBy = keyboard.nextLine();

            System.out.print( "\nModify search term by(startsWith/endsWith): " );
            String modifier = keyboard.nextLine();

            System.out.print( "\nEnter the term to search in record: " );
            String searchTerm = keyboard.nextLine();

            book = helper.searching( searchBy, modifier, searchTerm, book);
            
            if( book.isEmpty() == true )
            {
                System.out.println( "\nThere is no such entry in the record." );
                showMenu();
            }
            
            System.out.println( "\n" + searchBy + " that " + modifier + " " + searchTerm + " are..." ); 
            showContent( keyboard, book.size(), book );
            showMenu();
        }

        noSuchFile();
    }
    
    //---------------------MOVE AN ENTRY-------------------------//
    
    public void moveAnEntry( Scanner keyboard, ArrayList<addBook> book) throws Exception
    {
        helperAddBook helper = new helperAddBook();
        
        System.out.print( "\nEnter the name of the file from which you want to move an entry: ");
        File fileToMoveFrom = new File ( keyboard.nextLine() );
        
        System.out.println( "\nEnter the file name to which you want move: " );
        File whereToMove = new File( keyboard.nextLine() );
        
        if( fileToMoveFrom.exists() == true && whereToMove.exists() == true )
        {
            Scanner scan = new Scanner( fileToMoveFrom );

            //to load the file in a ArrayList
            while( scan.hasNext() == true )
            {
                helper.toReadFromFile( scan, book );
            }
            
            System.out.println( "\nEnter the firstName of the entry which you want move: " );
            String entryToMove = keyboard.nextLine();
            
            boolean match = false; int i;
            //matching with firstName
            for( i = 0; i < book.size(); i++ )
            {
                if( book.get(i).firstName.equalsIgnoreCase(entryToMove) == true )
                {
                    match = true;
                    break;
                }
            }
            
            if( match == false )
            {
                System.out.println( "\nThere is no such entry in " + fileToMoveFrom );
                book = new ArrayList<addBook>();//to ensure that empty book is passed to removeAnEntry method
                moveAnEntry( keyboard, book );
            }
            
            helper.movingAnEntry( keyboard, i, book );
            
            showMenu();
        }
        
        System.out.println( "\nPlease enter the valid filenames." );
        noSuchFile();
    }
    
    //---------------------TAKE FILENAME-------------------------//

    Scanner getFileName( Scanner keyboard ) throws Exception
    {
        System.out.print("\nEnter name of the file: " );
        String fileName = keyboard.next();
        System.out.println();

        File file = new File( fileName );
        Scanner scan = null;

        //if user enters file name which is unavailable
        if( file.exists() == true )
        {  
            scan = new Scanner( file );
        }
        else
        {
            System.out.print( "\nNo file found of such name. Please try again." );
            Thread.sleep( 600 );
            System.out.println();

            showMenu();
        }

        return scan;
    }

    //---------------------DISPLAYING RECORDS-----------------------//

    void showContent( Scanner scan, int counter, ArrayList<addBook> book ) throws Exception
    {
        for( int j = 0; j < counter; j++ )
        {
            System.out.println( "------------------------------------------\n" );
            System.out.println( "First Name : " + book.get(j).firstName + "\n" );
            System.out.println( "Last Name  : " + book.get(j).lastName + "\n" );
            System.out.println( "Phone No.  : " + book.get(j).phone + "\n" );
            System.out.println( "Address    : " + book.get(j).address + "\n" );
            System.out.println( "Email      : " + book.get(j).email + "\n");
        }
        Thread.sleep( 2000 );
    }

    //----------------------------SHOW MENU-----------------------//

    void showMenu() throws Exception
    {
        int choice = 0;
        Scanner keyboard = new Scanner(System.in);
        helperAddBook helper = new helperAddBook();

        do
        {
            //to display menu
            helper.menuItem();

            //to make sure user enters an integer
            try
            {
                System.out.print("\nPlease choose what you'd like to do with the database(1-8): ");
                choice = keyboard.nextInt();
                keyboard.nextLine();

                //To ensure valid entry from user
                while( choice < 1 || choice > 8 )
                {
                    System.out.println("Invalid choice. Please enter a valid response(1-8)." );
                    System.out.print("\nYour Choice: ");
                    choice = keyboard.nextInt();
                }
            }
            catch( InputMismatchException e )
            {
                System.out.println("Invalid choice. Please enter a valid response(1-8)." );
                showMenu();
            }

            ArrayList<addBook> book = new ArrayList<addBook>();

            //to call the method of choice
            toRunSelectedOpt( choice, keyboard, book );
        }
        while( choice != 9 );
    }

    public void toRunSelectedOpt( int choice, Scanner keyboard, ArrayList<addBook> book ) throws Exception
    {
        int counter;
        switch( choice )
        {
            case 1:
            {
                counter = 0;
                load( getFileName(keyboard), counter, book );//getFileName returns the scanner obj conatining file
                break;
            }

            case 2:
            {
                counter = 0;
                save( keyboard );//sending scanner obj to avoid creating it everytime
                break;
            }

            case 3:
            {
                addAnEntry( keyboard, book );//sending scanner obj to avoid creating it everytime\
                showMenu();
                break;
            }

            case 4:
            {
                removeAnEntry( keyboard, book );
                break;
            }

            case 5:
            {
                editEntry( keyboard, book );
                break;
            }

            case 6:
            {
                sortTheBook( keyboard, book );
                break;
            }

            case 7: 
            {
                searchForSpecificEntry( keyboard, book );
                break;
            }
            
            case 8:
            {
                moveAnEntry( keyboard, book );
                showMenu();
                break;
            }
        }
    }
    //when no file is found in directory
    public void noSuchFile()
    {
        System.out.println( "There is no such file. Please try again." );
    }
}

//--------------------------------RECORD--------------------------------------//
class addBook
{
    String firstName, lastName, phone, address, email ;

    public addBook( String firstName, String lastName, String phone, String address, String email )
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public String toString()
    {
        return this.firstName + " " + this.lastName + " " + this.phone + " " + this.address + " " + this.email ;
    }
}